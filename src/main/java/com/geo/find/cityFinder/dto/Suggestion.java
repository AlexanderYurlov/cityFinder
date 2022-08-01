package com.geo.find.cityFinder.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Suggestion {

    private String name;

    private Double latitude;

    private Double longitude;

}
