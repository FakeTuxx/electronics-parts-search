package com.example.electronics.demo;

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
}