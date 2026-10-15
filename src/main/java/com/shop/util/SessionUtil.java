package com.shop.util;

import com.shop.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {
    /** Creates a fresh session on login (prevents session fixation). */
    public static void startSession(HttpServletRequest req, User user) {
        HttpSession old = req.getSession(false);
        if (old != null) old.invalidate();
        HttpSession s = req.getSession(true);
        s.setAttribute("user", user);
        s.setMaxInactiveInterval(30 * 60);
    }

    public static User currentUser(HttpServletRequest req) {
        HttpSession s = req.getSession(false);
        return s == null ? null : (User) s.getAttribute("user");
    }

    public static void flash(HttpServletRequest req, String msg) {
        req.getSession().setAttribute("flash", msg);
    }
}