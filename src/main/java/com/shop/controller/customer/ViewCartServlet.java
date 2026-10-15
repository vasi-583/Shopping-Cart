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

@WebServlet("/cart")
public class ViewCartServlet extends HttpServlet {
    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = SessionUtil.currentUser(req);
        List<CartItem> items = cartService.getCart(u.getUserId());
        req.setAttribute("items", items);
        req.setAttribute("total", cartService.total(items));
        req.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(req, resp);
    }
}