package com.example.onlineStore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "creditcard_payments")
public class CreatorCreditCardPayment extends Payment {

    private String cardNumber;
    private String cardHolder;
    private String expiryDate;
    private String cvv;

    protected CreatorCreditCardPayment() {
        // requerido por JPA
    }

    public CreatorCreditCardPayment(BigDecimal amount, String cardNumber, String cardHolder, String expiryDate, String cvv) {
        super(amount);
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
        this.method = createPaymentMethod();
    }

    @Override
    protected IPaymentMethod createPaymentMethod() {
        return new CreditCardPayment(cardNumber, cardHolder, expiryDate, cvv);
    }
}
