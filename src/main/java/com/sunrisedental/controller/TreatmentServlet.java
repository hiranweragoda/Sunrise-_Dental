package com.sunrisedental.controller;

import com.google.gson.Gson;
import com.sunrisedental.dao.TreatmentDAO;
import com.sunrisedental.model.Treatment;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/api/treatments")
public class TreatmentServlet extends HttpServlet {
    private final TreatmentDAO treatmentDAO = new com.sunrisedental.dao.impl.TreatmentDAOImpl();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        List<Treatment> treatments = treatmentDAO.getAllTreatments();
        resp.getWriter().write(gson.toJson(treatments));
    }
}
