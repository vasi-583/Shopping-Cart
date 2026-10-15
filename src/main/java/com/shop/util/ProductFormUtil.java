package com.shop.util;

import com.shop.model.Product;
import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

public class ProductFormUtil {

    public static Product parse(HttpServletRequest req) {
        String name = req.getParameter("name");
        if (ValidationUtil.isBlank(name) || name.length() > 150)
            throw new IllegalArgumentException("Product name is required (max 150 chars).");

        BigDecimal price;
        try {
            price = new BigDecimal(req.getParameter("price").trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("Price must be a valid number.");
        }
        if (price.signum() < 0) throw new IllegalArgumentException("Price cannot be negative.");

        int qty;
        try {
            qty = Integer.parseInt(req.getParameter("quantity").trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("Quantity must be a whole number.");
        }
        if (qty < 0) throw new IllegalArgumentException("Quantity cannot be negative.");

        Product p = new Product();
        String id = req.getParameter("productId");
        if (!ValidationUtil.isBlank(id)) p.setProductId(Integer.parseInt(id.trim()));
        p.setName(name.trim());
        p.setDescription(req.getParameter("description"));
        p.setCategory(req.getParameter("category"));
        p.setPrice(price);
        p.setQuantity(qty);
        p.setImagePath(validateUrl(req.getParameter("imageUrl")));
        return p;
    }

    /** Returns null if blank; only http/https URLs are accepted. */
    private static String validateUrl(String url) {
        if (ValidationUtil.isBlank(url)) return null;
        url = url.trim();
        if (url.length() > 500) throw new IllegalArgumentException("Image URL is too long (max 500 chars).");
        try {
            URI u = new URI(url);
            String scheme = u.getScheme();
            if (scheme == null
                    || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))
                    || u.getHost() == null)
                throw new IllegalArgumentException("Image URL must start with http:// or https://");
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Image URL is not valid.");
        }
        return url;
    }
}