package com.example.electronics.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "parts")
public class Part {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "part_number", nullable = false, unique = true)
    private String partNumber;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "mount_type", nullable = false)
    private String mountType;

    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "purchase_price_eur", nullable = false)
    private Double purchasePriceEur;

    @Transient
    @JsonProperty("price1Eur")
    public double getPrice1Eur() {
        return round3(purchasePriceEur * 1.80);
    }

    @Transient
    @JsonProperty("price10Eur")
    public double getPrice10Eur() {
        return round3(purchasePriceEur * 10 * 1.65);
    }

    @Transient
    @JsonProperty("price100Eur")
    public double getPrice100Eur() {
        return round3(purchasePriceEur * 100 * 1.50);
    }

    @Transient
    @JsonProperty("price1000Eur")
    public double getPrice1000Eur() {
        return round3(purchasePriceEur * 1000 * 1.35);
    }

    private double round3(double value) {
        return Math.round(value * 1000.0) / 1000.0;
    }
}