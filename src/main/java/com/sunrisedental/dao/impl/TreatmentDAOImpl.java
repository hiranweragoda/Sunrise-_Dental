package com.sunrisedental.dao.impl;

import com.sunrisedental.util.DBConnection;
import com.sunrisedental.dao.TreatmentDAO;
import com.sunrisedental.model.Treatment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TreatmentDAOImpl implements TreatmentDAO {

    @Override
    public List<Treatment> getAllTreatments() {
        List<Treatment> list = new ArrayList<>();
        String sql = "SELECT id, treatment_name, cost FROM treatments ORDER BY treatment_name";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Treatment(
                    rs.getInt("id"),
                    rs.getString("treatment_name"),
                    rs.getBigDecimal("cost")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean createTreatment(Treatment treatment) {
        String sql = "INSERT INTO treatments (treatment_name, cost) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, treatment.getTreatmentName());
            ps.setBigDecimal(2, treatment.getCost());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateTreatment(Treatment treatment) {
        String sql = "UPDATE treatments SET treatment_name = ?, cost = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, treatment.getTreatmentName());
            ps.setBigDecimal(2, treatment.getCost());
            ps.setInt(3, treatment.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteTreatment(int id) {
        String sql = "DELETE FROM treatments WHERE id = ?";
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
