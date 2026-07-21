package com.sunrisedental.dao.impl;

import com.sunrisedental.dao.AppointmentDAO;
import com.sunrisedental.dao.DBConnection;
import com.sunrisedental.model.Appointment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppointmentDAOImpl implements AppointmentDAO {

    @Override
    public boolean createAppointment(Appointment app) {
        String sql = "INSERT INTO appointments (appointment_number, patient_name, address, contact_number, dentist_name, treatment_id, appointment_date, appointment_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, app.getAppointmentNumber());
            ps.setString(2, app.getPatientName());
            ps.setString(3, app.getAddress());
            ps.setString(4, app.getContactNumber());
            ps.setString(5, app.getDentistName());
            ps.setInt(6, app.getTreatmentId());
            ps.setDate(7, app.getAppointmentDate());
            ps.setTime(8, app.getAppointmentTime());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Appointment getAppointmentByNumber(String appointmentNumber) {
        String sql = "SELECT a.*, t.treatment_name, t.cost FROM appointments a " +
                     "JOIN treatments t ON a.treatment_id = t.id " +
                     "WHERE a.appointment_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, appointmentNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Appointment app = new Appointment();
                    app.setAppointmentNumber(rs.getString("appointment_number"));
                    app.setPatientName(rs.getString("patient_name"));
                    app.setAddress(rs.getString("address"));
                    app.setContactNumber(rs.getString("contact_number"));
                    app.setDentistName(rs.getString("dentist_name"));
                    app.setTreatmentId(rs.getInt("treatment_id"));
                    app.setAppointmentDate(rs.getDate("appointment_date"));
                    app.setAppointmentTime(rs.getTime("appointment_time"));
                    app.setStatus(rs.getString("status"));
                    app.setTreatmentName(rs.getString("treatment_name"));
                    app.setTreatmentCost(rs.getBigDecimal("cost"));
                    return app;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Appointment> getAllAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT a.*, t.treatment_name, t.cost FROM appointments a " +
                     "JOIN treatments t ON a.treatment_id = t.id " +
                     "ORDER BY a.appointment_date DESC, a.appointment_time DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Appointment app = new Appointment();
                app.setAppointmentNumber(rs.getString("appointment_number"));
                app.setPatientName(rs.getString("patient_name"));
                app.setAddress(rs.getString("address"));
                app.setContactNumber(rs.getString("contact_number"));
                app.setDentistName(rs.getString("dentist_name"));
                app.setTreatmentId(rs.getInt("treatment_id"));
                app.setAppointmentDate(rs.getDate("appointment_date"));
                app.setAppointmentTime(rs.getTime("appointment_time"));
                app.setStatus(rs.getString("status"));
                app.setTreatmentName(rs.getString("treatment_name"));
                app.setTreatmentCost(rs.getBigDecimal("cost"));
                list.add(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Map<String, Object>> getDentistStatistics() {
        List<Map<String, Object>> stats = new ArrayList<>();
        String sql = "SELECT dentist_name, COUNT(*) as appointment_count FROM appointments GROUP BY dentist_name";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("dentist_name", rs.getString("dentist_name"));
                map.put("appointment_count", rs.getInt("appointment_count"));
                stats.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stats;
    }
}
