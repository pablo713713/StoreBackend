package com.example.onlineStore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "paypal_payments")
public class CreatorPayPalPayment extends Payment {

    private String email;
    private String password;

    protected CreatorPayPalPayment() {
        // requerido por JPA
    }

    public CreatorPayPalPayment(BigDecimal amount, String email, String password) {
        super(amount);
        this.email = email;
        this.password = password;
        this.method = createPaymentMethod();
    }

    @Override
    protected IPaymentMethod createPaymentMethod() {
        return new PayPalPayment(email, password);
    }
}
