package com.sunrisedental.dao;

import com.sunrisedental.model.Patient;
import java.util.List;

public interface PatientDAO {
    List<Patient> getAllPatients();
    Patient getPatientById(int id);
    Patient getPatientByNic(String nicPassport);
    boolean createPatient(Patient patient);
    boolean updatePatient(Patient patient);
    boolean deletePatient(int id);
}
