package com.shop.controller.customer;

import com.shop.model.User;
import com.shop.service.CartService;
import com.shop.util.SessionUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/removeFromCart")
public class RemoveFromCartServlet extends HttpServlet {
    private final CartService cartService = new CartService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User u = SessionUtil.currentUser(req);
        try {
            cartService.remove(Integer.parseInt(req.getParameter("cartId")), u.getUserId());
            SessionUtil.flash(req, "Item removed.");
        } catch (NumberFormatException ignored) { }
        resp.sendRedirect(req.getContextPath() + "/cart");
    }
}