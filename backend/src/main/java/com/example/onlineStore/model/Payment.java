package com.example.onlineStore.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Transient
    protected IPaymentMethod method;

    protected Payment() {
        // requerido por JPA
    }

    protected Payment(BigDecimal amount) {
        this.amount = amount;
        this.method = createPaymentMethod();
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    protected abstract IPaymentMethod createPaymentMethod();

    public boolean pay() {
        if (method == null) {
            throw new IllegalStateException("No se ha asignado método de pago");
        }
        return method.pay(amount);
    }

    public String getPaymentDetails() {
        if (method == null) {
            return "No se ha asignado método de pago";
        }
        return method.getPaymentDetails();
    }
}
