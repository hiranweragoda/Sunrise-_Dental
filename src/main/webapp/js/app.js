// Traditional Java Web MVC Client Utility Functions

document.addEventListener('DOMContentLoaded', () => {
    setMinDate();
    initTabNavigation();
});

// Set minimum date on appointment date pickers to today
function setMinDate() {
    const dateInput = document.getElementById('appointment_date');
    if (dateInput) {
        const today = new Date().toISOString().split('T')[0];
        dateInput.min = today;
    }
    const updateDateInput = document.getElementById('update_appointment_date');
    if (updateDateInput) {
        const today = new Date().toISOString().split('T')[0];
        updateDateInput.min = today;
    }
}

// Client-side Tab Navigation Handler
function switchTab(tabId) {
    // Hide all tab contents
    document.querySelectorAll('.tab-content').forEach(section => {
        section.classList.remove('active');
    });

    // Remove active class from all nav items
    document.querySelectorAll('.sidebar .nav-item').forEach(button => {
        button.classList.remove('active');
    });

    // Activate selected tab content
    const selectedTab = document.getElementById(tabId);
    if (selectedTab) {
        selectedTab.classList.add('active');
    }

    // Activate corresponding sidebar nav button
    const targetNavBtn = document.querySelector(`.sidebar .nav-item[onclick*="${tabId}"]`);
    if (targetNavBtn) {
        targetNavBtn.classList.add('active');
    }
}

function initTabNavigation() {
    // Check URL parameters for active tab
    const urlParams = new URLSearchParams(window.location.search);
    const tabParam = urlParams.get('tab');
    if (tabParam) {
        switchTab(tabParam);
    }
}

// Toggle Inline Appointment Update Form
function toggleApptUpdateForm() {
    const form = document.getElementById('appt-update-form');
    if (form) {
        form.style.display = (form.style.display === 'none' || form.style.display === '') ? 'block' : 'none';
        if (form.style.display === 'block') {
            form.scrollIntoView({ behavior: 'smooth' });
        }
    }
}

// Handle Data Attributes for Edit
function handleEditPatient(btn) {
    const id = btn.getAttribute('data-id');
    const name = btn.getAttribute('data-name');
    const nic = btn.getAttribute('data-nic');
    const phone = btn.getAttribute('data-phone');
    const address = btn.getAttribute('data-address');
    editPatient(id, name, nic, phone, address);
}

function handleEditUser(btn) {
    const id = btn.getAttribute('data-id');
    const username = btn.getAttribute('data-username');
    const fullname = btn.getAttribute('data-fullname');
    const role = btn.getAttribute('data-role');
    editUser(id, username, fullname, role);
}

function handleEditDentist(btn) {
    const id = btn.getAttribute('data-id');
    const name = btn.getAttribute('data-name');
    const spec = btn.getAttribute('data-spec');
    const contact = btn.getAttribute('data-contact');
    editDentist(id, name, spec, contact);
}

function handleEditTreatment(btn) {
    const id = btn.getAttribute('data-id');
    const name = btn.getAttribute('data-name');
    const cost = btn.getAttribute('data-cost');
    editTreatment(id, name, cost);
}

// Edit populate functions
function editPatient(id, name, nic, phone, address) {
    switchTab('tab-patients');
    const idInput = document.getElementById('patient-id');
    if (idInput) idInput.value = id;
    const nameInput = document.getElementById('patient-name-input');
    if (nameInput) nameInput.value = name;
    const nicInput = document.getElementById('patient-nic-input');
    if (nicInput) nicInput.value = nic;
    const phoneInput = document.getElementById('patient-phone-input');
    if (phoneInput) phoneInput.value = phone;
    const addrInput = document.getElementById('patient-address-input');
    if (addrInput) addrInput.value = address || '';
    
    const submitBtn = document.querySelector('#tab-patients form button[type="submit"]');
    if (submitBtn) submitBtn.textContent = 'Update Patient Profile';
    if (nameInput) nameInput.focus();
}

function editUser(id, username, fullName, role) {
    switchTab('tab-users');
    const idInput = document.getElementById('user-id');
    if (idInput) idInput.value = id;
    const userInput = document.getElementById('user-username');
    if (userInput) userInput.value = username;
    const fullInput = document.getElementById('user-fullname');
    if (fullInput) fullInput.value = fullName;
    const roleInput = document.getElementById('user-role');
    if (roleInput) roleInput.value = role;

    const pwdInput = document.getElementById('user-password');
    if (pwdInput) pwdInput.placeholder = '(Leave blank to keep current password)';

    const submitBtn = document.querySelector('#tab-users form button[type="submit"]');
    if (submitBtn) submitBtn.textContent = 'Update User Account';
    if (userInput) userInput.focus();
}

function editDentist(id, name, specialization, contact) {
    switchTab('tab-dentists');
    const idInput = document.getElementById('dentist-id');
    if (idInput) idInput.value = id;
    const nameInput = document.getElementById('dentist-name-input');
    if (nameInput) nameInput.value = name;
    const specInput = document.getElementById('dentist-specialization');
    if (specInput) specInput.value = specialization;
    const contactInput = document.getElementById('dentist-contact');
    if (contactInput) contactInput.value = contact || '';

    const submitBtn = document.querySelector('#tab-dentists form button[type="submit"]');
    if (submitBtn) submitBtn.textContent = 'Update Dentist Profile';
    if (nameInput) nameInput.focus();
}

function editTreatment(id, name, cost) {
    switchTab('tab-treatments');
    const idInput = document.getElementById('treatment-id-input');
    if (idInput) idInput.value = id;
    const nameInput = document.getElementById('treatment-name-input');
    if (nameInput) nameInput.value = name;
    const costInput = document.getElementById('treatment-cost-input');
    if (costInput) costInput.value = cost;

    const submitBtn = document.querySelector('#tab-treatments form button[type="submit"]');
    if (submitBtn) submitBtn.textContent = 'Update Treatment Service';
    if (nameInput) nameInput.focus();
}

// Form reset helper functions
function resetPatientForm() {
    const form = document.querySelector('#tab-patients form');
    if (form) {
        form.reset();
        const idInput = document.getElementById('patient-id');
        if (idInput) idInput.value = '0';
        const submitBtn = form.querySelector('button[type="submit"]');
        if (submitBtn) submitBtn.textContent = 'Save Patient Profile';
    }
}

function resetDentistForm() {
    const form = document.querySelector('#tab-dentists form');
    if (form) {
        form.reset();
        const idInput = document.getElementById('dentist-id');
        if (idInput) idInput.value = '0';
        const submitBtn = form.querySelector('button[type="submit"]');
        if (submitBtn) submitBtn.textContent = 'Save Dentist Profile';
    }
}

function resetTreatmentForm() {
    const form = document.querySelector('#tab-treatments form');
    if (form) {
        form.reset();
        const idInput = document.getElementById('treatment-id-input');
        if (idInput) idInput.value = '0';
        const submitBtn = form.querySelector('button[type="submit"]');
        if (submitBtn) submitBtn.textContent = 'Save Treatment Service';
    }
}

function resetUserForm() {
    const form = document.querySelector('#tab-users form');
    if (form) {
        form.reset();
        const idInput = document.getElementById('user-id');
        if (idInput) idInput.value = '0';
        const submitBtn = form.querySelector('button[type="submit"]');
        if (submitBtn) submitBtn.textContent = 'Save User Account';
        const pwdInput = document.getElementById('user-password');
        if (pwdInput) pwdInput.placeholder = '••••••••';
    }
}

// System Logout redirect
function handleLogout() {
    window.location.href = 'logout';
}
