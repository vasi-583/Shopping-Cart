package com.shop.dao;

import com.shop.model.Product;
import com.shop.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WishlistDAO {

    /** Removes the product if already wishlisted, otherwise adds it. Returns true if now wishlisted. */
    public boolean toggle(int userId, int productId) {
        try (Connection c = DBConnection.getConnection()) {
            try (PreparedStatement del = c.prepareStatement(
                    "DELETE FROM wishlist WHERE user_id=? AND product_id=?")) {
                del.setInt(1, userId);
                del.setInt(2, productId);
                if (del.executeUpdate() > 0) return false;
            }
            try (PreparedStatement ins = c.prepareStatement(
                    "INSERT IGNORE INTO wishlist(user_id,product_id) VALUES(?,?)")) {
                ins.setInt(1, userId);
                ins.setInt(2, productId);
                return ins.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<Integer> idsByUser(int userId) {
        Set<Integer> ids = new HashSet<>();
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("SELECT product_id FROM wishlist WHERE user_id=?")) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ids.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ids;
    }

    public List<Product> findProducts(int userId) {
        String sql = "SELECT p.* FROM wishlist w JOIN products p ON p.product_id=w.product_id "
                   + "WHERE w.user_id=? ORDER BY w.wishlist_id DESC";
        List<Product> list = new ArrayList<>();
        try (Connection c = DBConnection.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();
                    p.setProductId(rs.getInt("product_id"));
                    p.setName(rs.getString("name"));
                    p.setDescription(rs.getString("description"));
                    p.setPrice(rs.getBigDecimal("price"));
                    p.setCategory(rs.getString("category"));
                    p.setQuantity(rs.getInt("quantity"));
                    p.setImagePath(rs.getString("image_path"));
                    list.add(p);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}