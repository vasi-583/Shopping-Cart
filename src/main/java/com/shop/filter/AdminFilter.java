package com.shop.filter;

import com.shop.model.User;
import com.shop.util.SessionUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebFilter("/admin/*")
public class AdminFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if ("/admin/login".equals(req.getServletPath())) {   // login page is public
            chain.doFilter(request, response);
            return;
        }
        User u = SessionUtil.currentUser(req);
        if (u == null || !"ADMIN".equals(u.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/admin/login");
            return;
        }
        resp.setHeader("Cache-Control", "no-store");
        chain.doFilter(request, response);
    }
}