package com.shop.util;

import java.time.YearMonth;
import java.util.regex.Pattern;

public class ValidationUtil {
    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isEmail(String s) {
        return !isBlank(s) && s.length() <= 120 && EMAIL.matcher(s.trim()).matches();
    }

    public static boolean isStrongPassword(String p) {
        return p != null && p.length() >= 8 && p.matches(".*[A-Za-z].*") && p.matches(".*\\d.*");
    }

    /** Luhn check on digits only. */
    public static boolean isValidCard(String d) {
        if (d == null || !d.matches("\\d{13,19}")) return false;
        int sum = 0;
        boolean alt = false;
        for (int i = d.length() - 1; i >= 0; i--) {
            int n = d.charAt(i) - '0';
            if (alt) { n *= 2; if (n > 9) n -= 9; }
            sum += n;
            alt = !alt;
        }
        return sum % 10 == 0;
    }

    /** Expects MM/YY and must not be in the past. */
    public static boolean isValidExpiry(String e) {
        if (e == null || !e.trim().matches("(0[1-9]|1[0-2])/\\d{2}")) return false;
        String[] p = e.trim().split("/");
        YearMonth exp = YearMonth.of(2000 + Integer.parseInt(p[1]), Integer.parseInt(p[0]));
        return !exp.isBefore(YearMonth.now());
    }

    public static boolean isValidCvv(String c) {
        return c != null && c.matches("\\d{3,4}");
    }
}