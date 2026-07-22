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

    private void ensureTableExists() {
        String createSql = "CREATE TABLE IF NOT EXISTS dentists (" +
                           "id INT AUTO_INCREMENT PRIMARY KEY, " +
                           "dentist_name VARCHAR(100) NOT NULL, " +
                           "specialization VARCHAR(100) NOT NULL, " +
                           "contact_number VARCHAR(20))";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(createSql)) {
            ps.execute();

            // Seed default dentists if table is empty
            String countSql = "SELECT COUNT(*) FROM dentists";
            try (PreparedStatement countPs = conn.prepareStatement(countSql);
                 ResultSet rs = countPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    String seedSql = "INSERT INTO dentists (dentist_name, specialization, contact_number) VALUES " +
                                     "('Dr. Samantha Perera', 'Orthodontist', '0771112233'), " +
                                     "('Dr. Nishantha Silva', 'General Dentist', '0772223344'), " +
                                     "('Dr. Priyanga Alwis', 'Pediatric Dentist', '0773334455'), " +
                                     "('Dr. Shalini Fernando', 'Periodontist', '0774445566')";
                    try (PreparedStatement seedPs = conn.prepareStatement(seedSql)) {
                        seedPs.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DentistDAOImpl() {
        ensureTableExists();
    }

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
