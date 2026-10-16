package com.shop.dao;

import com.shop.model.*;
import com.shop.util.DBConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public int placeOrder(int userId, List<CartItem> items, BigDecimal total, String cardholder, String last4,
                          String shipName, String shipPhone, String shipAddress) {
        Connection c = null;
        try {
            c = DBConnection.getConnection();
            c.setAutoCommit(false);

            int orderId;
            try (PreparedStatement ps = c.prepareStatement(
                    "INSERT INTO orders(user_id,total_amount,status,ship_name,ship_phone,ship_address) "
                  + "VALUES(?,?,'PLACED',?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, userId);
                ps.setBigDecimal(2, total);
                ps.setString(3, shipName);
                ps.setString(4, shipPhone);
                ps.setString(5, shipAddress);
                ps.executeUpdate();
                try (ResultSet k = ps.getGeneratedKeys()) {
                    k.next();
                    orderId = k.getInt(1);
                }
            }

            try (PreparedStatement item = c.prepareStatement(
                     "INSERT INTO order_items(order_id,product_id,product_name,quantity,price) VALUES(?,?,?,?,?)");
                 PreparedStatement stock = c.prepareStatement(
                     "UPDATE products SET quantity=quantity-? WHERE product_id=? AND quantity>=?")) {
                for (CartItem ci : items) {
                    stock.setInt(1, ci.getQuantity());
                    stock.setInt(2, ci.getProductId());
                    stock.setInt(3, ci.getQuantity());
                    if (stock.executeUpdate() == 0)
                        throw new IllegalStateException("Insufficient stock for " + ci.getProductName());
                    item.setInt(1, orderId);
                    item.setInt(2, ci.getProductId());
                    item.setString(3, ci.getProductName());
                    item.setInt(4, ci.getQuantity());
                    item.setBigDecimal(5, ci.getPrice());
                    item.executeUpdate();
                }
            }

            try (PreparedStatement pay = c.prepareStatement(
                    "INSERT INTO payments(order_id,cardholder_name,card_last4,payment_status) VALUES(?,?,?,'SUCCESS')")) {
                pay.setInt(1, orderId);
                pay.setString(2, cardholder);
                pay.setString(3, last4);
                pay.executeUpdate();
            }

            try (PreparedStatement clear = c.prepareStatement("DELETE FROM cart WHERE user_id=?")) {
                clear.setInt(1, userId);
                clear.executeUpdate();
            }

            c.commit();
            return orderId;
        } catch (SQLException | RuntimeException e) {
            try { if (c != null) c.rollback(); } catch (SQLException ignored) { }
            throw (e instanceof RuntimeException) ? (RuntimeException) e : new RuntimeException(e);
        } finally {
            try { if (c != null) { c.setAutoCommit(true); c.close(); } } catch (SQLException ignored) { }
        }
    }

    /** Admin status change in ONE transaction. On CANCELLED: stock restored, payment marked REFUNDED. */
    public void updateStatus(int orderId, String newStatus) {
        Connection c = null;
        try {
            c = DBConnection.getConnection();
            c.setAutoCommit(false);

            String current;
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT status FROM orders WHERE order_id=? FOR UPDATE")) {
                ps.setInt(1, orderId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) throw new IllegalArgumentException("Order not found.");
                    current = rs.getString(1);
                }
            }

            if (!Order.nextStatuses(current).contains(newStatus))
                throw new IllegalStateException("Cannot change status from " + current + " to " + newStatus + ".");

            try (PreparedStatement ps = c.prepareStatement("UPDATE orders SET status=? WHERE order_id=?")) {
                ps.setString(1, newStatus);
                ps.setInt(2, orderId);
                ps.executeUpdate();
            }

            if ("CANCELLED".equals(newStatus)) {
                try (PreparedStatement restore = c.prepareStatement(
                        "UPDATE products p JOIN order_items oi ON oi.product_id=p.product_id "
                      + "SET p.quantity = p.quantity + oi.quantity WHERE oi.order_id=?")) {
                    restore.setInt(1, orderId);
                    restore.executeUpdate();
                }
                try (PreparedStatement refund = c.prepareStatement(
                        "UPDATE payments SET payment_status='REFUNDED' WHERE order_id=?")) {
                    refund.setInt(1, orderId);
                    refund.executeUpdate();
                }
            }
            c.commit();
        } catch (SQLException | RuntimeException e) {
            try { if (c != null) c.rollback(); } catch (SQLException ignored) { }
            throw (e instanceof RuntimeException) ? (RuntimeException) e : new RuntimeException(e);
        } finally {
            try { if (c != null) { c.setAutoCommit(true); c.close(); } } catch (SQLException ignored) { }
        }
    }

    private List<Order> query(String where, Integer... params) {
        String sql = "SELECT o.order_id,o.user_id,o.total_amount,o.order_date,o.status,"
                   + "o.ship_name,o.ship_phone,o.ship_address,u.name AS user_name,"
                   + "p.cardholder_name,p.card_last4,p.payment_status FROM orders o "
                   + "JOIN users u ON u.user_id=o.user_id "
                   + "LEFT JOIN payments p ON p.order_id=o.order_id " + where + " ORDER BY o.order_id DESC";
        List<Order> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) ps.setInt(i + 1, params[i]);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order o = new Order();
                    o.setOrderId(rs.getInt("order_id"));
                    o.setUserId(rs.getInt("user_id"));
                    o.setUserName(rs.getString("user_name"));
                    o.setTotalAmount(rs.getBigDecimal("total_amount"));
                    o.setOrderDate(rs.getTimestamp("order_date"));
                    o.setStatus(rs.getString("status"));
                    o.setShipName(rs.getString("ship_name"));
                    o.setShipPhone(rs.getString("ship_phone"));
                    o.setShipAddress(rs.getString("ship_address"));
                    o.setCardholderName(rs.getString("cardholder_name"));
                    o.setCardLast4(rs.getString("card_last4"));
                    o.setPaymentStatus(rs.getString("payment_status"));
                    list.add(o);
                }
            }
            try (PreparedStatement is = c.prepareStatement(
                    "SELECT product_name,quantity,price FROM order_items WHERE order_id=?")) {
                for (Order o : list) {
                    is.setInt(1, o.getOrderId());
                    try (ResultSet r = is.executeQuery()) {
                        while (r.next()) {
                            OrderItem it = new OrderItem();
                            it.setProductName(r.getString("product_name"));
                            it.setQuantity(r.getInt("quantity"));
                            it.setPrice(r.getBigDecimal("price"));
                            o.getItems().add(it);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public List<Order> findAll() {
        return query("");
    }

    public List<Order> findByUser(int userId) {
        return query("WHERE o.user_id=?", userId);
    }

    public Order findForUser(int orderId, int userId) {
        List<Order> l = query("WHERE o.order_id=? AND o.user_id=?", orderId, userId);
        return l.isEmpty() ? null : l.get(0);
    }
}