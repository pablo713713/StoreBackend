package com.example.onlineStore.controller;

import com.example.onlineStore.model.Payment;
import com.example.onlineStore.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create/{clientId}")
    public Payment createPayment(
            @PathVariable Long clientId,
            @RequestBody PaymentRequest request
    ) {
        return paymentService.createPaymentForClient(
                clientId,
                request.getType(),
                request.getAmount(),
                request.getParams()
        );
    }

    @PostMapping("/pay/{clientId}")
    public String makePayment(@PathVariable Long clientId) {
        boolean success = paymentService.makePayment(clientId);
        return success ? "Pago realizado con éxito" : "Pago fallido";
    }

    @GetMapping("/details/{clientId}")
    public String getPaymentDetails(@PathVariable Long clientId) {
        return paymentService.getPaymentDetails(clientId);
    }

    public static class PaymentRequest {
        private String type;
        private BigDecimal amount;
        private String[] params;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public String[] getParams() {
            return params;
        }

        public void setParams(String[] params) {
            this.params = params;
        }
    }
}
