package com.sunrisedental.dao.impl;

import com.sunrisedental.dao.DentistDAO;
import com.sunrisedental.model.Dentist;
import com.sunrisedental.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DentistDAOImpl implements DentistDAO {

    @Override
    public List<Dentist> getAllDentists() {
        List<Dentist> list = new ArrayList<>();
        String sql = "SELECT id, dentist_name, specialization, contact_number FROM dentists ORDER BY id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Dentist(
                    rs.getInt("id"),
                    rs.getString("dentist_name"),
                    rs.getString("specialization"),
                    rs.getString("contact_number")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Dentist getDentistById(int id) {
        String sql = "SELECT id, dentist_name, specialization, contact_number FROM dentists WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Dentist(
                        rs.getInt("id"),
                        rs.getString("dentist_name"),
                        rs.getString("specialization"),
                        rs.getString("contact_number")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean createDentist(Dentist dentist) {
        String sql = "INSERT INTO dentists (dentist_name, specialization, contact_number) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dentist.getDentistName());
            ps.setString(2, dentist.getSpecialization());
            ps.setString(3, dentist.getContactNumber());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateDentist(Dentist dentist) {
        String sql = "UPDATE dentists SET dentist_name = ?, specialization = ?, contact_number = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dentist.getDentistName());
            ps.setString(2, dentist.getSpecialization());
            ps.setString(3, dentist.getContactNumber());
            ps.setInt(4, dentist.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteDentist(int id) {
        String sql = "DELETE FROM dentists WHERE id = ?";
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
