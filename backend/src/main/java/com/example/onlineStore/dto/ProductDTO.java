package com.example.onlineStore.dto;

import java.math.BigDecimal;

public record ProductDTO(
        Long id,
        String nameProduct,
        String imageUrl,      // si tu entidad no tiene imageUrl, cámbialo a null en el mapper
        String description,
        BigDecimal price,
        Integer stock
) {}
