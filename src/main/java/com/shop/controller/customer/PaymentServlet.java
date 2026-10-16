package com.shop.controller.customer;

import com.shop.model.Address;
import com.shop.model.CartItem;
import com.shop.model.User;
import com.shop.service.AddressService;
import com.shop.service.CartService;
import com.shop.service.OrderService;
import com.shop.util.SessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {
    private final CartService cartService = new CartService();
    private final OrderService orderService = new OrderService();
    private final AddressService addressService = new AddressService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        show(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = SessionUtil.currentUser(req);
        Integer addressId = (Integer) req.getSession().getAttribute("addressId");
        if (addressId == null) {
            SessionUtil.flash(req, "Please select a delivery address.");
            resp.sendRedirect(req.getContextPath() + "/checkout");
            return;
        }
        try {
            int orderId = orderService.checkout(u.getUserId(), addressId,
                    req.getParameter("cardholder"), req.getParameter("cardNumber"),
                    req.getParameter("expiry"), req.getParameter("cvv"));
            req.getSession().removeAttribute("addressId");
            resp.sendRedirect(req.getContextPath() + "/orderConfirmation?id=" + orderId + "&placed=1");
        } catch (IllegalArgumentException | IllegalStateException e) {
            req.setAttribute("error", e.getMessage());
            show(req, resp);
        }
    }

    private void show(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User u = SessionUtil.currentUser(req);
        List<CartItem> items = cartService.getCart(u.getUserId());
        if (items.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }
        Integer addressId = (Integer) req.getSession().getAttribute("addressId");
        Address a = addressId == null ? null : addressService.get(addressId, u.getUserId());
        if (a == null) {
            SessionUtil.flash(req, "Please select a delivery address.");
            resp.sendRedirect(req.getContextPath() + "/checkout");
            return;
        }
        req.setAttribute("address", a);
        req.setAttribute("total", cartService.total(items));
        req.getRequestDispatcher("/WEB-INF/views/payment.jsp").forward(req, resp);
    }
}