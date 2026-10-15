package com.shop.service;

import com.shop.dao.CartDAO;
import com.shop.dao.OrderDAO;
import com.shop.model.CartItem;
import com.shop.model.Order;
import com.shop.util.ValidationUtil;
import java.math.BigDecimal;
import java.util.List;

public class OrderService {
    private final CartDAO cartDAO = new CartDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final CartService cartService = new CartService();

    /** Validates payment details, creates the order, returns the order id.
     *  Throws IllegalArgumentException / IllegalStateException with a user-friendly message. */
    public int checkout(int userId, String holder, String cardNo, String expiry, String cvv) {
        if (ValidationUtil.isBlank(holder) || holder.length() > 100)
            throw new IllegalArgumentException("Cardholder name is required.");
        String digits = cardNo == null ? "" : cardNo.replaceAll("[\\s-]", "");
        if (!ValidationUtil.isValidCard(digits)) throw new IllegalArgumentException("Invalid card number.");
        if (!ValidationUtil.isValidExpiry(expiry)) throw new IllegalArgumentException("Invalid or expired date (use MM/YY).");
        if (!ValidationUtil.isValidCvv(cvv)) throw new IllegalArgumentException("Invalid CVV.");

        List<CartItem> items = cartDAO.findByUser(userId);
        if (items.isEmpty()) throw new IllegalStateException("Your cart is empty.");
        BigDecimal total = cartService.total(items);

        // Simulated payment gateway: a real one would be called here.
        String last4 = digits.substring(digits.length() - 4);
        return orderDAO.placeOrder(userId, items, total, holder.trim(), last4);
    }

    public Order getForUser(int orderId, int userId) { return orderDAO.findForUser(orderId, userId); }
    public List<Order> getAll()                      { return orderDAO.findAll(); }
}