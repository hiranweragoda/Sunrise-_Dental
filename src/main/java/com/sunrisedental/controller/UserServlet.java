package com.sunrisedental.controller;

import com.google.gson.Gson;
import com.sunrisedental.dao.UserDAO;
import com.sunrisedental.dao.impl.UserDAOImpl;
import com.sunrisedental.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/api/users", "/api/users/*"})
public class UserServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAOImpl();
    private final Gson gson = new Gson();

    private boolean isAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User sessionUser = (User) session.getAttribute("user");
            return "Admin".equalsIgnoreCase(sessionUser.getRole());
        }
        return false;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (!isAdmin(req)) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("{\"error\": \"Only administrators can view user accounts.\"}");
            return;
        }

        List<User> users = userDAO.getAllUsers();
        resp.getWriter().write(gson.toJson(users));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (!isAdmin(req)) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.getWriter().write("{\"error\": \"Only administrators can manage user accounts.\"}");
            return;
        }

        try {
            String idStr = req.getParameter("id");
            String username = req.getParameter("username");
            String fullName = req.getParameter("full_name");
            String role = req.getParameter("role");
            String password = req.getParameter("password");
            String action = req.getParameter("action");

            if (username == null || fullName == null || role == null) {
                try {
                    UserFormRequest body = gson.fromJson(req.getReader(), UserFormRequest.class);
                    if (body != null) {
                        if (body.id != null) idStr = body.id;
                        if (body.username != null) username = body.username;
                        if (body.full_name != null) fullName = body.full_name;
                        if (body.fullName != null) fullName = body.fullName;
                        if (body.role != null) role = body.role;
                        if (body.password != null) password = body.password;
                        if (body.action != null) action = body.action;
                    }
                } catch (Exception ignored) {}
            }

            if ("delete".equalsIgnoreCase(action) || "DELETE".equals(req.getMethod())) {
                if (idStr != null && !idStr.trim().isEmpty()) {
                    int userId = Integer.parseInt(idStr.trim());
                    boolean deleted = userDAO.deleteUser(userId);
                    if (deleted) {
                        resp.getWriter().write("{\"message\": \"User deleted successfully\"}");
                    } else {
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        resp.getWriter().write("{\"error\": \"Failed to delete user\"}");
                    }
                    return;
                }
            }

            if (username == null || username.trim().isEmpty() ||
                fullName == null || fullName.trim().isEmpty() ||
                role == null || role.trim().isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Username, Full Name, and Role are required.\"}");
                return;
            }

            boolean isUpdate = (idStr != null && !idStr.trim().isEmpty() && !idStr.trim().equals("0"));

            if (!isUpdate && (password == null || password.trim().isEmpty())) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\": \"Password is required for new user creation.\"}");
                return;
            }

            User user = new User();
            user.setUsername(username.trim());
            user.setFullName(fullName.trim());
            user.setRole(role.trim());

            boolean success;
            if (isUpdate) {
                user.setId(Integer.parseInt(idStr.trim()));
                success = userDAO.updateUser(user, password);
            } else {
                success = userDAO.createUser(user, password.trim());
            }

            if (success) {
                resp.getWriter().write("{\"message\": \"" + (isUpdate ? "User updated successfully" : "User created successfully") + "\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"error\": \"" + (isUpdate ? "Failed to update user" : "Failed to create user (username may already exist)") + "\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\": \"An error occurred: " + e.getMessage() + "\"}");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    private static class UserFormRequest {
        String id;
        String username;
        String full_name;
        String fullName;
        String role;
        String password;
        String action;
    }
}
