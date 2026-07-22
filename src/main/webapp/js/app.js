// Current user storage
let currentUser = null;

// Page initialization
document.addEventListener('DOMContentLoaded', () => {
    checkAuthentication();
    loadTreatments();
    loadDentists();
    loadPatients();
    setMinDate();
});

// 1. Verify session credentials
function checkAuthentication() {
    fetch('api/auth/user')
        .then(response => {
            if (!response.ok) {
                throw new Error('Not authenticated');
            }
            return response.json();
        })
        .then(user => {
            currentUser = user;
            document.getElementById('display-user-name').innerText = user.fullName;
            document.getElementById('display-user-role').innerText = user.role;
        })
        .catch(err => {
            console.error(err);
            window.location.href = 'login.html';
        });
}

// 2. Load treatments dropdown and management table dynamically
let allTreatmentsCache = [];

function loadTreatments() {
    const dropdown = document.getElementById('treatment_id');
    const tbody = document.getElementById('treatments-mgmt-table-tbody');
    
    fetch('api/treatments')
        .then(response => response.json())
        .then(treatments => {
            allTreatmentsCache = treatments;

            if (dropdown) {
                dropdown.innerHTML = '<option value="" disabled selected>Select a Service</option>';
                treatments.forEach(t => {
                    const opt = document.createElement('option');
                    opt.value = t.id;
                    opt.innerText = `${t.treatmentName} - LKR ${t.cost.toLocaleString('en-US', {minimumFractionDigits: 2})}`;
                    dropdown.appendChild(opt);
                });
            }

            if (tbody) {
                tbody.innerHTML = '';
                if (treatments.length === 0) {
                    tbody.innerHTML = '<tr><td colspan="4" style="text-align: center; color: var(--text-muted);">No treatment packages found.</td></tr>';
                    return;
                }
                treatments.forEach(t => {
                    const tr = document.createElement('tr');
                    tr.innerHTML = `
                        <td>${t.id}</td>
                        <td style="font-weight: 600;">${t.treatmentName}</td>
                        <td style="text-align: right;">LKR ${t.cost.toLocaleString('en-US', {minimumFractionDigits: 2})}</td>
                        <td style="text-align: right;">
                            <button type="button" class="btn btn-secondary" style="padding: 4px 10px; font-size: 0.8rem; margin-right: 6px;" onclick="editTreatment(${t.id})">Edit</button>
                            <button type="button" class="btn btn-secondary" style="padding: 4px 10px; font-size: 0.8rem; border-color: rgba(239, 68, 68, 0.5); color: #f87171;" onclick="deleteTreatment(${t.id}, '${t.treatmentName.replace(/'/g, "\\'")}')">Delete</button>
                        </td>
                    `;
                    tbody.appendChild(tr);
                });
            }
        })
        .catch(err => {
            console.error("Error loading treatments:", err);
            if (dropdown) dropdown.innerHTML = '<option value="" disabled>Error loading services</option>';
            if (tbody) tbody.innerHTML = '<tr><td colspan="4" style="text-align: center; color: var(--color-danger);">Error loading treatments.</td></tr>';
        });
}

// 2b. Load dentists dropdown and management table dynamically
let allDentistsCache = [];

function loadDentists() {
    const dropdown = document.getElementById('dentist_name');
    const tbody = document.getElementById('dentists-table-tbody');

    fetch('api/dentists')
        .then(res => res.json())
        .then(dentists => {
            allDentistsCache = dentists;

            if (dropdown) {
                dropdown.innerHTML = '<option value="" disabled selected>Select a Dentist</option>';
                dentists.forEach(d => {
                    const opt = document.createElement('option');
                    opt.value = d.dentistName;
                    opt.innerText = `${d.dentistName} (${d.specialization})`;
                    dropdown.appendChild(opt);
                });
            }

            if (tbody) {
                tbody.innerHTML = '';
                if (dentists.length === 0) {
                    tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; color: var(--text-muted);">No dentist records found.</td></tr>';
                    return;
                }
                dentists.forEach(d => {
                    const tr = document.createElement('tr');
                    tr.innerHTML = `
                        <td>${d.id}</td>
                        <td style="font-weight: 600;">${d.dentistName}</td>
                        <td>${d.specialization}</td>
                        <td>${d.contactNumber || '-'}</td>
                        <td style="text-align: right;">
                            <button type="button" class="btn btn-secondary" style="padding: 4px 10px; font-size: 0.8rem; margin-right: 6px;" onclick="editDentist(${d.id})">Edit</button>
                            <button type="button" class="btn btn-secondary" style="padding: 4px 10px; font-size: 0.8rem; border-color: rgba(239, 68, 68, 0.5); color: #f87171;" onclick="deleteDentist(${d.id}, '${d.dentistName.replace(/'/g, "\\'")}')">Delete</button>
                        </td>
                    `;
                    tbody.appendChild(tr);
                });
            }
        })
        .catch(err => {
            console.error("Error loading dentists:", err);
            if (dropdown) dropdown.innerHTML = '<option value="" disabled>Error loading dentists</option>';
            if (tbody) tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; color: var(--color-danger);">Error loading dentists.</td></tr>';
        });
}

// Restrict appointment dates to today and the future
function setMinDate() {
    const dateInput = document.getElementById('appointment_date');
    if (dateInput) {
        const today = new Date().toISOString().split('T')[0];
        dateInput.min = today;
    }
}

// 2c. Load registered patients directory
let allPatientsCache = [];

function loadPatients() {
    const tbody = document.getElementById('patients-table-tbody');

    fetch('api/patients')
        .then(res => res.json())
        .then(patients => {
            allPatientsCache = patients;

            if (tbody) {
                tbody.innerHTML = '';
                if (patients.length === 0) {
                    tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; color: var(--text-muted);">No registered patients found. Please add a patient profile first.</td></tr>';
                    return;
                }
                patients.forEach(p => {
                    const tr = document.createElement('tr');
                    tr.innerHTML = `
                        <td>${p.id}</td>
                        <td style="font-weight: 600;">${p.patientName}</td>
                        <td><span style="font-family: monospace; background: rgba(255,255,255,0.1); padding: 2px 6px; border-radius: 4px;">${p.nicPassport}</span></td>
                        <td>${p.phoneNumber}</td>
                        <td>${p.address || '-'}</td>
                        <td style="text-align: right;">
                            <button type="button" class="btn btn-secondary" style="padding: 4px 10px; font-size: 0.8rem; margin-right: 6px;" onclick="editPatient(${p.id})">Edit</button>
                            <button type="button" class="btn btn-secondary" style="padding: 4px 10px; font-size: 0.8rem; border-color: rgba(239, 68, 68, 0.5); color: #f87171;" onclick="deletePatient(${p.id}, '${p.patientName.replace(/'/g, "\\'")}')">Delete</button>
                        </td>
                    `;
                    tbody.appendChild(tr);
                });
            }
        })
        .catch(err => {
            console.error("Error loading patients:", err);
            if (tbody) tbody.innerHTML = '<tr><td colspan="6" style="text-align: center; color: var(--color-danger);">Error loading patients.</td></tr>';
        });
}

// 2d. Autofill patient details by entering NIC or Passport number
function lookupPatientByNic(showAlertIfNotFound = false) {
    const inputField = document.getElementById('search_patient_nic');
    const statusMsg = document.getElementById('patient-lookup-status');
    const nameField = document.getElementById('patient_name');
    const phoneField = document.getElementById('contact_number');
    const addressField = document.getElementById('address');

    if (!inputField) return;

    const query = inputField.value.trim().toLowerCase();

    if (query === '') {
        if (nameField) nameField.value = '';
        if (phoneField) phoneField.value = '';
        if (addressField) addressField.value = '';
        if (statusMsg) statusMsg.innerHTML = '';
        return;
    }

    const patient = allPatientsCache.find(p => p.nicPassport.trim().toLowerCase() === query);

    if (patient) {
        if (nameField) nameField.value = patient.patientName;
        if (phoneField) phoneField.value = patient.phoneNumber;
        if (addressField) addressField.value = patient.address || '';
        if (statusMsg) {
            statusMsg.innerHTML = `<span style="color: #4ade80;">✓ Patient Verified: <strong>${patient.patientName}</strong> (${patient.phoneNumber})</span>`;
        }
    } else {
        if (nameField) nameField.value = '';
        if (phoneField) phoneField.value = '';
        if (addressField) addressField.value = '';
        if (statusMsg) {
            statusMsg.innerHTML = `<span style="color: #f87171;">❌ No registered patient found with NIC/Passport "${inputField.value.trim()}". Please register under Patient Registration first.</span>`;
        }
        if (showAlertIfNotFound) {
            alert(`Patient with NIC/Passport "${inputField.value.trim()}" is not registered!\n\nPlease register the patient under the "Patient Registration" tab first.`);
        }
    }
}

// 3. Tab switching framework
function switchTab(tabId) {
    // Deactivate all navigation items
    const navItems = document.querySelectorAll('.nav-item');
    navItems.forEach(item => item.classList.remove('active'));
    
    // Deactivate all content screens
    const tabs = document.querySelectorAll('.tab-content');
    tabs.forEach(tab => tab.classList.remove('active'));

    // Activate selected navigation and screen
    const activeBtn = Array.from(navItems).find(btn => btn.getAttribute('onclick').includes(tabId));
    if (activeBtn) activeBtn.classList.add('active');

    const activeTab = document.getElementById(tabId);
    if (activeTab) activeTab.classList.add('active');

    // Trigger report loading if report tab is opened
    if (tabId === 'tab-reports') {
        loadReportData();
    } else if (tabId === 'tab-users') {
        loadUsers();
    } else if (tabId === 'tab-dentists') {
        loadDentists();
    } else if (tabId === 'tab-treatments') {
        loadTreatments();
    } else if (tabId === 'tab-patients') {
        loadPatients();
    }
}

// 4. Save New Appointment Details
function submitAppointment(event) {
    event.preventDefault();
    const alertBox = document.getElementById('register-alert');
    alertBox.style.display = 'none';

    const nicInput = document.getElementById('search_patient_nic').value.trim();
    const patientName = document.getElementById('patient_name').value.trim();

    if (!patientName) {
        lookupPatientByNic(true);
        showAlert('register-alert', 'alert-danger', 'Cannot schedule appointment! The entered NIC/Passport is not registered in the system.');
        return;
    }
    const contactNumber = document.getElementById('contact_number').value.trim();
    const address = document.getElementById('address').value.trim();
    const dentistName = document.getElementById('dentist_name').value;
    const treatmentId = document.getElementById('treatment_id').value;
    const appointmentDate = document.getElementById('appointment_date').value;
    const appointmentTime = document.getElementById('appointment_time').value;

    // Contact number basic validation
    const phoneRegex = /^[0-9\+\-\s]{9,15}$/;
    if (!phoneRegex.test(contactNumber)) {
        showAlert('register-alert', 'alert-error', 'Please enter a valid telephone contact number.');
        return;
    }

    const payload = {
        patientName,
        contactNumber,
        address,
        dentistName,
        treatmentId: parseInt(treatmentId),
        appointmentDate,
        appointmentTime
    };

    fetch('api/appointments', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(err => { throw new Error(err.error || 'Failed to submit appointment'); });
        }
        return response.json();
    })
    .then(data => {
        showAlert('register-alert', 'alert-success', `Success! Appointment scheduled. ID: <strong>${data.appointmentNumber}</strong>`);
        document.getElementById('register-form').reset();
        setMinDate();
    })
    .catch(err => {
        showAlert('register-alert', 'alert-error', err.message);
    });
}

// 5. Search for Appointment by ID
function searchAppointment() {
    const searchVal = document.getElementById('search-number').value.trim();
    const alertBox = document.getElementById('search-alert');
    const resultBox = document.getElementById('search-result-box');
    const billBox = document.getElementById('search-bill-box');

    alertBox.style.display = 'none';
    resultBox.style.display = 'none';
    billBox.style.display = 'none';

    if (!searchVal) {
        alertBox.style.display = 'flex';
        alertBox.innerText = '⚠️ Please enter an appointment code';
        return;
    }

    // Fetch appointment record
    fetch(`api/appointments?number=${encodeURIComponent(searchVal)}`)
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => { throw new Error(err.error || 'Search failed'); });
            }
            return response.json();
        })
        .then(app => {
            // Display appointment properties
            document.getElementById('result-apt-num').innerText = app.appointmentNumber;
            document.getElementById('result-patient-name').innerText = app.patientName;
            document.getElementById('result-contact').innerText = app.contactNumber;
            document.getElementById('result-address').innerText = app.address || 'N/A';
            document.getElementById('result-dentist').innerText = app.dentistName;
            document.getElementById('result-treatment').innerText = app.treatmentName;
            document.getElementById('result-datetime').innerText = `${app.appointmentDate} @ ${app.appointmentTime.substring(0, 5)}`;
            document.getElementById('result-treatment-cost').innerText = `LKR ${app.treatmentCost.toLocaleString('en-US', {minimumFractionDigits: 2})}`;

            resultBox.style.display = 'block';

            // Check if a bill is already generated
            fetch(`api/bills?appointment_number=${encodeURIComponent(searchVal)}`)
                .then(bResp => {
                    if (bResp.ok) {
                        return bResp.json();
                    }
                    return null;
                })
                .then(bill => {
                    if (bill) {
                        document.getElementById('result-bill-consultation').innerText = `LKR ${bill.consultationFee.toLocaleString('en-US', {minimumFractionDigits: 2})}`;
                        document.getElementById('result-bill-total').innerText = `LKR ${bill.totalCost.toLocaleString('en-US', {minimumFractionDigits: 2})}`;
                        billBox.style.display = 'block';
                    }
                });
        })
        .catch(err => {
            alertBox.style.display = 'flex';
            alertBox.innerText = `⚠️ ${err.message}`;
        });
}

// 6. Calculate bill & call stored procedure to print invoice
function generateInvoice(event) {
    event.preventDefault();
    const alertBox = document.getElementById('billing-alert');
    const receiptBox = document.getElementById('receipt-display');
    
    alertBox.style.display = 'none';
    receiptBox.style.display = 'none';

    const appointmentNumber = document.getElementById('bill-apt-number').value.trim();
    const consultationFee = document.getElementById('bill-consultation-fee').value.trim();

    const payload = {
        appointment_number: appointmentNumber,
        consultation_fee: consultationFee
    };

    fetch('api/bills', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(err => { throw new Error(err.error || 'Billing generation failed'); });
        }
        return response.json();
    })
    .then(bill => {
        // Populate printable invoice
        document.getElementById('rc-ref').innerText = `INV-${bill.billId + 10000}`;
        document.getElementById('rc-apt').innerText = bill.appointmentNumber;
        document.getElementById('rc-patient').innerText = bill.patientName;
        
        // Format Bill Date
        let bDate = new Date();
        if (bill.billDate) {
            bDate = new Date(bill.billDate);
        }
        document.getElementById('rc-date').innerText = bDate.toLocaleString();

        document.getElementById('rc-treatment-name').innerText = bill.treatmentName;
        document.getElementById('rc-treatment-cost').innerText = bill.treatmentCost.toLocaleString('en-US', {minimumFractionDigits: 2});
        document.getElementById('rc-consultation-fee').innerText = bill.consultationFee.toLocaleString('en-US', {minimumFractionDigits: 2});
        document.getElementById('rc-total').innerText = `${bill.totalCost.toLocaleString('en-US', {minimumFractionDigits: 2})} LKR`;

        // Present invoice panel
        receiptBox.style.display = 'block';
        showAlert('billing-alert', 'alert-success', 'Invoice generated and saved in database successfully!');
        document.getElementById('billing-form').reset();
    })
    .catch(err => {
        showAlert('billing-alert', 'alert-error', err.message);
    });
}

// 7. Load Reports statistics datasets
function loadReportData() {
    // Fetch dashboard numeric totals
    fetch('api/bills/summary')
        .then(response => response.json())
        .then(sum => {
            document.getElementById('stats-total-appointments').innerText = sum.total_bills;
            document.getElementById('stats-total-revenue').innerText = sum.total_revenue.toLocaleString('en-US', {minimumFractionDigits: 2});
            document.getElementById('stats-consultation-revenue').innerText = sum.total_consultation_fees.toLocaleString('en-US', {minimumFractionDigits: 2});
        })
        .catch(err => console.error("Error loading financial summary:", err));

    // Fetch treatment popularity list
    fetch('api/bills/treatments')
        .then(response => response.json())
        .then(list => {
            const tbody = document.getElementById('report-treatments-tbody');
            if (list.length === 0) {
                tbody.innerHTML = '<tr><td colspan="3" style="text-align: center; color: var(--text-muted);">No records found</td></tr>';
                return;
            }
            tbody.innerHTML = '';
            list.forEach(item => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${item.treatment_name}</td>
                    <td>${item.appointment_count}</td>
                    <td style="text-align: right; font-weight: 600; color: var(--color-success);">
                        LKR ${item.total_earnings.toLocaleString('en-US', {minimumFractionDigits: 2})}
                    </td>
                `;
                tbody.appendChild(row);
            });
        })
        .catch(err => console.error("Error loading treatments report:", err));

    // Fetch dentist workload distribution
    fetch('api/appointments/stats')
        .then(response => response.json())
        .then(list => {
            const tbody = document.getElementById('report-dentists-tbody');
            if (list.length === 0) {
                tbody.innerHTML = '<tr><td colspan="2" style="text-align: center; color: var(--text-muted);">No records found</td></tr>';
                return;
            }
            tbody.innerHTML = '';
            list.forEach(item => {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${item.dentist_name}</td>
                    <td style="font-weight: 600;">${item.appointment_count}</td>
                `;
                tbody.appendChild(row);
            });
        })
        .catch(err => console.error("Error loading dentist workload stats:", err));
}

// 8. Sign-out and session invalidation
function handleLogout() {
    if (confirm("Are you sure you want to exit and log out from the system?")) {
        fetch('api/auth/logout', { method: 'POST' })
            .then(() => {
                window.location.href = 'login.html';
            })
            .catch(() => {
                window.location.href = 'login.html';
            });
    }
}

// 9. Interactive help guide toggles
function toggleFaq(header) {
    const item = header.parentElement;
    const answer = item.querySelector('.faq-answer');
    const arrow = header.querySelector('span:last-child');
    
    // Toggle active state
    if (answer.style.display === 'none' || !answer.style.display) {
        answer.style.display = 'block';
        arrow.innerText = '▲';
        arrow.style.color = 'var(--color-primary)';
    } else {
        answer.style.display = 'none';
        arrow.innerText = '▼';
        arrow.style.color = '';
    }
}

// Helper alert populator
function showAlert(boxId, alertClass, message) {
    const alertBox = document.getElementById(boxId);
    alertBox.className = `alert ${alertClass}`;
    alertBox.innerHTML = `<span>${alertClass === 'alert-success' ? '✅' : '⚠️'}</span><span>${message}</span>`;
    alertBox.style.display = 'flex';
}

// 10. User Account Management (Admin Only)
let allUsersCache = [];

function loadUsers() {
    const tbody = document.getElementById('users-table-tbody');
    if (!tbody) return;
    tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; color: var(--text-muted);">Loading users...</td></tr>';

    fetch('api/users')
        .then(res => {
            if (!res.ok) throw new Error('Failed to load user accounts');
            return res.json();
        })
        .then(users => {
            allUsersCache = users;
            tbody.innerHTML = '';
            if (users.length === 0) {
                tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; color: var(--text-muted);">No user accounts found.</td></tr>';
                return;
            }

            users.forEach(u => {
                const tr = document.createElement('tr');
                const roleBadge = u.role === 'Admin' ? 
                    '<span style="background: rgba(14, 165, 233, 0.2); color: #38bdf8; padding: 2px 8px; border-radius: 4px; font-size: 0.8rem; font-weight: 600;">Admin</span>' : 
                    '<span style="background: rgba(148, 163, 184, 0.2); color: #cbd5e1; padding: 2px 8px; border-radius: 4px; font-size: 0.8rem;">Staff</span>';

                tr.innerHTML = `
                    <td>${u.id}</td>
                    <td style="font-weight: 600;">${u.username}</td>
                    <td>${u.fullName}</td>
                    <td>${roleBadge}</td>
                    <td style="text-align: right;">
                        <button type="button" class="btn btn-secondary" style="padding: 4px 10px; font-size: 0.8rem; margin-right: 6px;" onclick="editUser(${u.id})">Edit</button>
                        <button type="button" class="btn btn-secondary" style="padding: 4px 10px; font-size: 0.8rem; border-color: rgba(239, 68, 68, 0.5); color: #f87171;" onclick="deleteUser(${u.id}, '${u.username}')">Delete</button>
                    </td>
                `;
                tbody.appendChild(tr);
            });
        })
        .catch(err => {
            console.error(err);
            tbody.innerHTML = '<tr><td colspan="5" style="text-align: center; color: var(--color-danger);">Error loading users.</td></tr>';
        });
}

function submitUserForm(event) {
    event.preventDefault();
    const alertBox = document.getElementById('user-alert');
    alertBox.style.display = 'none';

    const userId = document.getElementById('user-id').value;
    const username = document.getElementById('user-username').value.trim();
    const fullName = document.getElementById('user-fullname').value.trim();
    const role = document.getElementById('user-role').value;
    const password = document.getElementById('user-password').value;
    const confirmPassword = document.getElementById('user-confirm-password').value;

    if (password !== confirmPassword) {
        showAlert('user-alert', 'alert-danger', 'Password and Confirm Password do not match!');
        return;
    }

    const payload = {
        id: userId,
        username: username,
        full_name: fullName,
        role: role,
        password: password
    };

    fetch('api/users', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
    .then(res => res.json())
    .then(data => {
        if (data.error) {
            showAlert('user-alert', 'alert-danger', data.error);
        } else {
            showAlert('user-alert', 'alert-success', data.message);
            resetUserForm();
            loadUsers();
        }
    })
    .catch(err => {
        console.error(err);
        showAlert('user-alert', 'alert-danger', 'An unexpected error occurred while saving user.');
    });
}

function editUser(userId) {
    const user = allUsersCache.find(u => u.id === userId);
    if (!user) return;

    document.getElementById('user-id').value = user.id;
    document.getElementById('user-username').value = user.username;
    document.getElementById('user-fullname').value = user.fullName;
    document.getElementById('user-role').value = user.role;
    document.getElementById('user-password').value = '';
    document.getElementById('user-confirm-password').value = '';
    
    document.getElementById('user-password-hint').innerText = '(Leave blank to keep existing password)';
    document.getElementById('btn-save-user').innerText = 'Update User Account';
    
    const alertBox = document.getElementById('user-alert');
    alertBox.style.display = 'none';
    
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

function resetUserForm() {
    document.getElementById('user-form').reset();
    document.getElementById('user-id').value = '0';
    document.getElementById('user-password').value = '';
    document.getElementById('user-confirm-password').value = '';
    document.getElementById('user-password-hint').innerText = '(Required for new users)';
    document.getElementById('btn-save-user').innerText = 'Save User Account';
}

function deleteUser(userId, username) {
    if (currentUser && currentUser.id === userId) {
        alert("You cannot delete your own logged-in account!");
        return;
    }

    if (confirm(`Are you sure you want to delete user account '${username}'?`)) {
        fetch('api/users', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ action: 'delete', id: String(userId) })
        })
        .then(res => res.json())
        .then(data => {
            if (data.error) {
                showAlert('user-alert', 'alert-danger', data.error);
            } else {
                showAlert('user-alert', 'alert-success', data.message);
                loadUsers();
            }
        })
        .catch(err => {
            console.error(err);
            showAlert('user-alert', 'alert-danger', 'Failed to delete user.');
        });
    }
}

// 11. Dentist Management (Admin Only)
function submitDentistForm(event) {
    event.preventDefault();
    const alertBox = document.getElementById('dentist-alert');
    alertBox.style.display = 'none';

    const id = document.getElementById('dentist-id').value;
    const name = document.getElementById('dentist-name-input').value.trim();
    const spec = document.getElementById('dentist-specialization').value.trim();
    const phone = document.getElementById('dentist-contact').value.trim();

    fetch('api/dentists', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: id, dentist_name: name, specialization: spec, contact_number: phone })
    })
    .then(res => res.json())
    .then(data => {
        if (data.error) {
            showAlert('dentist-alert', 'alert-danger', data.error);
        } else {
            showAlert('dentist-alert', 'alert-success', data.message);
            resetDentistForm();
            loadDentists();
        }
    })
    .catch(err => {
        console.error(err);
        showAlert('dentist-alert', 'alert-danger', 'An error occurred while saving dentist profile.');
    });
}

function editDentist(id) {
    const dentist = allDentistsCache.find(d => d.id === id);
    if (!dentist) return;

    document.getElementById('dentist-id').value = dentist.id;
    document.getElementById('dentist-name-input').value = dentist.dentistName;
    document.getElementById('dentist-specialization').value = dentist.specialization;
    document.getElementById('dentist-contact').value = dentist.contactNumber || '';
    document.getElementById('btn-save-dentist').innerText = 'Update Dentist Profile';

    document.getElementById('dentist-alert').style.display = 'none';
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

function resetDentistForm() {
    document.getElementById('dentist-form').reset();
    document.getElementById('dentist-id').value = '0';
    document.getElementById('btn-save-dentist').innerText = 'Save Dentist Profile';
}

function deleteDentist(id, name) {
    if (confirm(`Are you sure you want to delete dentist record '${name}'?`)) {
        fetch('api/dentists', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ action: 'delete', id: String(id) })
        })
        .then(res => res.json())
        .then(data => {
            if (data.error) {
                showAlert('dentist-alert', 'alert-danger', data.error);
            } else {
                showAlert('dentist-alert', 'alert-success', data.message);
                loadDentists();
            }
        })
        .catch(err => {
            console.error(err);
            showAlert('dentist-alert', 'alert-danger', 'Failed to delete dentist.');
        });
    }
}

// 12. Treatment Package Management (Admin Only)
function submitTreatmentForm(event) {
    event.preventDefault();
    const alertBox = document.getElementById('treatment-alert');
    alertBox.style.display = 'none';

    const id = document.getElementById('treatment-id-input').value;
    const name = document.getElementById('treatment-name-input').value.trim();
    const cost = document.getElementById('treatment-cost-input').value;

    fetch('api/treatments', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: id, treatment_name: name, cost: cost })
    })
    .then(res => res.json())
    .then(data => {
        if (data.error) {
            showAlert('treatment-alert', 'alert-danger', data.error);
        } else {
            showAlert('treatment-alert', 'alert-success', data.message);
            resetTreatmentForm();
            loadTreatments();
        }
    })
    .catch(err => {
        console.error(err);
        showAlert('treatment-alert', 'alert-danger', 'An error occurred while saving treatment service.');
    });
}

function editTreatment(id) {
    const treatment = allTreatmentsCache.find(t => t.id === id);
    if (!treatment) return;

    document.getElementById('treatment-id-input').value = treatment.id;
    document.getElementById('treatment-name-input').value = treatment.treatmentName;
    document.getElementById('treatment-cost-input').value = treatment.cost;
    document.getElementById('btn-save-treatment').innerText = 'Update Treatment Service';

    document.getElementById('treatment-alert').style.display = 'none';
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

function resetTreatmentForm() {
    document.getElementById('treatment-form').reset();
    document.getElementById('treatment-id-input').value = '0';
    document.getElementById('btn-save-treatment').innerText = 'Save Treatment Service';
}

function deleteTreatment(id, name) {
    if (confirm(`Are you sure you want to delete treatment package '${name}'?`)) {
        fetch('api/treatments', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ action: 'delete', id: String(id) })
        })
        .then(res => res.json())
        .then(data => {
            if (data.error) {
                showAlert('treatment-alert', 'alert-danger', data.error);
            } else {
                showAlert('treatment-alert', 'alert-success', data.message);
                loadTreatments();
            }
        })
        .catch(err => {
            console.error(err);
            showAlert('treatment-alert', 'alert-danger', 'Failed to delete treatment package.');
        });
    }
}

// 13. Patient Registration & Profile Management (Staff & Admin)
function submitPatientForm(event) {
    event.preventDefault();
    const alertBox = document.getElementById('patient-alert');
    alertBox.style.display = 'none';

    const id = document.getElementById('patient-id').value;
    const name = document.getElementById('patient-name-input').value.trim();
    const nic = document.getElementById('patient-nic-input').value.trim();
    const phone = document.getElementById('patient-phone-input').value.trim();
    const address = document.getElementById('patient-address-input').value.trim();

    fetch('api/patients', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ id: id, patient_name: name, nic_passport: nic, phone_number: phone, address: address })
    })
    .then(res => res.json())
    .then(data => {
        if (data.error) {
            showAlert('patient-alert', 'alert-danger', data.error);
        } else {
            showAlert('patient-alert', 'alert-success', data.message);
            resetPatientForm();
            loadPatients();
        }
    })
    .catch(err => {
        console.error(err);
        showAlert('patient-alert', 'alert-danger', 'An error occurred while saving patient profile.');
    });
}

function editPatient(id) {
    const patient = allPatientsCache.find(p => p.id === id);
    if (!patient) return;

    document.getElementById('patient-id').value = patient.id;
    document.getElementById('patient-name-input').value = patient.patientName;
    document.getElementById('patient-nic-input').value = patient.nicPassport;
    document.getElementById('patient-phone-input').value = patient.phoneNumber;
    document.getElementById('patient-address-input').value = patient.address || '';
    document.getElementById('btn-save-patient').innerText = 'Update Patient Profile';

    document.getElementById('patient-alert').style.display = 'none';
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

function resetPatientForm() {
    document.getElementById('patient-form').reset();
    document.getElementById('patient-id').value = '0';
    document.getElementById('btn-save-patient').innerText = 'Save Patient Profile';
}

function deletePatient(id, name) {
    if (confirm(`Are you sure you want to delete patient record '${name}'?`)) {
        fetch('api/patients', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ action: 'delete', id: String(id) })
        })
        .then(res => res.json())
        .then(data => {
            if (data.error) {
                showAlert('patient-alert', 'alert-danger', data.error);
            } else {
                showAlert('patient-alert', 'alert-success', data.message);
                loadPatients();
            }
        })
        .catch(err => {
            console.error(err);
            showAlert('patient-alert', 'alert-danger', 'Failed to delete patient record.');
        });
    }
}
