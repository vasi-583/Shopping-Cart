package com.shop.controller.customer;

import com.shop.model.User;
import com.shop.service.CartService;
import com.shop.util.SessionUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/updateCart")
public class UpdateCartServlet extends HttpServlet {
    private final CartService cartService = new CartService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User u = SessionUtil.currentUser(req);
        try {
            int cartId = Integer.parseInt(req.getParameter("cartId"));
            String action = req.getParameter("action");
            if ("inc".equals(action)) {
                if (!cartService.increase(cartId, u.getUserId()))
                    SessionUtil.flash(req, "No more stock available for this product.");
            } else if ("dec".equals(action)) {
                cartService.decrease(cartId, u.getUserId());
            }
        } catch (NumberFormatException ignored) { }
        resp.sendRedirect(req.getContextPath() + "/cart");
    }
}