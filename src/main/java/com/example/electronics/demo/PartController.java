package com.example.electronics.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/parts")
@CrossOrigin
public class PartController {

    @Autowired
    private PartRepository partRepository;

    @Autowired
    private PriceService priceService;

    @GetMapping("/search")
    public SearchResult searchParts(@RequestParam String partNumber) {

        String search = partNumber == null ? "" : partNumber.trim();

        List<Part> localParts = new ArrayList<>();

        if (!search.isEmpty()) {
            localParts.addAll(partRepository.findByPartNumberContainingIgnoreCase(search));

            if (localParts.isEmpty()) {
                localParts.addAll(partRepository.findByNameContainingIgnoreCase(search));
            }

            if (localParts.isEmpty()) {
                localParts.addAll(partRepository.findByTypeContainingIgnoreCase(search));
            }
        }

        localParts = removeDuplicates(localParts);

        List<OnlinePriceInfo> onlinePrices = new ArrayList<>();
        double average = 0.0;

        if (!localParts.isEmpty()) {
            onlinePrices = priceService.getOctopartPricesByPartNumber(localParts.get(0).getPartNumber());
            average = onlinePrices.stream()
                    .mapToDouble(OnlinePriceInfo::getPriceEur)
                    .average()
                    .orElse(0.0);
        }

        SearchResult result = new SearchResult();
        result.setLocalParts(localParts);
        result.setOnlinePrices(onlinePrices);
        result.setAverageOnlinePrice(average);

        if (localParts.isEmpty()) {
            result.setMessage("Keine passenden Lagerteile gefunden");
        } else {
            result.setMessage(localParts.size() + " Lagerteil(e) gefunden");
        }

        return result;
    }

    @GetMapping("/suggestions")
    public List<PartSuggestion> getSuggestions(@RequestParam String query) {
        String search = query == null ? "" : query.trim();

        if (search.isEmpty() || search.length() < 2) {
            return new ArrayList<>();
        }

        List<Part> parts = new ArrayList<>();
        parts.addAll(partRepository.findByPartNumberContainingIgnoreCase(search));
        parts.addAll(partRepository.findByNameContainingIgnoreCase(search));

        parts = removeDuplicates(parts);

        List<PartSuggestion> suggestions = new ArrayList<>();
        int limit = Math.min(parts.size(), 8);

        for (int i = 0; i < limit; i++) {
            Part part = parts.get(i);
            suggestions.add(new PartSuggestion(
                    part.getPartNumber(),
                    part.getName(),
                    part.getType()
            ));
        }

        return suggestions;
    }

    private List<Part> removeDuplicates(List<Part> parts) {
        Map<Long, Part> uniqueMap = new LinkedHashMap<>();
        for (Part part : parts) {
            uniqueMap.put(part.getId(), part);
        }
        return new ArrayList<>(uniqueMap.values());
    }
}