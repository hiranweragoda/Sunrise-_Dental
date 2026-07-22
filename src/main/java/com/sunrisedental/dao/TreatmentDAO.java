package com.sunrisedental.dao;

import com.sunrisedental.model.Treatment;
import java.util.List;

public interface TreatmentDAO {
    List<Treatment> getAllTreatments();
    boolean createTreatment(Treatment treatment);
    boolean updateTreatment(Treatment treatment);
    boolean deleteTreatment(int id);
}
