package com.shop.controller.customer;

import com.shop.model.User;
import com.shop.service.WishlistService;
import com.shop.util.SessionUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/toggleWishlist")
public class ToggleWishlistServlet extends HttpServlet {
    private final WishlistService wishlistService = new WishlistService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User u = SessionUtil.currentUser(req);
        try {
            int productId = Integer.parseInt(req.getParameter("productId"));
            boolean added = wishlistService.toggle(u.getUserId(), productId);
            SessionUtil.flash(req, added ? "Added to wishlist." : "Removed from wishlist.");
        } catch (NumberFormatException e) {
            SessionUtil.flash(req, "Invalid product.");
        }
        String back = "wishlist".equals(req.getParameter("back")) ? "/wishlist" : "/products";
        resp.sendRedirect(req.getContextPath() + back);
    }
}