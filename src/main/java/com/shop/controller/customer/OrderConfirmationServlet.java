package com.shop.controller.customer;

import com.shop.model.Order;
import com.shop.model.User;
import com.shop.service.OrderService;
import com.shop.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/orderConfirmation")
public class OrderConfirmationServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = SessionUtil.currentUser(req);
        try {
            Order o = orderService.getForUser(Integer.parseInt(req.getParameter("id")), u.getUserId());
            if (o == null) { resp.sendError(404); return; }
            req.setAttribute("order", o);
            req.getRequestDispatcher("/WEB-INF/views/orderConfirmation.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(400);
        }
    }
}