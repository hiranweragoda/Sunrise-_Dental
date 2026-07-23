<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.sunrisedental.model.Bill" %>
<%@ page import="com.sunrisedental.model.User" %>
<%
    User sessionUser = (User) session.getAttribute("user");
    if (sessionUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }

    Bill bill = (Bill) request.getAttribute("bill");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Payment Receipt - Sunrise Dental</title>
    <link rel="stylesheet" href="css/style.css">
    <style>
        .receipt-card {
            max-width: 600px;
            margin: 3rem auto;
            background: #1e293b;
            border-radius: 16px;
            padding: 2.5rem;
            box-shadow: 0 10px 25px rgba(0,0,0,0.5);
            border: 1px solid #334155;
            color: #f8fafc;
        }
        .receipt-header {
            text-align: center;
            border-bottom: 2px dashed #475569;
            padding-bottom: 1.5rem;
            margin-bottom: 1.5rem;
        }
        .receipt-row {
            display: flex;
            justify-content: space-between;
            margin-bottom: 0.75rem;
            font-size: 0.95rem;
        }
        .receipt-label { color: #94a3b8; }
        .receipt-val { font-weight: 600; }
        .receipt-total {
            border-top: 2px solid #475569;
            padding-top: 1rem;
            margin-top: 1.5rem;
            font-size: 1.25rem;
            color: #38bdf8;
        }
        @media print {
            body { background: white; color: black; }
            .receipt-card { background: white; color: black; border: 1px solid #000; box-shadow: none; }
            .no-print { display: none; }
            .receipt-label { color: #333; }
            .receipt-total { color: #000; }
        }
    </style>
</head>
<body style="background: #0f172a; min-height: 100vh; display: flex; flex-direction: column;">

    <div class="no-print" style="max-width: 600px; margin: 1.5rem auto 0 auto; display: flex; justify-content: space-between; width: 100%;">
        <a href="dashboard?tab=tab-billing" class="btn btn-secondary">← Back to Dashboard</a>
        <button onclick="window.print()" class="btn btn-primary">🖨️ Print Receipt</button>
    </div>

    <div class="receipt-card">
        <div class="receipt-header">
            <h1 style="margin: 0; font-size: 1.8rem; color: #38bdf8;">🦷 Sunrise Dental Clinic</h1>
            <p style="margin: 0.25rem 0 0 0; color: #94a3b8; font-size: 0.85rem;">Official Payment Receipt & Statement</p>
        </div>

        <% if (bill != null) { %>
            <div class="receipt-row">
                <span class="receipt-label">Invoice Number:</span>
                <span class="receipt-val"><%= bill.getBillNumber() %></span>
            </div>
            <div class="receipt-row">
                <span class="receipt-label">Appointment #:</span>
                <span class="receipt-val"><%= bill.getAppointmentNumber() %></span>
            </div>
            <div class="receipt-row">
                <span class="receipt-label">Patient Name:</span>
                <span class="receipt-val"><%= bill.getPatientName() %></span>
            </div>
            <div class="receipt-row">
                <span class="receipt-label">Dentist Name:</span>
                <span class="receipt-val"><%= bill.getDentistName() %></span>
            </div>
            <div class="receipt-row">
                <span class="receipt-label">Treatment Service:</span>
                <span class="receipt-val"><%= bill.getTreatmentName() %></span>
            </div>
            <div class="receipt-row">
                <span class="receipt-label">Date Issued:</span>
                <span class="receipt-val"><%= bill.getBillDate() %></span>
            </div>

            <div style="margin-top: 1.5rem; border-top: 1px solid #334155; padding-top: 1rem;">
                <div class="receipt-row">
                    <span class="receipt-label">Treatment Cost:</span>
                    <span class="receipt-val">LKR <%= String.format("%,.2f", bill.getTreatmentCost()) %></span>
                </div>
                <div class="receipt-row">
                    <span class="receipt-label">Consultation Fee:</span>
                    <span class="receipt-val">LKR <%= String.format("%,.2f", bill.getConsultationFee()) %></span>
                </div>
            </div>

            <div class="receipt-row receipt-total">
                <span>Total Amount Paid:</span>
                <span style="font-weight: 700;">LKR <%= String.format("%,.2f", bill.getTotalAmount()) %></span>
            </div>

            <div style="text-align: center; margin-top: 2rem; color: #94a3b8; font-size: 0.8rem;">
                <p>Thank you for visiting Sunrise Dental Clinic!</p>
                <p style="font-style: italic;">Issued by <%= sessionUser.getFullName() %> (<%= sessionUser.getRole() %>)</p>
            </div>
        <% } else { %>
            <div style="text-align: center; color: #f87171;">
                <p>No receipt data found.</p>
            </div>
        <% } %>
    </div>

</body>
</html>
