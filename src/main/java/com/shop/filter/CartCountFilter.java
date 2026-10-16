package com.shop.filter;

import com.shop.model.User;
import com.shop.service.CartService;
import com.shop.util.SessionUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter("/*")
public class CartCountFilter implements Filter {
    private final CartService cartService = new CartService();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if ("GET".equals(req.getMethod())) {
            String path = req.getServletPath();
            if (!path.startsWith("/css") && !path.startsWith("/js") && !path.startsWith("/admin")) {
                User u = SessionUtil.currentUser(req);
                if (u != null && "USER".equals(u.getRole())) {
                    try {
                        req.setAttribute("cartCount", cartService.count(u.getUserId()));
                    } catch (RuntimeException ignored) {
                        // the badge is cosmetic: never break the page because of it
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }
}