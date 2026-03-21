package com.example.electronics.demo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OnlinePriceInfo {
    private String vendor;
    private double priceEur;
}