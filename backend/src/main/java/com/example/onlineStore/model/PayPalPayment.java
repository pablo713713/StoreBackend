package com.example.onlineStore.model;

import java.math.BigDecimal;

public class PayPalPayment implements IPaymentMethod {

    private String emailAccount;
    private String password;

    public PayPalPayment(String emailAccount, String password) {
        this.emailAccount = emailAccount;
        this.password = password;
    }

    @Override
    public boolean pay(BigDecimal amount) {
        return true;
    }

    @Override
    public String getPaymentDetails() {
        return "PayPal account: " + emailAccount;
    }

    public String getEmailAccount() {
        return emailAccount;
    }

    public void setEmailAccount(String emailAccount) {
        this.emailAccount = emailAccount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
