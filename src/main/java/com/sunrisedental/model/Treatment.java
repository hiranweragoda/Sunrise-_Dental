package com.sunrisedental.model;

import java.math.BigDecimal;

public class Treatment {
    private int id;
    private String treatmentName;
    private BigDecimal cost;

    public Treatment() {}

    public Treatment(int id, String treatmentName, BigDecimal cost) {
        this.id = id;
        this.treatmentName = treatmentName;
        this.cost = cost;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTreatmentName() { return treatmentName; }
    public void setTreatmentName(String treatmentName) { this.treatmentName = treatmentName; }

    public BigDecimal getCost() { return cost; }
    public void setCost(BigDecimal cost) { this.cost = cost; }
}
