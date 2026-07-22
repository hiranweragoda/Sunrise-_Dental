package com.sunrisedental.controller;

import com.google.gson.Gson;
import com.sunrisedental.dao.BillDAO;
import com.sunrisedental.model.Bill;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@WebServlet("/api/bills/*")
public class BillServlet extends HttpServlet {
    private final BillDAO billDAO = new com.sunrisedental.dao.impl.BillDAOImpl();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();

        if ("/summary".equals(pathInfo)) {
            Map<String, Object> summary = billDAO.getFinancialSummary();
            resp.getWriter().write(gson.toJson(summary));
            return;
        }

        if ("/treatments".equals(pathInfo)) {
            List<Map<String, Object>> report = billDAO.getTreatmentRevenueReport();
            resp.getWriter().write(gson.toJson(report));
            return;
        }

        String appointmentNumber = req.getParameter("appointment_number");
        if (appointmentNumber != null && !appointmentNumber.trim().isEmpty()) {
            Bill bill = billDAO.getBillByAppointmentNumber(appointmentNumber.trim());
            if (bill != null) {
                resp.getWriter().write(gson.toJson(bill));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\": \"Bill not found for this appointment number\"}");
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Missing appointment_number parameter\"}");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String appointmentNumber = req.getParameter("appointment_number");
            String feeStr = req.getParameter("consultation_fee");

            if (appointmentNumber == null || feeStr == null) {
                BillRequest requestBill = gson.fromJson(req.getReader(), BillRequest.class);
                if (requestBill != null) {
                    appointmentNumber = requestBill.appointment_number;
                    feeStr = requestBill.consultation_fee;
                }
            }

            if (appointmentNumber == null || appointmentNumber.trim().isEmpty() || feeStr == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Missing appointment_number or consultation_fee\"}");
                return;
            }

            BigDecimal consultationFee = new BigDecimal(feeStr.trim());
            if (consultationFee.compareTo(BigDecimal.ZERO) < 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Consultation fee cannot be negative\"}");
                return;
            }

            Bill bill = billDAO.generateBill(appointmentNumber.trim(), consultationFee);
            if (bill != null) {
                resp.getWriter().write(gson.toJson(bill));
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"error\": \"Failed to calculate and generate bill. Ensure the appointment number is correct.\"}");
            }

        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid consultation fee format\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"An error occurred: " + e.getMessage() + "\"}");
        }
    }

    private static class BillRequest {
        String appointment_number;
        String consultation_fee;
    }
}
