package com.shop.controller.admin;

import com.shop.model.Product;
import com.shop.service.ProductService;
import com.shop.util.ProductFormUtil;
import com.shop.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/updateProduct")
public class UpdateProductServlet extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Product p = productService.getById(Integer.parseInt(req.getParameter("id")));
            if (p == null) { resp.sendError(404); return; }
            req.setAttribute("product", p);
            req.getRequestDispatcher("/WEB-INF/views/admin/productForm.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(400);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Product p = ProductFormUtil.parse(req);
            productService.update(p);
            SessionUtil.flash(req, "Product updated.");
            resp.sendRedirect(req.getContextPath() + "/admin/products");
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            try {
                req.setAttribute("product", productService.getById(Integer.parseInt(req.getParameter("productId"))));
            } catch (NumberFormatException ignored) { }
            req.getRequestDispatcher("/WEB-INF/views/admin/productForm.jsp").forward(req, resp);
        }
    }
}