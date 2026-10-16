package com.shop.controller.customer;

import com.shop.model.User;
import com.shop.service.WishlistService;
import com.shop.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/wishlist")
public class WishlistServlet extends HttpServlet {
    private final WishlistService wishlistService = new WishlistService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = SessionUtil.currentUser(req);
        req.setAttribute("products", wishlistService.list(u.getUserId()));
        req.getRequestDispatcher("/WEB-INF/views/wishlist.jsp").forward(req, resp);
    }
}
