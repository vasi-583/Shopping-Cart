package com.shop.controller.customer;

import com.shop.model.User;
import com.shop.service.ProductService;
import com.shop.service.WishlistService;
import com.shop.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/products")
public class ProductListServlet extends HttpServlet {
    private final ProductService productService = new ProductService();
    private final WishlistService wishlistService = new WishlistService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("products", productService.getAll());
        User u = SessionUtil.currentUser(req);
        if (u != null && "USER".equals(u.getRole())) {
            req.setAttribute("wishlistIds", wishlistService.idsFor(u.getUserId()));
        }
        req.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(req, resp);
    }
}