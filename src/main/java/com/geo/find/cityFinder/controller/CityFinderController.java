package com.geo.find.cityFinder.controller;

import com.geo.find.cityFinder.dto.Suggestion;
import com.geo.find.cityFinder.dto.SuggestionResponse;
import com.geo.find.cityFinder.dto.SuggestionScore;
import com.geo.find.cityFinder.service.CityFinderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class CityFinderController {

    public static final String BASE_PATH = "";
    public static final String SUGGESTIONS_PATH = BASE_PATH + "suggestions";

    private final CityFinderService cityFinderService;

    @GetMapping(SUGGESTIONS_PATH)
    public ResponseEntity<SuggestionResponse> findCities(@RequestParam(value = "q") String q,
                                                         @RequestParam(value = "latitude", required = false) Double latitude,
                                                         @RequestParam(value = "longitude", required = false) Double longitude) {
        List<SuggestionScore> suggestions = cityFinderService.find(q, latitude, longitude);
        return ResponseEntity
                .ok(SuggestionResponse.builder()
                        .suggestions(suggestions)
                        .build());
    }

}
