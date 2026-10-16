package com.shop.filter;

import com.shop.model.User;
import com.shop.util.SessionUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter({"/cart", "/addToCart", "/removeFromCart", "/updateCart",
            "/checkout", "/payment", "/orderConfirmation",
            "/myOrders",
            "/wishlist", "/toggleWishlist",
            "/addAddress", "/deleteAddress"})
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        User u = SessionUtil.currentUser(req);
        if (u == null || !"USER".equals(u.getRole())) {
            SessionUtil.flash(req, "Please log in as a customer to continue.");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        resp.setHeader("Cache-Control", "no-store");
        chain.doFilter(request, response);
    }
}