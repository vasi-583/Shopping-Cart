package com.shop.controller.admin;

import com.shop.model.User;
import com.shop.service.UserService;
import com.shop.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/login")
public class AdminLoginServlet extends HttpServlet {
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/admin/adminLogin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = userService.login(req.getParameter("email"), req.getParameter("password"));
        if (u == null || !"ADMIN".equals(u.getRole())) {
            req.setAttribute("error", "Invalid admin credentials.");
            req.getRequestDispatcher("/WEB-INF/views/admin/adminLogin.jsp").forward(req, resp);
            return;
        }
        SessionUtil.startSession(req, u);
        resp.sendRedirect(req.getContextPath() + "/admin/products");
    }
}