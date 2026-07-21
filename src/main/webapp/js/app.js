// Current user storage
let currentUser = null;

// Page initialization
document.addEventListener('DOMContentLoaded', () => {
    checkAuthentication();
    loadTreatments();
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

// 2. Load treatments dropdown dynamically
function loadTreatments() {
    const dropdown = document.getElementById('treatment_id');
    
    fetch('api/treatments')
        .then(response => response.json())
        .then(treatments => {
            dropdown.innerHTML = '<option value="" disabled selected>Select a Service</option>';
            treatments.forEach(t => {
                const opt = document.createElement('option');
                opt.value = t.id;
                opt.innerText = `${t.treatmentName} - LKR ${t.cost.toLocaleString('en-US', {minimumFractionDigits: 2})}`;
                dropdown.appendChild(opt);
            });
        })
        .catch(err => {
            console.error("Error loading treatments:", err);
            dropdown.innerHTML = '<option value="" disabled>Error loading services</option>';
        });
}

// Restrict appointment dates to today and the future
function setMinDate() {
    const dateInput = document.getElementById('appointment_date');
    const today = new Date().toISOString().split('T')[0];
    dateInput.min = today;
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
    }
}

// 4. Save New Appointment Details
function submitAppointment(event) {
    event.preventDefault();
    const alertBox = document.getElementById('register-alert');
    alertBox.style.display = 'none';

    const patientName = document.getElementById('patient_name').value.trim();
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
