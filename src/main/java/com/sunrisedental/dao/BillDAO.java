package com.sunrisedental.dao;

import com.sunrisedental.model.Bill;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BillDAO {
    Bill generateBill(String appointmentNumber, BigDecimal consultationFee);
    Bill getBillByAppointmentNumber(String appointmentNumber);
    Map<String, Object> getFinancialSummary();
    List<Map<String, Object>> getTreatmentRevenueReport();
}
