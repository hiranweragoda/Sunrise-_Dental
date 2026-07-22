package com.sunrisedental.model;

public class Dentist {
    private int id;
    private String dentistName;
    private String specialization;
    private String contactNumber;

    public Dentist() {}

    public Dentist(int id, String dentistName, String specialization, String contactNumber) {
        this.id = id;
        this.dentistName = dentistName;
        this.specialization = specialization;
        this.contactNumber = contactNumber;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDentistName() { return dentistName; }
    public void setDentistName(String dentistName) { this.dentistName = dentistName; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }
}
