package com.example.onlinestore.model;

import java.util.Date;
import java.math.BigDecimal;

public class Validation {

    private Validation() {
        throw new UnsupportedOperationException("Utility class");
    }

    private static final int MIN_PWD_LEN = 5;
    private static final int MAX_PWD_LEN = 128; // límite para evitar abusos

    public static boolean isValidUsername(String username) {
        if (username == null) return false;
        return username.length() > 4;
    }

    public static boolean isValidPassword(String password) {
        if (password == null) return false;
        int len = password.length();
        if (len < MIN_PWD_LEN || len > MAX_PWD_LEN) return false;

        boolean hasLetter = false;
        boolean hasDigit = false;

        for (int i = 0; i < len; i++) {
            char c = password.charAt(i);
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
            if (hasLetter && hasDigit) return true;
        }
        return false;
    }

    public static boolean isTimeGone(Date date) {
        if (date == null) return false;
        Date today = new Date();
        return date.before(today);
    }

    public static boolean isNotTimeGone(Date date) {
        return !isTimeGone(date);
    }

    public static boolean isValidAmount(BigDecimal amount) {
        return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isValidCreditCardNumber(String number) {
        if (number == null) return false;
        return number.matches("\\d{16}"); // este patrón es seguro (no hay backtracking peligroso)
    }

    public static boolean isValidPayPalEmail(String email) {
        if (email == null) return false;
        return email.contains("@");
    }

    public static boolean hasRequiredParams(String[] params, int expectedLength) {
        return params != null && params.length >= expectedLength;
    }

    public static boolean hasStarted(Date start) {
        return start == null || !start.after(new Date());
    }

    public static boolean notExpired(Date end) {
        return end == null || !end.before(new Date());
    }

    public static boolean isValidPercentage(BigDecimal pct) {
        return pct != null && pct.compareTo(BigDecimal.ZERO) > 0
                && pct.compareTo(new BigDecimal("100")) <= 0;
    }
}
