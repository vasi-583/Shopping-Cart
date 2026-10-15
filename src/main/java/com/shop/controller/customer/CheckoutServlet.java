package com.shop.controller.customer;

import com.shop.model.CartItem;
import com.shop.model.User;
import com.shop.service.CartService;
import com.shop.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = SessionUtil.currentUser(req);
        List<CartItem> items = cartService.getCart(u.getUserId());
        if (items.isEmpty()) {
            SessionUtil.flash(req, "Your cart is empty.");
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }
        req.setAttribute("items", items);
        req.setAttribute("total", cartService.total(items));
        req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
    }
}