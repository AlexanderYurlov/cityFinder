package com.geo.find.cityFinder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuggestionResponse {

    private List<SuggestionScore> suggestions;

}
