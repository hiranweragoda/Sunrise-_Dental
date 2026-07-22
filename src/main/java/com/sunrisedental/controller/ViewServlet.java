package com.sunrisedental.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/login", "/dashboard"})
public class ViewServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        HttpSession session = req.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        if ("/login".equals(path)) {
            if (isLoggedIn) {
                resp.sendRedirect(req.getContextPath() + "/dashboard");
            } else {
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
        } else if ("/dashboard".equals(path)) {
            if (!isLoggedIn) {
                resp.sendRedirect(req.getContextPath() + "/login");
            } else {
                req.getRequestDispatcher("/dashboard.jsp").forward(req, resp);
            }
        }
    }
}
