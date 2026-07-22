package com.sunrisedental.controller;

import com.google.gson.Gson;
import com.sunrisedental.dao.PatientDAO;
import com.sunrisedental.dao.impl.PatientDAOImpl;
import com.sunrisedental.model.Patient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/api/patients", "/api/patients/*"})
public class PatientServlet extends HttpServlet {
    private final PatientDAO patientDAO = new PatientDAOImpl();
    private final Gson gson = new Gson();

    private boolean isAuthenticated(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (session != null && session.getAttribute("user") != null);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (!isAuthenticated(req)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Unauthorized. Please log in.\"}");
            return;
        }

        List<Patient> list = patientDAO.getAllPatients();
        resp.getWriter().write(gson.toJson(list));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (!isAuthenticated(req)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\": \"Unauthorized. Please log in.\"}");
            return;
        }

        try {
            String idStr = req.getParameter("id");
            String patientName = req.getParameter("patient_name");
            String nicPassport = req.getParameter("nic_passport");
            String address = req.getParameter("address");
            String phoneNumber = req.getParameter("phone_number");
            String action = req.getParameter("action");

            if (patientName == null || nicPassport == null) {
                try {
                    PatientRequest body = gson.fromJson(req.getReader(), PatientRequest.class);
                    if (body != null) {
                        if (body.id != null) idStr = body.id;
                        if (body.patient_name != null) patientName = body.patient_name;
                        if (body.patientName != null) patientName = body.patientName;
                        if (body.nic_passport != null) nicPassport = body.nic_passport;
                        if (body.nicPassport != null) nicPassport = body.nicPassport;
                        if (body.address != null) address = body.address;
                        if (body.phone_number != null) phoneNumber = body.phone_number;
                        if (body.phoneNumber != null) phoneNumber = body.phoneNumber;
                        if (body.action != null) action = body.action;
                    }
                } catch (Exception ignored) {}
            }

            if ("delete".equalsIgnoreCase(action) || "DELETE".equals(req.getMethod())) {
                if (idStr != null && !idStr.trim().isEmpty()) {
                    int id = Integer.parseInt(idStr.trim());
                    boolean deleted = patientDAO.deletePatient(id);
                    if (deleted) {
                        resp.getWriter().write("{\"message\": \"Patient record deleted successfully\"}");
                    } else {
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        resp.getWriter().write("{\"error\": \"Failed to delete patient record\"}");
                    }
                    return;
                }
            }

            if (patientName == null || patientName.trim().isEmpty() ||
                nicPassport == null || nicPassport.trim().isEmpty() ||
                phoneNumber == null || phoneNumber.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Patient Name, NIC/Passport, and Phone Number are required.\"}");
                return;
            }

            boolean isUpdate = (idStr != null && !idStr.trim().isEmpty() && !idStr.trim().equals("0"));

            Patient p = new Patient();
            p.setPatientName(patientName.trim());
            p.setNicPassport(nicPassport.trim());
            p.setAddress(address != null ? address.trim() : "");
            p.setPhoneNumber(phoneNumber.trim());

            boolean success;
            if (isUpdate) {
                p.setId(Integer.parseInt(idStr.trim()));
                success = patientDAO.updatePatient(p);
            } else {
                success = patientDAO.createPatient(p);
            }

            if (success) {
                resp.getWriter().write("{\"message\": \"" + (isUpdate ? "Patient record updated successfully" : "Patient registered successfully") + "\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"error\": \"" + (isUpdate ? "Failed to update patient record" : "Failed to register patient (NIC/Passport may already exist)") + "\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"An error occurred: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    private static class PatientRequest {
        String id;
        String patient_name;
        String patientName;
        String nic_passport;
        String nicPassport;
        String address;
        String phone_number;
        String phoneNumber;
        String action;
    }
}
