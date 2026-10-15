package com.shop.controller.admin;

import com.shop.model.Product;
import com.shop.service.ProductService;
import com.shop.util.ProductFormUtil;
import com.shop.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/addProduct")
public class AddProductServlet extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/admin/productForm.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Product p = ProductFormUtil.parse(req);
            productService.add(p);
            SessionUtil.flash(req, "Product added.");
            resp.sendRedirect(req.getContextPath() + "/admin/products");
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/admin/productForm.jsp").forward(req, resp);
        }
    }
}