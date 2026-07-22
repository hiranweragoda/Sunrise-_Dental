<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sunrisedental.model.User" %>
<%
    User sessionUser = (User) session.getAttribute("user");
    if (sessionUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    String fullName = sessionUser.getFullName();
    String role = sessionUser.getRole();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="MVC management dashboard for Sunrise Dental Clinic staff.">
    <title>Dashboard - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

    <!-- Blurred background decor shapes -->
    <div class="bg-decor decor-1"></div>
    <div class="bg-decor decor-2"></div>

    <header>
        <div class="header-container">
            <div class="logo-section">
                <span class="logo-icon">🦷</span>
                <span class="logo-text">Sunrise Dental (MVC)</span>
            </div>
            <div class="user-profile">
                <div class="user-info">
                    <div id="display-user-name" class="user-name"><%= fullName %></div>
                    <div id="display-user-role" class="user-role"><%= role %></div>
                </div>
            </div>
        </div>
    </header>

    <div class="main-wrapper">
        <!-- Sidebar Navigation -->
        <aside class="sidebar">
            <button class="nav-item active" onclick="switchTab('tab-register')">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z" />
                </svg>
                <span>New Appointment</span>
            </button>
            <button class="nav-item" onclick="switchTab('tab-search')">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                </svg>
                <span>Search Details</span>
            </button>
            <button class="nav-item" onclick="switchTab('tab-billing')">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M9 7h6m0 10v-3m-3 3h.01M9 17h.01M9 14h.01M12 14h.01M15 11h.01M12 11h.01M9 11h.01M7 21h10a2 2 0 002-2V5a2 2 0 00-2-2H7a2 2 0 00-2 2v14a2 2 0 002 2z" />
                </svg>
                <span>Calculate & Bill</span>
            </button>
            <% if ("Admin".equals(role)) { %>
            <button class="nav-item" onclick="switchTab('tab-users')">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
                </svg>
                <span>User Management</span>
            </button>
            <button class="nav-item" onclick="switchTab('tab-dentists')">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                </svg>
                <span>Dentist Management</span>
            </button>
            <button class="nav-item" onclick="switchTab('tab-treatments')">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M19.428 15.428a2 2 0 00-1.022-.547l-2.387-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L5.605 15.13a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 10.172V5L8 4z" />
                </svg>
                <span>Treatment Packages</span>
            </button>
            <button class="nav-item" onclick="switchTab('tab-reports')">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
                </svg>
                <span>Clinic Reports</span>
            </button>
            <% } %>
            <button class="nav-item" onclick="switchTab('tab-help')">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <span>Help Section</span>
            </button>
            <button class="nav-item logout" onclick="handleLogout()">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                </svg>
                <span>Exit System</span>
            </button>
        </aside>

        <!-- Main Content Area -->
        <main class="content-area">

            <!-- TAB 1: REGISTER APPOINTMENT -->
            <section id="tab-register" class="tab-content active">
                <div class="panel">
                    <div class="panel-header">
                        <h2 class="panel-title">Register Appointment</h2>
                        <p class="panel-subtitle">Create a new patient checkup schedule</p>
                    </div>

                    <div id="register-alert" class="alert" style="display: none;"></div>

                    <form id="register-form" onsubmit="submitAppointment(event)">
                        <div class="form-grid">
                            <div class="form-group">
                                <label for="patient_name">Patient Full Name</label>
                                <input type="text" id="patient_name" placeholder="John Doe" required minlength="3">
                            </div>

                            <div class="form-group">
                                <label for="contact_number">Contact Number</label>
                                <input type="tel" id="contact_number" placeholder="e.g. 0771234567" required>
                            </div>

                            <div class="form-group full-width">
                                <label for="address">Address</label>
                                <input type="text" id="address" placeholder="123 Galle Road, Colombo 03">
                            </div>

                            <div class="form-group">
                                <label for="dentist_name">Dentist Name</label>
                                <select id="dentist_name" required>
                                    <option value="" disabled selected>Loading Dentists...</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="treatment_id">Treatment Service</label>
                                <select id="treatment_id" required>
                                    <option value="" disabled selected>Loading Treatments...</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="appointment_date">Appointment Date</label>
                                <input type="date" id="appointment_date" required>
                            </div>

                            <div class="form-group">
                                <label for="appointment_time">Appointment Time</label>
                                <input type="time" id="appointment_time" required>
                            </div>
                        </div>

                        <div style="margin-top: 2rem; display: flex; justify-content: flex-end; gap: 1rem;">
                            <button type="reset" class="btn btn-secondary">Clear Fields</button>
                            <button type="submit" class="btn btn-primary">Register Appointment</button>
                        </div>
                    </form>
                </div>
            </section>

            <!-- TAB 2: SEARCH DETAILS -->
            <section id="tab-search" class="tab-content">
                <div class="panel">
                    <div class="panel-header">
                        <h2 class="panel-title">Display Appointment Details</h2>
                        <p class="panel-subtitle">Find registered patient records by appointment number</p>
                    </div>

                    <div class="search-container">
                        <div class="search-input-wrapper">
                            <svg class="search-icon-svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                                <path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                            </svg>
                            <input type="text" id="search-number" placeholder="Enter Appointment Number (e.g., APT-12345)" required>
                        </div>
                        <button type="button" class="btn btn-primary" onclick="searchAppointment()">Search</button>
                    </div>

                    <div id="search-alert" class="alert alert-error" style="display: none;"></div>

                    <div id="search-result-box" style="display: none;">
                        <div class="details-card">
                            <div class="details-grid">
                                <div class="details-item">
                                    <div class="details-label">Appointment ID</div>
                                    <div id="result-apt-num" class="details-value text-primary" style="color: var(--color-primary);">--</div>
                                </div>
                                <div class="details-item">
                                    <div class="details-label">Patient Name</div>
                                    <div id="result-patient-name" class="details-value">--</div>
                                </div>
                                <div class="details-item">
                                    <div class="details-label">Contact Number</div>
                                    <div id="result-contact" class="details-value">--</div>
                                </div>
                                <div class="details-item">
                                    <div class="details-label">Home Address</div>
                                    <div id="result-address" class="details-value">--</div>
                                </div>
                                <div class="details-item">
                                    <div class="details-label">Consulting Dentist</div>
                                    <div id="result-dentist" class="details-value">--</div>
                                </div>
                                <div class="details-item">
                                    <div class="details-label">Treatment Type</div>
                                    <div id="result-treatment" class="details-value">--</div>
                                </div>
                                <div class="details-item">
                                    <div class="details-label">Date & Time</div>
                                    <div id="result-datetime" class="details-value">--</div>
                                </div>
                                <div class="details-item">
                                    <div class="details-label">Treatment Cost</div>
                                    <div id="result-treatment-cost" class="details-value">--</div>
                                </div>
                            </div>
                        </div>

                        <!-- Bill details inside search if generated -->
                        <div id="search-bill-box" class="details-card" style="margin-top: 1.5rem; background: rgba(16, 185, 129, 0.05); border-color: rgba(16, 185, 129, 0.2); display: none;">
                            <h3 style="color: #34d399; font-size: 1.1rem; margin-bottom: 1rem;">Invoice Generated</h3>
                            <div class="details-grid">
                                <div class="details-item">
                                    <div class="details-label">Consultation Fee</div>
                                    <div id="result-bill-consultation" class="details-value">--</div>
                                </div>
                                <div class="details-item">
                                    <div class="details-label">Total Invoice Cost</div>
                                    <div id="result-bill-total" class="details-value" style="color: #34d399; font-weight: 700;">--</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <!-- TAB 3: BILLING SYSTEM -->
            <section id="tab-billing" class="tab-content">
                <div class="panel">
                    <div class="panel-header">
                        <h2 class="panel-title">Patient Billing Center</h2>
                        <p class="panel-subtitle">Compute final treatment cost and generate printable receipt</p>
                    </div>

                    <div id="billing-alert" class="alert" style="display: none;"></div>

                    <form id="billing-form" onsubmit="generateInvoice(event)">
                        <div class="form-grid">
                            <div class="form-group">
                                <label for="bill-apt-number">Appointment Number</label>
                                <input type="text" id="bill-apt-number" placeholder="e.g. APT-42351" required>
                            </div>
                            <div class="form-group">
                                <label for="bill-consultation-fee">Consultation Fee (LKR)</label>
                                <input type="number" id="bill-consultation-fee" placeholder="e.g. 2500" min="0" step="0.01" required>
                            </div>
                        </div>
                        <div style="margin-top: 1.5rem; display: flex; justify-content: flex-end;">
                            <button type="submit" class="btn btn-success">
                                <span>💵</span> Calculate & Generate Invoice
                            </button>
                        </div>
                    </form>

                    <!-- Receipt View (Printable) -->
                    <div id="receipt-display" style="display: none;">
                        <div class="bill-receipt-box" id="printable-receipt">
                            <div class="receipt-header">
                                <div class="receipt-logo">SUNRISE DENTAL CLINIC</div>
                                <div class="receipt-subtitle">Colombo Road, Colombo, Sri Lanka</div>
                                <div class="receipt-subtitle">Tel: +94 11 234 5678</div>
                            </div>

                            <div class="receipt-info-row">
                                <span class="receipt-info-label">Invoice Ref:</span>
                                <span class="receipt-info-value" id="rc-ref">INV-10023</span>
                            </div>
                            <div class="receipt-info-row">
                                <span class="receipt-info-label">Appointment No:</span>
                                <span class="receipt-info-value" id="rc-apt">APT-98245</span>
                            </div>
                            <div class="receipt-info-row">
                                <span class="receipt-info-label">Patient Name:</span>
                                <span class="receipt-info-value" id="rc-patient">John Doe</span>
                            </div>
                            <div class="receipt-info-row">
                                <span class="receipt-info-label">Date:</span>
                                <span class="receipt-info-value" id="rc-date">July 21, 2026</span>
                            </div>

                            <div class="receipt-divider"></div>

                            <table class="receipt-items-table">
                                <thead>
                                    <tr>
                                        <th>Description</th>
                                        <th style="text-align: right;">Amount (LKR)</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr>
                                        <td id="rc-treatment-name">Dental Scaling & Cleaning</td>
                                        <td style="text-align: right;" id="rc-treatment-cost">5,000.00</td>
                                    </tr>
                                    <tr>
                                        <td>Doctor Consultation Fee</td>
                                        <td style="text-align: right;" id="rc-consultation-fee">2,500.00</td>
                                    </tr>
                                </tbody>
                            </table>

                            <div class="receipt-total-row">
                                <span>NET TOTAL:</span>
                                <span id="rc-total">7,500.00 LKR</span>
                            </div>

                            <div style="text-align: center; margin-top: 2rem; font-size: 0.8rem; color: #64748b;">
                                Thank you for visiting Sunrise Dental Clinic!<br>
                                Stay Healthy, Smile Bright.
                            </div>
                        </div>

                        <div style="margin-top: 1.5rem; display: flex; justify-content: flex-end; gap: 1rem;">
                            <button type="button" class="btn btn-secondary" onclick="document.getElementById('receipt-display').style.display='none'">Dismiss</button>
                            <button type="button" class="btn btn-primary" onclick="window.print()">
                                <span>🖨️</span> Print Invoice Receipt
                            </button>
                        </div>
                    </div>
                </div>
            </section>

            <!-- TAB 4: CLINIC REPORTS -->
            <% if ("Admin".equals(role)) { %>
            <section id="tab-reports" class="tab-content">
                <div class="panel">
                    <div class="panel-header">
                        <h2 class="panel-title">Clinic Performance & Reports</h2>
                        <p class="panel-subtitle">Interactive summaries and statistics of clinical operations</p>
                    </div>

                    <div class="stats-grid">
                        <div class="stats-card">
                            <div class="stats-title">Total Appointments</div>
                            <div id="stats-total-appointments" class="stats-num">0</div>
                            <div style="font-size: 0.8rem; color: var(--text-muted);">Registered in system</div>
                        </div>
                        <div class="stats-card">
                            <div class="stats-title">Total Revenue (LKR)</div>
                            <div id="stats-total-revenue" class="stats-num">0.00</div>
                            <div style="font-size: 0.8rem; color: var(--text-muted);">Treatments + Consultations</div>
                        </div>
                        <div class="stats-card">
                            <div class="stats-title">Consultation Fees</div>
                            <div id="stats-consultation-revenue" class="stats-num">0.00</div>
                            <div style="font-size: 0.8rem; color: var(--text-muted);">Clinic service charge</div>
                        </div>
                    </div>

                    <div style="display: grid; grid-template-columns: 1fr; gap: 2rem;">
                        <div>
                            <h3 style="margin-bottom: 1rem; color: var(--color-primary);">Revenue per Treatment Type</h3>
                            <div style="overflow-x: auto; background: rgba(0, 0, 0, 0.2); border-radius: 12px; border: 1px solid var(--card-border);">
                                <table class="report-table">
                                    <thead>
                                        <tr>
                                            <th>Treatment Name</th>
                                            <th>Total Sessions</th>
                                            <th style="text-align: right;">Total Earnings (LKR)</th>
                                        </tr>
                                    </thead>
                                    <tbody id="report-treatments-tbody">
                                        <tr>
                                            <td colspan="3" style="text-align: center; color: var(--text-muted);">No records found</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <div>
                            <h3 style="margin-bottom: 1rem; color: var(--color-primary);">Dentist Load Report</h3>
                            <div style="overflow-x: auto; background: rgba(0, 0, 0, 0.2); border-radius: 12px; border: 1px solid var(--card-border);">
                                <table class="report-table">
                                    <thead>
                                        <tr>
                                            <th>Dentist Name</th>
                                            <th>Appointments Booked</th>
                                        </tr>
                                    </thead>
                                    <tbody id="report-dentists-tbody">
                                        <tr>
                                            <td colspan="2" style="text-align: center; color: var(--text-muted);">No records found</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <!-- TAB: USER MANAGEMENT (ADMIN ONLY) -->
            <section id="tab-users" class="tab-content">
                <div class="panel">
                    <div class="panel-header">
                        <h2 class="panel-title">User Account Management</h2>
                        <p class="panel-subtitle">Create, update, or remove Admin and Staff user credentials</p>
                    </div>

                    <div id="user-alert" class="alert" style="display: none;"></div>

                    <form id="user-form" onsubmit="submitUserForm(event)">
                        <input type="hidden" id="user-id" value="0">
                        <div class="form-grid">
                            <div class="form-group">
                                <label for="user-username">Username</label>
                                <input type="text" id="user-username" placeholder="e.g. john_staff" required minlength="3">
                            </div>

                            <div class="form-group">
                                <label for="user-fullname">Full Name</label>
                                <input type="text" id="user-fullname" placeholder="e.g. Johnathan Doe" required>
                            </div>

                            <div class="form-group">
                                <label for="user-role">System Role</label>
                                <select id="user-role" required>
                                    <option value="Staff" selected>Staff Member</option>
                                    <option value="Admin">Administrator</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="user-password">Password <span id="user-password-hint" style="font-weight: normal; font-size: 0.8rem; color: var(--text-muted);">(Required for new users)</span></label>
                                <input type="password" id="user-password" placeholder="••••••••">
                            </div>

                            <div class="form-group">
                                <label for="user-confirm-password">Confirm Password</label>
                                <input type="password" id="user-confirm-password" placeholder="••••••••">
                            </div>
                        </div>

                        <div class="form-actions" style="margin-top: 1.5rem;">
                            <button type="button" class="btn btn-secondary" onclick="resetUserForm()">Clear Form</button>
                            <button type="submit" id="btn-save-user" class="btn btn-primary">Save User Account</button>
                        </div>
                    </form>

                    <div style="margin-top: 2.5rem;">
                        <h3 style="margin-bottom: 1rem; color: var(--color-primary);">System Users List</h3>
                        <div style="overflow-x: auto; background: rgba(0, 0, 0, 0.2); border-radius: 12px; border: 1px solid var(--card-border);">
                            <table class="report-table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Username</th>
                                        <th>Full Name</th>
                                        <th>Role</th>
                                        <th style="text-align: right;">Actions</th>
                                    </tr>
                                </thead>
                                <tbody id="users-table-tbody">
                                    <tr>
                                        <td colspan="5" style="text-align: center; color: var(--text-muted);">Loading users...</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </section>

            <!-- TAB: DENTIST MANAGEMENT (ADMIN ONLY) -->
            <section id="tab-dentists" class="tab-content">
                <div class="panel">
                    <div class="panel-header">
                        <h2 class="panel-title">Dentist Profile Management</h2>
                        <p class="panel-subtitle">Register, update, or remove consulting dentists and specializations</p>
                    </div>

                    <div id="dentist-alert" class="alert" style="display: none;"></div>

                    <form id="dentist-form" onsubmit="submitDentistForm(event)">
                        <input type="hidden" id="dentist-id" value="0">
                        <div class="form-grid">
                            <div class="form-group">
                                <label for="dentist-name-input">Dentist Name</label>
                                <input type="text" id="dentist-name-input" placeholder="e.g. Dr. Kasun Perera" required minlength="3">
                            </div>

                            <div class="form-group">
                                <label for="dentist-specialization">Specialization</label>
                                <select id="dentist-specialization" required>
                                    <option value="" disabled selected>Select Specialization</option>
                                    <option value="General Dentist">General Dentist</option>
                                    <option value="Orthodontist">Orthodontist</option>
                                    <option value="Pediatric Dentist">Pediatric Dentist (Pedodontist)</option>
                                    <option value="Periodontist">Periodontist</option>
                                    <option value="Endodontist">Endodontist</option>
                                    <option value="Oral & Maxillofacial Surgeon">Oral & Maxillofacial Surgeon</option>
                                    <option value="Prosthodontist">Prosthodontist</option>
                                    <option value="Cosmetic Dentist">Cosmetic Dentist</option>
                                </select>
                            </div>

                            <div class="form-group full-width">
                                <label for="dentist-contact">Contact Phone Number</label>
                                <input type="tel" id="dentist-contact" placeholder="e.g. 0771234567">
                            </div>
                        </div>

                        <div class="form-actions" style="margin-top: 1.5rem;">
                            <button type="button" class="btn btn-secondary" onclick="resetDentistForm()">Clear Form</button>
                            <button type="submit" id="btn-save-dentist" class="btn btn-primary">Save Dentist Profile</button>
                        </div>
                    </form>

                    <div style="margin-top: 2.5rem;">
                        <h3 style="margin-bottom: 1rem; color: var(--color-primary);">Registered Dentists List</h3>
                        <div style="overflow-x: auto; background: rgba(0, 0, 0, 0.2); border-radius: 12px; border: 1px solid var(--card-border);">
                            <table class="report-table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Dentist Name</th>
                                        <th>Specialization</th>
                                        <th>Contact Number</th>
                                        <th style="text-align: right;">Actions</th>
                                    </tr>
                                </thead>
                                <tbody id="dentists-table-tbody">
                                    <tr>
                                        <td colspan="5" style="text-align: center; color: var(--text-muted);">Loading dentists...</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </section>

            <!-- TAB: TREATMENT SERVICE MANAGEMENT (ADMIN ONLY) -->
            <section id="tab-treatments" class="tab-content">
                <div class="panel">
                    <div class="panel-header">
                        <h2 class="panel-title">Treatment Service Packages & Pricing</h2>
                        <p class="panel-subtitle">Create, update service rates, or remove clinical treatment packages</p>
                    </div>

                    <div id="treatment-alert" class="alert" style="display: none;"></div>

                    <form id="treatment-form" onsubmit="submitTreatmentForm(event)">
                        <input type="hidden" id="treatment-id-input" value="0">
                        <div class="form-grid">
                            <div class="form-group">
                                <label for="treatment-name-input">Treatment Service Name</label>
                                <select id="treatment-name-input" required>
                                    <option value="" disabled selected>Select Treatment Service</option>
                                    <option value="Dental Cleaning & Scaling">Dental Cleaning & Scaling</option>
                                    <option value="Composite Filling">Composite Filling</option>
                                    <option value="Tooth Extraction">Tooth Extraction</option>
                                    <option value="Root Canal Therapy">Root Canal Therapy</option>
                                    <option value="Teeth Whitening">Teeth Whitening</option>
                                    <option value="Braces Consultation">Braces Consultation</option>
                                    <option value="Dental Crown & Bridge">Dental Crown & Bridge</option>
                                    <option value="Dental Implant Consultation">Dental Implant Consultation</option>
                                    <option value="Fluoride Treatment">Fluoride Treatment</option>
                                    <option value="Wisdom Tooth Surgery">Wisdom Tooth Surgery</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label for="treatment-cost-input">Service Price / Cost (LKR)</label>
                                <input type="number" step="0.01" id="treatment-cost-input" placeholder="e.g. 5000.00" required min="0">
                            </div>
                        </div>

                        <div class="form-actions" style="margin-top: 1.5rem;">
                            <button type="button" class="btn btn-secondary" onclick="resetTreatmentForm()">Clear Form</button>
                            <button type="submit" id="btn-save-treatment" class="btn btn-primary">Save Treatment Service</button>
                        </div>
                    </form>

                    <div style="margin-top: 2.5rem;">
                        <h3 style="margin-bottom: 1rem; color: var(--color-primary);">Clinic Service Rates List</h3>
                        <div style="overflow-x: auto; background: rgba(0, 0, 0, 0.2); border-radius: 12px; border: 1px solid var(--card-border);">
                            <table class="report-table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Treatment Service Name</th>
                                        <th style="text-align: right;">Service Cost (LKR)</th>
                                        <th style="text-align: right;">Actions</th>
                                    </tr>
                                </thead>
                                <tbody id="treatments-mgmt-table-tbody">
                                    <tr>
                                        <td colspan="4" style="text-align: center; color: var(--text-muted);">Loading treatments...</td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </section>
            <% } %>

            <!-- TAB 5: HELP GUIDE -->
            <section id="tab-help" class="tab-content">
                <div class="panel">
                    <div class="panel-header">
                        <h2 class="panel-title">System Help Section</h2>
                        <p class="panel-subtitle">Interactive instructions and guide for Sunrise Dental Clinic staff</p>
                    </div>

                    <div class="help-faq">
                        <div class="faq-item">
                            <div class="faq-question" onclick="toggleFaq(this)">
                                <span>1. How do I register a new patient appointment?</span>
                                <span>▼</span>
                            </div>
                            <div class="faq-answer">
                                <p>To schedule an appointment:</p>
                                <ol style="margin-left: 1.5rem; margin-top: 0.5rem;">
                                    <li>Navigate to the <strong>New Appointment</strong> tab in the sidebar.</li>
                                    <li>Enter the patient's full name, telephone contact number, and optional address.</li>
                                    <li>Select the specific Consulting Dentist and the required Treatment Service package.</li>
                                    <li>Set the correct Appointment Date and Time (must be in the future).</li>
                                    <li>Click <strong>Register Appointment</strong>. The system will automatically generate a unique appointment number starting with <code>APT-</code>.</li>
                                </ol>
                            </div>
                        </div>

                        <div class="faq-item">
                            <div class="faq-question" onclick="toggleFaq(this)">
                                <span>2. How do I search for patient details?</span>
                                <span>▼</span>
                            </div>
                            <div class="faq-answer">
                                <p>Navigate to the <strong>Search Details</strong> tab in the sidebar. Enter the generated appointment code (e.g., <code>APT-76839</code>) in the search box and click <strong>Search</strong>. The application will query the database and present all demographic, scheduled, and billing records for that visit.</p>
                            </div>
                        </div>

                        <div class="faq-item">
                            <div class="faq-question" onclick="toggleFaq(this)">
                                <span>3. How do I calculate and print a bill?</span>
                                <span>▼</span>
                            </div>
                            <div class="faq-answer">
                                <p>To process payments:</p>
                                <ol style="margin-left: 1.5rem; margin-top: 0.5rem;">
                                    <li>Open the <strong>Calculate & Bill</strong> tab in the sidebar.</li>
                                    <li>Provide the active appointment code.</li>
                                    <li>Enter the specific Consultation Fee charged by the clinic (e.g., 2500 LKR).</li>
                                    <li>Click <strong>Calculate & Generate Invoice</strong>. The database will combine the treatment package cost with the consultation fee via a stored procedure.</li>
                                    <li>Click <strong>Print Invoice Receipt</strong>. This triggers a clean layout printable page matching invoice guidelines.</li>
                                </ol>
                            </div>
                        </div>

                        <div class="faq-item">
                            <div class="faq-question" onclick="toggleFaq(this)">
                                <span>4. Where are reports found and what do they show?</span>
                                <span>▼</span>
                            </div>
                            <div class="faq-answer">
                                <p>Select the <strong>Clinic Reports</strong> tab. The system aggregates real-time metrics including total revenue generated (sum of treatment base costs + consultations), total registered patients, individual dentist bookings workload, and individual revenue totals grouped by specific dental treatment packages.</p>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

        </main>
    </div>

    <!-- Inject JS logic file -->
    <script src="js/app.js"></script>
    <script>
        const currentUserName = "<%= fullName %>";
        const currentUserRole = "<%= role %>";
        console.log(`Initialized MVC session: ${currentUserName} (${currentUserRole})`);
    </script>
</body>
</html>
