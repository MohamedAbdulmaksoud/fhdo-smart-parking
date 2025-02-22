package com.fhdo.ui.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AddressAutocompleteService {

    public List<String> getSuggestions(String query) {
        String url = "https://photon.komoot.io/api/?q=" + query + "&limit=5";
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);

        List<Map<String, Object>> features = (List<Map<String, Object>>) response.get("features");
        return features.stream()
                .map(feature -> (String) ((Map<String, Object>) feature.get("properties")).get("name"))
                .toList();
    }
}
