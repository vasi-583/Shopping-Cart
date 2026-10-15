package com.shop.service;

import com.shop.dao.ProductDAO;
import com.shop.model.Product;
import java.util.List;

public class ProductService {
    private final ProductDAO dao = new ProductDAO();

    public List<Product> getAll()          { return dao.findAll(); }
    public Product getById(int id)         { return dao.findById(id); }
    public boolean add(Product p)          { return dao.add(p); }
    public boolean update(Product p)       { return dao.update(p); }
    public boolean delete(int id)          { return dao.delete(id); }
}