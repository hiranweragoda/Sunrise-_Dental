package com.sunrisedental.dao;

import com.sunrisedental.model.User;

public interface UserDAO {
    User authenticate(String username, String password);
}
