package com.geo.find.cityFinder.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuggestionScore extends Suggestion {

    @Setter
    private Double score;

    @Setter
    @JsonIgnore
    private double distance;

    public SuggestionScore(Suggestion s) {
        this.setName(s.getName());
        this.setLatitude(s.getLatitude());
        this.setLongitude(s.getLongitude());
    }
}
