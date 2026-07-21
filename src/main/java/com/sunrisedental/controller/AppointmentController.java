package com.sunrisedental.controller;

import com.google.gson.Gson;
import com.sunrisedental.dao.AppointmentDAO;
import com.sunrisedental.dto.AppointmentRequest;
import com.sunrisedental.model.Appointment;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Map;
import java.util.Random;

@WebServlet("/api/appointments/*")
public class AppointmentController extends HttpServlet {
    private final AppointmentDAO appointmentDAO = new com.sunrisedental.dao.impl.AppointmentDAOImpl();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();

        if ("/stats".equals(pathInfo)) {
            List<Map<String, Object>> stats = appointmentDAO.getDentistStatistics();
            resp.getWriter().write(gson.toJson(stats));
            return;
        }

        String appointmentNumber = req.getParameter("number");
        if (appointmentNumber != null && !appointmentNumber.trim().isEmpty()) {
            Appointment app = appointmentDAO.getAppointmentByNumber(appointmentNumber.trim());
            if (app != null) {
                resp.getWriter().write(gson.toJson(app));
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write("{\"error\": \"Appointment not found\"}");
            }
        } else {
            List<Appointment> list = appointmentDAO.getAllAppointments();
            resp.getWriter().write(gson.toJson(list));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String patientName = req.getParameter("patient_name");
            String address = req.getParameter("address");
            String contactNumber = req.getParameter("contact_number");
            String dentistName = req.getParameter("dentist_name");
            String treatmentIdStr = req.getParameter("treatment_id");
            String dateStr = req.getParameter("appointment_date");
            String timeStr = req.getParameter("appointment_time");

            if (patientName == null) {
                AppointmentRequest requestApp = gson.fromJson(req.getReader(), AppointmentRequest.class);
                if (requestApp != null) {
                    patientName = requestApp.patientName;
                    address = requestApp.address;
                    contactNumber = requestApp.contactNumber;
                    dentistName = requestApp.dentistName;
                    treatmentIdStr = requestApp.treatmentId;
                    dateStr = requestApp.appointmentDate;
                    timeStr = requestApp.appointmentTime;
                }
            }

            if (patientName == null || patientName.trim().isEmpty() ||
                contactNumber == null || contactNumber.trim().isEmpty() ||
                dentistName == null || dentistName.trim().isEmpty() ||
                treatmentIdStr == null || dateStr == null || timeStr == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Missing required fields\"}");
                return;
            }

            int treatmentId = Integer.parseInt(treatmentIdStr);
            Date appointmentDate = Date.valueOf(dateStr);
            
            if (timeStr.length() == 5) {
                timeStr += ":00";
            }
            Time appointmentTime = Time.valueOf(timeStr);

            String appointmentNumber = "APT-" + (10000 + new Random().nextInt(90000));

            Appointment app = new Appointment();
            app.setAppointmentNumber(appointmentNumber);
            app.setPatientName(patientName.trim());
            app.setAddress(address != null ? address.trim() : "");
            app.setContactNumber(contactNumber.trim());
            app.setDentistName(dentistName.trim());
            app.setTreatmentId(treatmentId);
            app.setAppointmentDate(appointmentDate);
            app.setAppointmentTime(appointmentTime);

            boolean success = appointmentDAO.createAppointment(app);
            if (success) {
                Appointment saved = appointmentDAO.getAppointmentByNumber(appointmentNumber);
                resp.getWriter().write(gson.toJson(saved));
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"error\": \"Failed to register appointment in database\"}");
            }

        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"error\": \"Invalid date or time format. Use YYYY-MM-DD and HH:MM\"}");
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"An unexpected error occurred: " + e.getMessage() + "\"}");
        }
    }
}
