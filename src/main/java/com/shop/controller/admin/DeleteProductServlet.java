package com.shop.controller.admin;

import com.shop.service.ProductService;
import com.shop.util.SessionUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/deleteProduct")
public class DeleteProductServlet extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            productService.delete(Integer.parseInt(req.getParameter("id")));
            SessionUtil.flash(req, "Product deleted.");
        } catch (NumberFormatException e) {
            SessionUtil.flash(req, "Invalid product.");
        }
        resp.sendRedirect(req.getContextPath() + "/admin/products");
    }
}