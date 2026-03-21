package com.example.electronics.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnlinePriceInfo {
    private String vendorName;
    private String productUrl;
    private Double unitPrice;
    private Double price10;
    private Double price100;
    private Double price1000;
    private boolean available;
}