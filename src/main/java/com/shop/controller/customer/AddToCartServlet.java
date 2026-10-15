package com.shop.controller.customer;

import com.shop.model.User;
import com.shop.service.CartService;
import com.shop.util.SessionUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/addToCart")
public class AddToCartServlet extends HttpServlet {
    private final CartService cartService = new CartService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User u = SessionUtil.currentUser(req);
        try {
            int productId = Integer.parseInt(req.getParameter("productId"));
            boolean ok = cartService.add(u.getUserId(), productId);
            SessionUtil.flash(req, ok ? "Added to cart." : "Sorry, not enough stock available.");
        } catch (NumberFormatException e) {
            SessionUtil.flash(req, "Invalid product.");
        }
        resp.sendRedirect(req.getContextPath() + "/products");
    }
}