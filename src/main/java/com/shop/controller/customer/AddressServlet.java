package com.shop.controller.customer;

import com.shop.model.User;
import com.shop.service.AddressService;
import com.shop.util.SessionUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet({"/addAddress", "/deleteAddress"})
public class AddressServlet extends HttpServlet {
    private final AddressService addressService = new AddressService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User u = SessionUtil.currentUser(req);

        if ("/deleteAddress".equals(req.getServletPath())) {
            try {
                int id = Integer.parseInt(req.getParameter("addressId"));
                addressService.delete(id, u.getUserId());
                Integer selected = (Integer) req.getSession().getAttribute("addressId");
                if (selected != null && selected == id) req.getSession().removeAttribute("addressId");
                SessionUtil.flash(req, "Address deleted.");
            } catch (NumberFormatException e) {
                SessionUtil.flash(req, "Invalid address.");
            }
        } else {
            String error = addressService.add(u.getUserId(),
                    req.getParameter("fullName"), req.getParameter("phone"),
                    req.getParameter("line1"), req.getParameter("city"),
                    req.getParameter("state"), req.getParameter("pincode"));
            SessionUtil.flash(req, error != null ? error : "Address saved.");
        }
        resp.sendRedirect(req.getContextPath() + "/checkout");
    }
}