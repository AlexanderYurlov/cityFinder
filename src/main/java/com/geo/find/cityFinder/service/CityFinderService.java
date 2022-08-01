package com.geo.find.cityFinder.service;

import com.geo.find.cityFinder.dto.Suggestion;
import com.geo.find.cityFinder.dto.SuggestionScore;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CityFinderService {

    private final SuggestionService suggestionService;

    private final double ROUNDING_DELTA = 1;

    public List<SuggestionScore> find(String q, Double latitude, Double longitude) {
        double max = 0;
        List<SuggestionScore> suggestionScoreList = new ArrayList<>();
        for (Suggestion s : suggestionService.getSuggestions(q)) {
            double distance = getDistance(s, latitude, longitude);
            if (max < distance) {
                max = distance;
            }
            SuggestionScore suggestionScore = new SuggestionScore(s);
            suggestionScore.setDistance(distance);
            suggestionScoreList.add(suggestionScore);

        }
        double maxFinal = max + ROUNDING_DELTA;

        return suggestionScoreList.stream()
                .peek(s -> s.setScore(getScore(s, maxFinal)))
                .sorted((o1, o2) -> o2.getScore() > o1.getScore() ? 1 : -1)
                .collect(Collectors.toList());
    }

    private Double getScore(SuggestionScore s, double maxFinal) {
        return maxFinal != 0 ? round((maxFinal - s.getDistance()) / maxFinal, 1) : null;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private double getDistance(Suggestion s, Double latitude, Double longitude) {
        double deltaLatitude = 0;
        double deltaLongitude = 0;
        if (latitude != null) {
            deltaLatitude = s.getLatitude() - latitude;
        }
        if (longitude != null) {
            deltaLongitude = s.getLongitude() - longitude;
        }

        return Math.sqrt(deltaLatitude * deltaLatitude + deltaLongitude * deltaLongitude);

    }

}
