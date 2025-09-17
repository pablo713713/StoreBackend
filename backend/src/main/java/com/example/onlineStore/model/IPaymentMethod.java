package com.example.onlineStore.model;

import java.math.BigDecimal;

public interface IPaymentMethod {
    boolean pay(BigDecimal amount);
    String getPaymentDetails();
}
