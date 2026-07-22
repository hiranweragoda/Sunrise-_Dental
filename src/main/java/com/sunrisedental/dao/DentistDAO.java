package com.sunrisedental.dao;

import com.sunrisedental.model.Dentist;
import java.util.List;

public interface DentistDAO {
    List<Dentist> getAllDentists();
    Dentist getDentistById(int id);
    boolean createDentist(Dentist dentist);
    boolean updateDentist(Dentist dentist);
    boolean deleteDentist(int id);
}
