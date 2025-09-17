package com.example.onlineStore.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "discounts")
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discount_code", unique = true, nullable = false, length = 50)
    private String idDiscount;

    @Column(nullable = false, length = 120)
    private String nameDiscount;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal percentage;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date endDate;

    protected Discount() {
        // requerido por JPA
    }

    public Discount(String idDiscount, String nameDiscount,
                    BigDecimal percentage, Date startDate, Date endDate) {
        this.idDiscount = idDiscount;
        this.nameDiscount = nameDiscount;
        this.percentage = percentage;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public String getIdDiscount() {
        return idDiscount;
    }

    public void setIdDiscount(String idDiscount) {
        this.idDiscount = idDiscount;
    }

    public String getNameDiscount() {
        return nameDiscount;
    }

    public void setNameDiscount(String nameDiscount) {
        this.nameDiscount = nameDiscount;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isActive() {
        Date now = new Date();
        return now.after(startDate) && now.before(endDate);
    }
}
