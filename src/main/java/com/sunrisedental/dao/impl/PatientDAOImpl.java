package com.sunrisedental.dao.impl;

import com.sunrisedental.dao.PatientDAO;
import com.sunrisedental.model.Patient;
import com.sunrisedental.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PatientDAOImpl implements PatientDAO {

    @Override
    public List<Patient> getAllPatients() {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT id, patient_name, nic_passport, address, phone_number FROM patients ORDER BY id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Patient(
                    rs.getInt("id"),
                    rs.getString("patient_name"),
                    rs.getString("nic_passport"),
                    rs.getString("address"),
                    rs.getString("phone_number")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Patient getPatientById(int id) {
        String sql = "SELECT id, patient_name, nic_passport, address, phone_number FROM patients WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Patient(
                        rs.getInt("id"),
                        rs.getString("patient_name"),
                        rs.getString("nic_passport"),
                        rs.getString("address"),
                        rs.getString("phone_number")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Patient getPatientByNic(String nicPassport) {
        String sql = "SELECT id, patient_name, nic_passport, address, phone_number FROM patients WHERE nic_passport = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nicPassport);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Patient(
                        rs.getInt("id"),
                        rs.getString("patient_name"),
                        rs.getString("nic_passport"),
                        rs.getString("address"),
                        rs.getString("phone_number")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean createPatient(Patient patient) {
        String sql = "INSERT INTO patients (patient_name, nic_passport, address, phone_number) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, patient.getPatientName());
            ps.setString(2, patient.getNicPassport());
            ps.setString(3, patient.getAddress());
            ps.setString(4, patient.getPhoneNumber());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updatePatient(Patient patient) {
        String sql = "UPDATE patients SET patient_name = ?, nic_passport = ?, address = ?, phone_number = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, patient.getPatientName());
            ps.setString(2, patient.getNicPassport());
            ps.setString(3, patient.getAddress());
            ps.setString(4, patient.getPhoneNumber());
            ps.setInt(5, patient.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deletePatient(int id) {
        String sql = "DELETE FROM patients WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
