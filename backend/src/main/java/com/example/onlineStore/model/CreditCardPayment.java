package com.example.onlineStore.model;

import java.math.BigDecimal;

public class CreditCardPayment implements IPaymentMethod {

    private String cardNumber;
    private String cardHolder;
    private String expirationDate;
    private String cvv;

    public CreditCardPayment(String cardNumber, String cardHolder, String expiryDate, String cvv) {
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.expirationDate = expiryDate;
        this.cvv = cvv;
    }

    @Override
    public boolean pay(BigDecimal amount) {
        System.out.println("Pagando " + amount + " con tarjeta ****" + cardNumber.substring(cardNumber.length() - 4));
        return true;
    }

    @Override
    public String getPaymentDetails() {
        return "CreditCard ****" + cardNumber.substring(cardNumber.length() - 4);
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
}
