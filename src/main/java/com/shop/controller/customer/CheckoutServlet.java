package com.shop.controller.customer;

import com.shop.model.Address;
import com.shop.model.CartItem;
import com.shop.model.User;
import com.shop.service.AddressService;
import com.shop.service.CartService;
import com.shop.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private final CartService cartService = new CartService();
    private final AddressService addressService = new AddressService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = SessionUtil.currentUser(req);
        List<CartItem> items = cartService.getCart(u.getUserId());
        if (items.isEmpty()) {
            SessionUtil.flash(req, "Your cart is empty.");
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }
        List<Address> addresses = addressService.list(u.getUserId());
        Integer selected = (Integer) req.getSession().getAttribute("addressId");
        if (selected == null && !addresses.isEmpty()) selected = addresses.get(0).getAddressId();

        req.setAttribute("items", items);
        req.setAttribute("total", cartService.total(items));
        req.setAttribute("addresses", addresses);
        req.setAttribute("selectedAddressId", selected);
        req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
    }

    /** Saves the chosen delivery address in the session, then moves on to payment. */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User u = SessionUtil.currentUser(req);
        try {
            int addressId = Integer.parseInt(req.getParameter("addressId"));
            if (addressService.get(addressId, u.getUserId()) == null) throw new NumberFormatException();
            req.getSession().setAttribute("addressId", addressId);
            resp.sendRedirect(req.getContextPath() + "/payment");
        } catch (NumberFormatException e) {
            SessionUtil.flash(req, "Please select a delivery address.");
            resp.sendRedirect(req.getContextPath() + "/checkout");
        }
    }
}