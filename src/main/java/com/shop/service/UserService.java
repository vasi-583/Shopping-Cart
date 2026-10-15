package com.shop.service;

import com.shop.dao.UserDAO;
import com.shop.model.User;
import com.shop.util.PasswordUtil;
import com.shop.util.ValidationUtil;
import java.util.List;

public class UserService {
    private final UserDAO dao = new UserDAO();

    /** Returns an error message, or null on success. */
    public String register(String name, String email, String password) {
        if (ValidationUtil.isBlank(name) || name.length() > 100) return "Name is required.";
        if (!ValidationUtil.isEmail(email)) return "Enter a valid email address.";
        if (!ValidationUtil.isStrongPassword(password))
            return "Password must be 8+ characters with at least one letter and one digit.";
        email = email.trim().toLowerCase();
        if (dao.findByEmail(email) != null) return "This email is already registered.";
        User u = new User();
        u.setName(name.trim());
        u.setEmail(email);
        u.setPassword(PasswordUtil.hash(password));
        return dao.register(u) ? null : "Registration failed. Try again.";
    }

    /** Returns the user (password stripped) or null if credentials are wrong. */
    public User login(String email, String password) {
        if (ValidationUtil.isBlank(email) || password == null) return null;
        User u = dao.findByEmail(email.trim().toLowerCase());
        if (u != null && PasswordUtil.verify(password, u.getPassword())) {
            u.setPassword(null);
            return u;
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = dao.findAll();
        users.forEach(u -> u.setPassword(null));
        return users;
    }
}