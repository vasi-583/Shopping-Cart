package com.shop.service;

import com.shop.dao.AddressDAO;
import com.shop.dao.CartDAO;
import com.shop.dao.OrderDAO;
import com.shop.model.Address;
import com.shop.model.CartItem;
import com.shop.model.Order;
import com.shop.util.ValidationUtil;
import java.math.BigDecimal;
import java.util.List;

public class OrderService {
    private final CartDAO cartDAO = new CartDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final AddressDAO addressDAO = new AddressDAO();
    private final CartService cartService = new CartService();

    public int checkout(int userId, int addressId, String holder, String cardNo, String expiry, String cvv) {
        if (ValidationUtil.isBlank(holder) || holder.length() > 100)
            throw new IllegalArgumentException("Cardholder name is required.");
        String digits = cardNo == null ? "" : cardNo.trim();
        if (!ValidationUtil.isValidCard(digits)) throw new IllegalArgumentException("Invalid card number.");
        if (!ValidationUtil.isValidExpiry(expiry)) throw new IllegalArgumentException("Invalid or expired date (use MM/YY).");
        if (!ValidationUtil.isValidCvv(cvv)) throw new IllegalArgumentException("Invalid CVV.");

        Address a = addressDAO.findById(addressId, userId);
        if (a == null) throw new IllegalArgumentException("Please select a valid delivery address.");

        List<CartItem> items = cartDAO.findByUser(userId);
        if (items.isEmpty()) throw new IllegalStateException("Your cart is empty.");
        BigDecimal total = cartService.total(items);

        // Simulated payment gateway: a real one would be called here.
        String last4 = digits.substring(digits.length() - 4);
        return orderDAO.placeOrder(userId, items, total, holder.trim(), last4,
                                   a.getFullName(), a.getPhone(), a.getFormatted());
    }

    public Order getForUser(int orderId, int userId) { return orderDAO.findForUser(orderId, userId); }
    public List<Order> getByUser(int userId)         { return orderDAO.findByUser(userId); }
    public List<Order> getAll()                      { return orderDAO.findAll(); }

    public void updateStatus(int orderId, String newStatus) {
        if (!List.of("SHIPPED", "DELIVERED", "CANCELLED").contains(newStatus))
            throw new IllegalArgumentException("Invalid status.");
        orderDAO.updateStatus(orderId, newStatus);
    }
}