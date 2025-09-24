package com.example.onlinestore.model;

import java.math.BigDecimal;

public class CreditCardPayment implements IPaymentMethod {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(CreditCardPayment.class);

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
        if (logger.isInfoEnabled()) {
            logger.info("Pagando {} con tarjeta ****{}", amount, cardNumber.substring(cardNumber.length() - 4));
        }
        return true;
    }

    @Override
    public String getPaymentDetails() {
        // Aquí no usamos logger, pero sí substring, así que conviene validar longitud
        if (cardNumber != null && cardNumber.length() >= 4) {
            return "CreditCard ****" + cardNumber.substring(cardNumber.length() - 4);
        }
        return "CreditCard (número inválido)";
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
