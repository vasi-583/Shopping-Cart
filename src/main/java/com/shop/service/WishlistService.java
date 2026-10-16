package com.shop.service;

import com.shop.dao.WishlistDAO;
import com.shop.model.Product;
import java.util.List;
import java.util.Set;

public class WishlistService {
    private final WishlistDAO dao = new WishlistDAO();

    public boolean toggle(int userId, int productId) { return dao.toggle(userId, productId); }
    public List<Product> list(int userId)            { return dao.findProducts(userId); }
    public Set<Integer> idsFor(int userId)           { return dao.idsByUser(userId); }
}