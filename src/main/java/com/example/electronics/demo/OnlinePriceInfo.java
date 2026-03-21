package com.example.electronics.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlinePriceInfo {
    private String vendor;
    private double priceEur;
    private String url;
}