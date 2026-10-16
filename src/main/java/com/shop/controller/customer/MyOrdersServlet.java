package com.shop.controller.customer;

import com.shop.model.User;
import com.shop.service.OrderService;
import com.shop.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/myOrders")
public class MyOrdersServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = SessionUtil.currentUser(req);
        req.setAttribute("orders", orderService.getByUser(u.getUserId()));
        req.getRequestDispatcher("/WEB-INF/views/myOrders.jsp").forward(req, resp);
    }
}