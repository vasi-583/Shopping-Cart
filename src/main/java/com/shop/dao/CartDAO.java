package com.shop.dao;

import com.shop.model.CartItem;
import com.shop.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {
    public void add(int userId, int productId) {
        String sql = "INSERT INTO cart(user_id,product_id,quantity) VALUES(?,?,1) "
                   + "ON DUPLICATE KEY UPDATE quantity = quantity + 1";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int quantityInCart(int userId, int productId) {
        String sql = "SELECT quantity FROM cart WHERE user_id=? AND product_id=?";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<CartItem> findByUser(int userId) {
        String sql = "SELECT c.cart_id,c.user_id,c.product_id,c.quantity,p.name,p.price,p.image_path,"
                   + "p.quantity AS stock FROM cart c JOIN products p ON p.product_id=c.product_id "
                   + "WHERE c.user_id=? ORDER BY c.cart_id";
        List<CartItem> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem ci = new CartItem();
                    ci.setCartId(rs.getInt("cart_id"));
                    ci.setUserId(rs.getInt("user_id"));
                    ci.setProductId(rs.getInt("product_id"));
                    ci.setQuantity(rs.getInt("quantity"));
                    ci.setProductName(rs.getString("name"));
                    ci.setPrice(rs.getBigDecimal("price"));
                    ci.setImagePath(rs.getString("image_path"));
                    ci.setStock(rs.getInt("stock"));
                    list.add(ci);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    public void remove(int cartId, int userId) {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM cart WHERE cart_id=? AND user_id=?")) {
            ps.setInt(1, cartId);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** +1 only if it stays within stock. Returns false when the stock limit is reached. */
    public boolean increase(int cartId, int userId) {
        String sql = "UPDATE cart c JOIN products p ON p.product_id=c.product_id "
                   + "SET c.quantity=c.quantity+1 "
                   + "WHERE c.cart_id=? AND c.user_id=? AND c.quantity < p.quantity";
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** -1; if the quantity is already 1 the item is removed from the cart. */
    public void decrease(int cartId, int userId) {
        try (Connection c = DBConnection.getConnection()) {
            try (PreparedStatement ps = c.prepareStatement(
                    "UPDATE cart SET quantity=quantity-1 WHERE cart_id=? AND user_id=? AND quantity>1")) {
                ps.setInt(1, cartId);
                ps.setInt(2, userId);
                if (ps.executeUpdate() > 0) return;
            }
            try (PreparedStatement del = c.prepareStatement(
                    "DELETE FROM cart WHERE cart_id=? AND user_id=?")) {
                del.setInt(1, cartId);
                del.setInt(2, userId);
                del.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** Total units in the cart (navbar badge). */
    public int countItems(int userId) {
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT COALESCE(SUM(quantity),0) FROM cart WHERE user_id=?")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}