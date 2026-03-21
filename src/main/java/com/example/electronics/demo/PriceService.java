package com.example.electronics.demo;

import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class PriceService {

    private static final List<String> VENDORS = List.of(
            "Avnet",
            "Arrow Electronics",
            "DigiKey",
            "Distrelec",
            "element14",
            "Farnell",
            "Future Electronics",
            "Heilind",
            "Onlinecomponents.com",
            "Master Electronics",
            "Mouser",
            "Newark",
            "Rochester Electronics",
            "RS",
            "Sager Electronics",
            "TME",
            "TTI",
            "Verical"
    );

    public List<OnlinePriceInfo> getOnlinePrices(Part part) {
        List<OnlinePriceInfo> results = new ArrayList<>();

        for (String vendor : VENDORS) {
            results.add(buildVendorPrice(vendor, part));
        }

        return results;
    }

    private OnlinePriceInfo buildVendorPrice(String vendor, Part part) {
        String url = buildSearchUrl(vendor, part.getPartNumber());

        Double unitPrice = simulateUnitPrice(part, vendor);

        if (unitPrice == null) {
            return new OnlinePriceInfo(vendor, url, null, null, null, null, false);
        }

        double unit10 = unitPrice * 0.97;
        double unit100 = unitPrice * 0.92;
        double unit1000 = unitPrice * 0.85;

        double total1 = unitPrice;
        double total10 = unit10 * 10;
        double total100 = unit100 * 100;
        double total1000 = unit1000 * 1000;

        return new OnlinePriceInfo(
                vendor,
                url,
                round(total1),
                round(total10),
                round(total100),
                round(total1000),
                true
        );
    }

    private Double simulateUnitPrice(Part part, String vendor) {
        if (part.getPurchasePriceEur() == null) {
            return null;
        }

        double factor = switch (vendor) {
            case "DigiKey" -> 1.18;
            case "Mouser" -> 1.16;
            case "Farnell" -> 1.20;
            case "RS" -> 1.19;
            case "TME" -> 1.12;
            case "Arrow Electronics" -> 1.17;
            case "Avnet" -> 1.14;
            case "Distrelec" -> 1.15;
            case "element14" -> 1.18;
            case "Future Electronics" -> 1.16;
            case "Heilind" -> 1.13;
            case "Onlinecomponents.com" -> 1.15;
            case "Master Electronics" -> 1.14;
            case "Newark" -> 1.17;
            case "Rochester Electronics" -> 1.21;
            case "Sager Electronics" -> 1.15;
            case "TTI" -> 1.16;
            case "Verical" -> 1.14;
            default -> 1.15;
        };

        return part.getPurchasePriceEur() * factor;
    }

    private String buildSearchUrl(String vendor, String partNumber) {
        String encoded = URLEncoder.encode(partNumber, StandardCharsets.UTF_8);

        return switch (vendor) {
            case "DigiKey" -> "https://www.digikey.com/en/products/result?keywords=" + encoded;
            case "Mouser" -> "https://www.mouser.com/c/?q=" + encoded;
            case "Farnell" -> "https://de.farnell.com/search?st=" + encoded;
            case "RS" -> "https://de.rs-online.com/web/c/?searchTerm=" + encoded;
            case "TME" -> "https://www.tme.eu/de/katalog/?search=" + encoded;
            case "Arrow Electronics" -> "https://www.arrow.com/en/products/search?q=" + encoded;
            case "Avnet" -> "https://www.avnet.com/shop/us/search/" + encoded;
            case "Distrelec" -> "https://www.distrelec.de/search?q=" + encoded;
            case "element14" -> "https://de.element14.com/search?st=" + encoded;
            case "Future Electronics" -> "https://www.futureelectronics.com/search?text=" + encoded;
            case "Heilind" -> "https://www.heilind.com/search?text=" + encoded;
            case "Onlinecomponents.com" -> "https://www.onlinecomponents.com/en/search?searchTerm=" + encoded;
            case "Master Electronics" -> "https://www.masterelectronics.com/en/search?searchTerm=" + encoded;
            case "Newark" -> "https://www.newark.com/search?st=" + encoded;
            case "Rochester Electronics" -> "https://www.rocelec.com/en/search?query=" + encoded;
            case "Sager Electronics" -> "https://www.sager.com/search?text=" + encoded;
            case "TTI" -> "https://www.tti.com/content/ttiinc/en/apps/part-search.html#/?searchTerm=" + encoded;
            case "Verical" -> "https://www.verical.com/search?q=" + encoded;
            default -> "https://www.google.com/search?q=" + encoded + "+" + URLEncoder.encode(vendor, StandardCharsets.UTF_8);
        };
    }

    private double round(double value) {
        return Math.round(value * 1000.0) / 1000.0;
    }
}