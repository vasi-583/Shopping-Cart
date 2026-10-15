package com.shop.controller.customer;

import com.shop.model.CartItem;
import com.shop.model.User;
import com.shop.service.CartService;
import com.shop.service.OrderService;
import com.shop.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {
    private final CartService cartService = new CartService();
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        show(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = SessionUtil.currentUser(req);
        try {
            int orderId = orderService.checkout(u.getUserId(),
                    req.getParameter("cardholder"), req.getParameter("cardNumber"),
                    req.getParameter("expiry"), req.getParameter("cvv"));
            resp.sendRedirect(req.getContextPath() + "/orderConfirmation?id=" + orderId);
        } catch (IllegalArgumentException | IllegalStateException e) {
            req.setAttribute("error", e.getMessage());
            show(req, resp);
        }
    }

    private void show(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = SessionUtil.currentUser(req);
        List<CartItem> items = cartService.getCart(u.getUserId());
        if (items.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }
        req.setAttribute("total", cartService.total(items));
        req.getRequestDispatcher("/WEB-INF/views/payment.jsp").forward(req, resp);
    }
}