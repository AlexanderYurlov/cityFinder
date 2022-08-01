package com.geo.find.cityFinder.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NodeName {

    @Builder.Default
    private String name = "";

    private NodeName parent;

    @Builder.Default
    private Map<String, NodeName> children = new HashMap();

    @Builder.Default
    private Boolean isNameExist = false;

}
