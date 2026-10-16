package com.shop.service;

import com.shop.dao.AddressDAO;
import com.shop.model.Address;
import com.shop.util.ValidationUtil;
import java.util.List;

public class AddressService {
    private static final int MAX_ADDRESSES = 5;
    private final AddressDAO dao = new AddressDAO();

    /** Returns an error message, or null on success. */
    public String add(int userId, String fullName, String phone, String line1,
                      String city, String state, String pincode) {
        if (ValidationUtil.isBlank(fullName) || fullName.length() > 100) return "Full name is required.";
        if (phone == null || !phone.trim().matches("[6-9]\\d{9}")) return "Enter a valid 10-digit mobile number.";
        if (ValidationUtil.isBlank(line1) || line1.length() > 200) return "Address is required (max 200 characters).";
        if (ValidationUtil.isBlank(city) || city.length() > 80) return "City is required.";
        if (ValidationUtil.isBlank(state) || state.length() > 80) return "State is required.";
        if (pincode == null || !pincode.trim().matches("\\d{6}")) return "Enter a valid 6-digit PIN code.";
        if (dao.countByUser(userId) >= MAX_ADDRESSES)
            return "You can save up to " + MAX_ADDRESSES + " addresses. Delete one first.";

        Address a = new Address();
        a.setUserId(userId);
        a.setFullName(fullName.trim());
        a.setPhone(phone.trim());
        a.setLine1(line1.trim());
        a.setCity(city.trim());
        a.setState(state.trim());
        a.setPincode(pincode.trim());
        dao.add(a);
        return null;
    }

    public List<Address> list(int userId)             { return dao.findByUser(userId); }
    public Address get(int addressId, int userId)     { return dao.findById(addressId, userId); }
    public void delete(int addressId, int userId)     { dao.delete(addressId, userId); }
}