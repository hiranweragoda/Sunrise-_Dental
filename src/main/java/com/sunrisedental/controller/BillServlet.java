package com.sunrisedental.controller;

import com.sunrisedental.dao.BillDAO;
import com.sunrisedental.dao.impl.BillDAOImpl;
import com.sunrisedental.model.Bill;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/bills")
public class BillServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final BillDAO billDAO = new BillDAOImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        try {
            String appNum = req.getParameter("appointment_number");
            String feeStr = req.getParameter("consultation_fee");
            String paymentMethod = req.getParameter("payment_method");
            String cashGivenStr = req.getParameter("cash_given");
            String balanceStr = req.getParameter("balance_returned");

            if (appNum == null || appNum.trim().isEmpty() || feeStr == null || feeStr.trim().isEmpty()) {
                session.setAttribute("flashError", "Appointment Number and Consultation Fee are required.");
                resp.sendRedirect(req.getContextPath() + "/dashboard?tab=tab-billing");
                return;
            }

            BigDecimal consultationFee = new BigDecimal(feeStr.trim());
            BigDecimal cashGiven = null;
            BigDecimal balanceReturned = null;

            if ("Cash".equalsIgnoreCase(paymentMethod)) {
                if (cashGivenStr != null && !cashGivenStr.trim().isEmpty()) {
                    cashGiven = new BigDecimal(cashGivenStr.trim());
                }
                if (balanceStr != null && !balanceStr.trim().isEmpty()) {
                    balanceReturned = new BigDecimal(balanceStr.trim());
                }
            } else {
                paymentMethod = "Card";
            }

            Bill bill = billDAO.generateBill(appNum.trim(), consultationFee, paymentMethod, cashGiven, balanceReturned);

            if (bill != null) {
                req.setAttribute("bill", bill);
                req.getRequestDispatcher("/receipt.jsp").forward(req, resp);
                return;
            } else {
                session.setAttribute("flashError", "Failed to generate bill for appointment: " + appNum);
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("flashError", "Error generating bill: " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/dashboard?tab=tab-billing");
    }
}
