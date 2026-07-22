package com.sunrisedental.controller;

import com.google.gson.Gson;
import com.sunrisedental.dao.DentistDAO;
import com.sunrisedental.dao.impl.DentistDAOImpl;
import com.sunrisedental.model.Dentist;
import com.sunrisedental.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/api/dentists", "/api/dentists/*"})
public class DentistServlet extends HttpServlet {
    private final DentistDAO dentistDAO = new DentistDAOImpl();
    private final Gson gson = new Gson();

    private boolean isAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User sessionUser = (User) session.getAttribute("user");
            return "Admin".equalsIgnoreCase(sessionUser.getRole());
        }
        return false;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        List<Dentist> list = dentistDAO.getAllDentists();
        resp.getWriter().write(gson.toJson(list));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (!isAdmin(req)) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("{\"error\": \"Only administrators can manage dentist records.\"}");
            return;
        }

        try {
            String idStr = req.getParameter("id");
            String dentistName = req.getParameter("dentist_name");
            String specialization = req.getParameter("specialization");
            String contactNumber = req.getParameter("contact_number");
            String action = req.getParameter("action");

            if (dentistName == null || specialization == null) {
                try {
                    DentistRequest body = gson.fromJson(req.getReader(), DentistRequest.class);
                    if (body != null) {
                        if (body.id != null) idStr = body.id;
                        if (body.dentist_name != null) dentistName = body.dentist_name;
                        if (body.dentistName != null) dentistName = body.dentistName;
                        if (body.specialization != null) specialization = body.specialization;
                        if (body.contact_number != null) contactNumber = body.contact_number;
                        if (body.contactNumber != null) contactNumber = body.contactNumber;
                        if (body.action != null) action = body.action;
                    }
                } catch (Exception ignored) {}
            }

            if ("delete".equalsIgnoreCase(action) || "DELETE".equals(req.getMethod())) {
                if (idStr != null && !idStr.trim().isEmpty()) {
                    int id = Integer.parseInt(idStr.trim());
                    boolean deleted = dentistDAO.deleteDentist(id);
                    if (deleted) {
                        resp.getWriter().write("{\"message\": \"Dentist deleted successfully\"}");
                    } else {
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        resp.getWriter().write("{\"error\": \"Failed to delete dentist record\"}");
                    }
                    return;
                }
            }

            if (dentistName == null || dentistName.trim().isEmpty() ||
                specialization == null || specialization.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Dentist Name and Specialization are required.\"}");
                return;
            }

            boolean isUpdate = (idStr != null && !idStr.trim().isEmpty() && !idStr.trim().equals("0"));

            Dentist d = new Dentist();
            d.setDentistName(dentistName.trim());
            d.setSpecialization(specialization.trim());
            d.setContactNumber(contactNumber != null ? contactNumber.trim() : "");

            boolean success;
            if (isUpdate) {
                d.setId(Integer.parseInt(idStr.trim()));
                success = dentistDAO.updateDentist(d);
            } else {
                success = dentistDAO.createDentist(d);
            }

            if (success) {
                resp.getWriter().write("{\"message\": \"" + (isUpdate ? "Dentist updated successfully" : "Dentist registered successfully") + "\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"error\": \"" + (isUpdate ? "Failed to update dentist record" : "Failed to register dentist") + "\"}");
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

    private static class DentistRequest {
        String id;
        String dentist_name;
        String dentistName;
        String specialization;
        String contact_number;
        String contactNumber;
        String action;
    }
}
