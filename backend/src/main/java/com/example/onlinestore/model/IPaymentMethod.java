package com.example.onlinestore.model;

import java.math.BigDecimal;

public interface IPaymentMethod {
    boolean pay(BigDecimal amount);
    String getPaymentDetails();
}
