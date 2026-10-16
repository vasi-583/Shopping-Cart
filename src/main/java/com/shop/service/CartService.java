package com.shop.service;

import com.shop.dao.CartDAO;
import com.shop.dao.ProductDAO;
import com.shop.model.CartItem;
import com.shop.model.Product;
import java.math.BigDecimal;
import java.util.List;

public class CartService {
    private final CartDAO cartDAO = new CartDAO();
    private final ProductDAO productDAO = new ProductDAO();

    public boolean add(int userId, int productId) {
        Product p = productDAO.findById(productId);
        if (p == null || p.getQuantity() <= 0) return false;
        if (cartDAO.quantityInCart(userId, productId) + 1 > p.getQuantity()) return false;
        cartDAO.add(userId, productId);
        return true;
    }

    public List<CartItem> getCart(int userId)       { return cartDAO.findByUser(userId); }
    public void remove(int cartId, int userId)      { cartDAO.remove(cartId, userId); }
    public boolean increase(int cartId, int userId) { return cartDAO.increase(cartId, userId); }
    public void decrease(int cartId, int userId)    { cartDAO.decrease(cartId, userId); }
    public int count(int userId)                    { return cartDAO.countItems(userId); }

    public BigDecimal total(List<CartItem> items) {
        return items.stream().map(CartItem::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}