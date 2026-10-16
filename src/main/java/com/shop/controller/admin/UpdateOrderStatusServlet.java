package com.shop.controller.admin;

import com.shop.service.OrderService;
import com.shop.util.SessionUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/updateOrderStatus")
public class UpdateOrderStatusServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int orderId = Integer.parseInt(req.getParameter("orderId"));
            String status = req.getParameter("status");
            orderService.updateStatus(orderId, status);
            SessionUtil.flash(req, "Order #" + orderId + " marked as " + status + ".");
        } catch (NumberFormatException e) {
            SessionUtil.flash(req, "Invalid order.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            SessionUtil.flash(req, e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/admin/orders");
    }
}