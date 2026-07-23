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
            max-width: 650px;
            margin: 2rem auto;
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
        .receipt-val { font-weight: 600; color: #ffffff; }
        .receipt-total {
            border-top: 2px solid #475569;
            padding-top: 1rem;
            margin-top: 1.5rem;
            font-size: 1.3rem;
            color: #38bdf8;
        }

        /* PRINT STYLES - Force all elements to solid black text on white paper */
        @media print {
            @page {
                size: portrait;
                margin: 10mm;
            }
            * {
                background: transparent !important;
                color: #000000 !important;
                -webkit-text-fill-color: #000000 !important;
                box-shadow: none !important;
                text-shadow: none !important;
                opacity: 1 !important;
                visibility: visible !important;
                filter: none !important;
            }
            html, body {
                background: #ffffff !important;
                color: #000000 !important;
                height: auto !important;
                min-height: 0 !important;
                max-height: none !important;
                display: block !important;
                overflow: visible !important;
                margin: 0 !important;
                padding: 0 !important;
                -webkit-print-color-adjust: exact !important;
                print-color-adjust: exact !important;
            }
            .no-print {
                display: none !important;
            }
            .receipt-card {
                display: block !important;
                background: #ffffff !important;
                border: 2px solid #000000 !important;
                margin: 0 auto !important;
                width: 100% !important;
                max-width: 100% !important;
                padding: 20px !important;
                border-radius: 0 !important;
                visibility: visible !important;
                opacity: 1 !important;
                height: auto !important;
                overflow: visible !important;
                page-break-inside: avoid !important;
                box-sizing: border-box !important;
            }
            .receipt-row {
                display: flex !important;
                justify-content: space-between !important;
                margin-bottom: 8px !important;
                font-size: 14px !important;
                border-bottom: 1px dotted #ccc !important;
                padding-bottom: 4px !important;
            }
            .receipt-header {
                border-bottom: 2px solid #000000 !important;
                text-align: center !important;
                padding-bottom: 12px !important;
                margin-bottom: 15px !important;
            }
            .receipt-header h1 {
                font-size: 24px !important;
                font-weight: bold !important;
            }
            .receipt-total {
                border-top: 2px solid #000000 !important;
                font-size: 18px !important;
                font-weight: bold !important;
                margin-top: 15px !important;
                padding-top: 10px !important;
            }
        }
    </style>
</head>
<body style="background: #0f172a; min-height: 100vh; display: flex; flex-direction: column;">

    <div class="no-print" style="max-width: 650px; margin: 1.5rem auto 0 auto; display: flex; justify-content: space-between; width: 100%;">
        <a href="dashboard?tab=tab-billing" class="btn btn-secondary" style="background: #334155; color: #fff; padding: 10px 20px; border-radius: 8px; text-decoration: none; font-weight: 600;">← Back to Billing Dashboard</a>
        <button onclick="window.print()" class="btn btn-primary" style="background: linear-gradient(135deg, #06b6d4, #3b82f6); color: #fff; border: none; padding: 10px 24px; border-radius: 8px; font-weight: 700; cursor: pointer; font-size: 1rem;">🖨️ Print Payment Receipt</button>
    </div>

    <div class="receipt-card">
        <div class="receipt-header">
            <h1 style="margin: 0; font-size: 1.8rem; color: #38bdf8;">🦷 Sunrise Dental Clinic</h1>
            <p style="margin: 0.25rem 0 0 0; color: #94a3b8; font-size: 0.85rem;">Official Payment Receipt & Statement</p>
        </div>

        <% if (bill != null) { 
            boolean isCash = "Cash".equalsIgnoreCase(bill.getPaymentMethod());
        %>
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
                <span class="receipt-label">Consulting Dentist:</span>
                <span class="receipt-val"><%= bill.getDentistName() %></span>
            </div>
            <div class="receipt-row">
                <span class="receipt-label">Treatment Service:</span>
                <span class="receipt-val"><%= bill.getTreatmentName() %></span>
            </div>
            <div class="receipt-row">
                <span class="receipt-label">Payment Method:</span>
                <span class="receipt-val" style="color: #38bdf8;"><%= isCash ? "💵 Cash Payment" : "💳 Credit / Debit Card" %></span>
            </div>
            <div class="receipt-row">
                <span class="receipt-label">Date & Time Issued:</span>
                <span class="receipt-val"><%= bill.getBillDate() %></span>
            </div>

            <div style="margin-top: 1.5rem; border-top: 1px dashed #475569; padding-top: 1rem;">
                <div class="receipt-row">
                    <span class="receipt-label">Treatment Package Price:</span>
                    <span class="receipt-val">LKR <%= String.format("%,.2f", bill.getTreatmentCost()) %></span>
                </div>
                <div class="receipt-row">
                    <span class="receipt-label">Doctor Consultation Fee:</span>
                    <span class="receipt-val">LKR <%= String.format("%,.2f", bill.getConsultationFee()) %></span>
                </div>
            </div>

            <div class="receipt-row receipt-total">
                <span>Total Amount Paid:</span>
                <span style="font-weight: 700;">LKR <%= String.format("%,.2f", bill.getTotalAmount()) %></span>
            </div>

            <% if (isCash && bill.getCashGiven() != null) { %>
                <div style="margin-top: 1rem; background: rgba(255,255,255,0.05); padding: 0.75rem 1rem; border-radius: 8px; border: 1px solid rgba(255,255,255,0.1);">
                    <div class="receipt-row" style="margin-bottom: 0.4rem;">
                        <span class="receipt-label">Cash Given by Patient:</span>
                        <span class="receipt-val">LKR <%= String.format("%,.2f", bill.getCashGiven()) %></span>
                    </div>
                    <div class="receipt-row" style="margin-bottom: 0;">
                        <span class="receipt-label">Balance Change Returned:</span>
                        <span class="receipt-val" style="color: #4ade80;">LKR <%= String.format("%,.2f", bill.getBalanceReturned() != null ? bill.getBalanceReturned() : java.math.BigDecimal.ZERO) %></span>
                    </div>
                </div>
            <% } %>

            <div style="text-align: center; margin-top: 2rem; border-top: 1px solid #334155; padding-top: 1rem; color: #94a3b8; font-size: 0.85rem;">
                <p style="margin: 0; font-weight: 600;">Thank you for visiting Sunrise Dental Clinic!</p>
                <p style="margin: 4px 0 0 0; font-size: 0.75rem; font-style: italic;">Issued by Staff: <%= sessionUser.getFullName() %> (<%= sessionUser.getRole() %>)</p>
            </div>
        <% } else { %>
            <div style="text-align: center; color: #f87171; padding: 2rem;">
                <p style="font-size: 1.1rem; font-weight: 600;">⚠️ No receipt data available to display.</p>
                <a href="dashboard?tab=tab-billing" style="color: #38bdf8; text-decoration: underline;">Return to Billing Dashboard</a>
            </div>
        <% } %>
    </div>

</body>
</html>
