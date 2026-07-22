package com.sunrisedental.dao;

import com.sunrisedental.model.User;
import java.util.List;

public interface UserDAO {
    User authenticate(String username, String password);
    List<User> getAllUsers();
    boolean createUser(User user, String rawPassword);
    boolean updateUser(User user, String rawPassword);
    boolean deleteUser(int id);
}
