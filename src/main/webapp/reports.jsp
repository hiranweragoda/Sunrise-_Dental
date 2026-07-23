<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sunrisedental.model.User" %>
<%@ page import="com.sunrisedental.dao.*" %>
<%@ page import="com.sunrisedental.dao.impl.*" %>
<%@ page import="java.util.*" %>
<%
    User sessionUser = (User) session.getAttribute("user");
    if (sessionUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    String role = sessionUser.getRole();

    AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
    BillDAO billDAO = new BillDAOImpl();

    List<Map<String, Object>> dentistStats = appointmentDAO.getDentistStatistics();
    Map<String, Object> financialSummary = billDAO.getFinancialSummary();
    List<Map<String, Object>> treatmentReport = billDAO.getTreatmentRevenueReport();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Analytics & Reports - Sunrise Dental Management</title>
    <link rel="stylesheet" href="css/style.css?v=7">
</head>
<body class="dashboard-body">

    <!-- Top Navigation Bar -->
    <header class="top-nav">
        <div class="brand">
            <span class="logo-icon">🦷</span>
            <span class="brand-title">Sunrise Dental (Traditional MVC)</span>
        </div>
        <div class="user-profile">
            <div class="user-avatar"><%= sessionUser.getFullName().substring(0, 1).toUpperCase() %></div>
            <div class="user-info">
                <span class="user-name"><%= sessionUser.getFullName() %></span>
                <span class="user-role"><%= role %></span>
            </div>
        </div>
    </header>

    <div class="main-wrapper">
        <!-- Sidebar Navigation -->
        <aside class="sidebar">
            <a href="dashboard?tab=tab-register" class="nav-item">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z" />
                </svg>
                <span>New Appointment</span>
            </a>
            <a href="dashboard?tab=tab-patients" class="nav-item">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                </svg>
                <span>Patient Registration</span>
            </a>
            <a href="dashboard?tab=tab-search" class="nav-item">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                </svg>
                <span>Search Details</span>
            </a>
            <a href="dashboard?tab=tab-billing" class="nav-item">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M9 7h6m0 10v-3m-3 3h.01M9 17h.01M9 14h.01M12 14h.01M15 11h.01M12 11h.01M7 21h10a2 2 0 002-2V5a2 2 0 00-2-2H7a2 2 0 00-2 2v14a2 2 0 002 2z" />
                </svg>
                <span>Calculate & Bill</span>
            </a>
            <% if ("Admin".equals(role)) { %>
            <a href="dashboard?tab=tab-users" class="nav-item">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
                </svg>
                <span>User Management</span>
            </a>
            <a href="dashboard?tab=tab-dentists" class="nav-item">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                </svg>
                <span>Dentist Management</span>
            </a>
            <a href="dashboard?tab=tab-treatments" class="nav-item">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M19.428 15.428a2 2 0 00-1.022-.547l-2.387-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L5.605 15.13a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 10.172V5L8 4z" />
                </svg>
                <span>Treatment Packages</span>
            </a>
            <a href="reports" class="nav-item active">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
                </svg>
                <span>Analytics & Reports</span>
            </a>
            <% } %>

            <a href="dashboard?tab=tab-help" class="nav-item">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M8.228 9c.549-1.165 2.03-2 3.772-2 2.21 0 4 1.343 4 3 0 1.4-1.278 2.575-3.006 2.907-.542.104-.994.54-.994 1.093m0 3h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                </svg>
                <span>Help Section</span>
            </a>

            <a href="logout" class="nav-item logout">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
                    <path stroke-linecap="round" stroke-linejoin="round" d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                </svg>
                <span>Exit System</span>
            </a>
        </aside>

        <!-- Main Content Area -->
        <main class="content-area">
            <div style="margin-bottom: 2rem;">
                <h1 style="color: var(--color-primary); margin-bottom: 0.5rem; font-size: 1.8rem;">Clinic Analytics & Financial Reports</h1>
                <p style="color: var(--text-muted); font-size: 0.95rem;">Overview of clinic performance, appointment distribution, and revenue generation.</p>
            </div>

            <!-- Summary Cards -->
            <div style="display: grid; grid-template-columns: repeat(auto-fit, minmax(240px, 1fr)); gap: 1.5rem; margin-bottom: 2.5rem;">
                <div class="panel" style="text-align: center;">
                    <h3 style="color: var(--text-muted); font-size: 0.9rem; margin-bottom: 0.5rem;">Total Invoices Issued</h3>
                    <span style="font-size: 2.2rem; font-weight: 700; color: #38bdf8;"><%= financialSummary.getOrDefault("total_bills", 0) %></span>
                </div>
                <div class="panel" style="text-align: center;">
                    <h3 style="color: var(--text-muted); font-size: 0.9rem; margin-bottom: 0.5rem;">Total Revenue Collected</h3>
                    <span style="font-size: 2.2rem; font-weight: 700; color: #4ade80;">LKR <%= String.format("%,.2f", financialSummary.getOrDefault("total_revenue", 0.0)) %></span>
                </div>
            </div>

            <!-- Dentist Appointment Distribution -->
            <div class="panel" style="margin-bottom: 2rem;">
                <h2 class="panel-title" style="margin-bottom: 1rem;">Dentist Appointment Volume</h2>
                <div style="overflow-x: auto;">
                    <table class="report-table">
                        <thead>
                            <tr>
                                <th>Dentist Name</th>
                                <th style="text-align: right;">Total Appointments</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (dentistStats != null && !dentistStats.isEmpty()) { for (Map<String, Object> stat : dentistStats) { %>
                                <tr>
                                    <td style="font-weight: 600;"><%= stat.get("dentist_name") %></td>
                                    <td style="text-align: right;"><%= stat.get("appointment_count") %></td>
                                </tr>
                            <% } } else { %>
                                <tr><td colspan="2" style="text-align: center; color: var(--text-muted);">No dentist statistics available.</td></tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Treatment Revenue Breakdown -->
            <div class="panel">
                <h2 class="panel-title" style="margin-bottom: 1rem;">Treatment Revenue Breakdown</h2>
                <div style="overflow-x: auto;">
                    <table class="report-table">
                        <thead>
                            <tr>
                                <th>Treatment Service</th>
                                <th style="text-align: center;">Times Performed</th>
                                <th style="text-align: right;">Total Generated (LKR)</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% if (treatmentReport != null && !treatmentReport.isEmpty()) { for (Map<String, Object> tr : treatmentReport) { %>
                                <tr>
                                    <td style="font-weight: 600;"><%= tr.get("treatment_name") %></td>
                                    <td style="text-align: center;"><%= tr.get("appointment_count") %></td>
                                    <td style="text-align: right;">LKR <%= String.format("%,.2f", tr.get("total_earnings")) %></td>
                                </tr>
                            <% } } else { %>
                                <tr><td colspan="3" style="text-align: center; color: var(--text-muted);">No treatment revenue records available.</td></tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>

</body>
</html>
