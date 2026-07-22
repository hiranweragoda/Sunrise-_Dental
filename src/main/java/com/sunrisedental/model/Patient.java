package com.sunrisedental.model;

public class Patient {
    private int id;
    private String patientName;
    private String nicPassport;
    private String address;
    private String phoneNumber;

    public Patient() {}

    public Patient(int id, String patientName, String nicPassport, String address, String phoneNumber) {
        this.id = id;
        this.patientName = patientName;
        this.nicPassport = nicPassport;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getNicPassport() { return nicPassport; }
    public void setNicPassport(String nicPassport) { this.nicPassport = nicPassport; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
