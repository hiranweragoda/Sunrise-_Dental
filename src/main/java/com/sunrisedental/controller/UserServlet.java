package com.sunrisedental.controller;

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

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAOImpl();

    private boolean isAdmin(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            return "Admin".equalsIgnoreCase(user.getRole());
        }
        return false;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (!isAdmin(req)) {
            if (session != null) session.setAttribute("flashError", "Only administrators can manage user accounts.");
            resp.sendRedirect(req.getContextPath() + "/dashboard?tab=tab-users");
            return;
        }

        try {
            String idStr = req.getParameter("id");
            String action = req.getParameter("action");
            String username = req.getParameter("username");
            String fullName = req.getParameter("full_name");
            String role = req.getParameter("role");
            String password = req.getParameter("password");
            String confirmPassword = req.getParameter("confirm_password");

            if ("delete".equalsIgnoreCase(action)) {
                if (idStr != null && !idStr.trim().isEmpty()) {
                    int id = Integer.parseInt(idStr.trim());
                    User currentUser = (User) session.getAttribute("user");
                    if (currentUser != null && currentUser.getId() == id) {
                        session.setAttribute("flashError", "You cannot delete your own logged-in user account!");
                    } else {
                        boolean deleted = userDAO.deleteUser(id);
                        if (deleted) {
                            session.setAttribute("flashSuccess", "User account deleted successfully.");
                        } else {
                            session.setAttribute("flashError", "Failed to delete user account.");
                        }
                    }
                }
                resp.sendRedirect(req.getContextPath() + "/dashboard?tab=tab-users");
                return;
            }

            if (username == null || username.trim().isEmpty() ||
                fullName == null || fullName.trim().isEmpty() ||
                role == null || role.trim().isEmpty()) {
                session.setAttribute("flashError", "Username, Full Name, and Role are required.");
                resp.sendRedirect(req.getContextPath() + "/dashboard?tab=tab-users");
                return;
            }

            boolean isUpdate = (idStr != null && !idStr.trim().isEmpty() && !idStr.trim().equals("0"));

            if (!isUpdate && (password == null || password.trim().isEmpty())) {
                session.setAttribute("flashError", "Password is required for new user creation.");
                resp.sendRedirect(req.getContextPath() + "/dashboard?tab=tab-users");
                return;
            }

            if (password != null && !password.trim().isEmpty()) {
                if (confirmPassword == null || !password.equals(confirmPassword)) {
                    session.setAttribute("flashError", "Password and Confirm Password do not match!");
                    resp.sendRedirect(req.getContextPath() + "/dashboard?tab=tab-users");
                    return;
                }
            }

            User user = new User();
            user.setUsername(username.trim());
            user.setFullName(fullName.trim());
            user.setRole(role.trim());

            boolean success;
            if (isUpdate) {
                user.setId(Integer.parseInt(idStr.trim()));
                String pwdToUpdate = (password != null && !password.trim().isEmpty()) ? password.trim() : null;
                success = userDAO.updateUser(user, pwdToUpdate);
            } else {
                success = userDAO.createUser(user, password.trim());
            }

            if (success) {
                session.setAttribute("flashSuccess", isUpdate ? "User account updated successfully." : "User account created successfully.");
            } else {
                session.setAttribute("flashError", isUpdate ? "Failed to update user account." : "Failed to create user account (username may already exist).");
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("flashError", "Error processing user record: " + e.getMessage());
        }

        resp.sendRedirect(req.getContextPath() + "/dashboard?tab=tab-users");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String idStr = req.getParameter("id");
        HttpSession session = req.getSession(false);

        if ("delete".equalsIgnoreCase(action) && idStr != null && isAdmin(req)) {
            try {
                int id = Integer.parseInt(idStr.trim());
                User currentUser = (User) session.getAttribute("user");
                if (currentUser != null && currentUser.getId() == id) {
                    session.setAttribute("flashError", "You cannot delete your own logged-in user account!");
                } else {
                    boolean deleted = userDAO.deleteUser(id);
                    if (deleted) {
                        session.setAttribute("flashSuccess", "User account deleted successfully.");
                    } else {
                        session.setAttribute("flashError", "Failed to delete user account.");
                    }
                }
            } catch (Exception e) {
                session.setAttribute("flashError", "Failed to delete user account.");
            }
        }
        resp.sendRedirect(req.getContextPath() + "/dashboard?tab=tab-users");
    }
}
