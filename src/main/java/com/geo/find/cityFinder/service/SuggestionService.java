package com.geo.find.cityFinder.service;

import com.geo.find.cityFinder.dto.NodeName;
import com.geo.find.cityFinder.dto.Suggestion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SuggestionService {

    private static final String CITY_FILE_NAME = "/static/cities_canada-usa.tsv";
    private static final String DELIMITER = ", ";
    private static final Map<String, String> COUNTRY_MAP = Map.of(
            "US", "USA",
            "CA", "Canada"
    );
    private final Map<String, Suggestion> allSuggestion = new HashMap<>();
    private final NodeName rootNode = new NodeName();

    @PostConstruct
    public void init() {
        initAllSuggestion();
        log.info("All Suggestions is initialized");
        initRootNode(allSuggestion.values());
        log.info("Nodes is initialized");
    }

    public List<Suggestion> getSuggestions(String q) {
        List<String> keys = getCityNames(q);
        return keys.stream()
                .map(allSuggestion::get)
                .collect(Collectors.toList());

    }

    private List<String> getCityNames(String q) {
        String[] chars = q.split("");
        List<String> keys = new ArrayList<>();
        return getNames(rootNode, chars, 0, keys);
    }

    private List<String> getNames(NodeName nodeName, String[] chars, int i, List<String> keys) {
        if (nodeName.getName().equals("") && i == 0) {
            if (nodeName.getChildren().containsKey(chars[i])) {
                getNames(nodeName.getChildren().get(chars[i]), chars, i, keys);
            }
        } else {
            int nextI = ++i;
            if (chars.length <= i) {
                if (nodeName.getIsNameExist()) {
                    StringBuilder sb = new StringBuilder();
                    keys.add(recoveryName(nodeName, sb).toString());
                }
                for (NodeName nn : nodeName.getChildren().values()) {
                    getNames(nn, chars, nextI, keys);
                }
            } else {
                if (nodeName.getChildren().containsKey(chars[i])) {
                    getNames(nodeName.getChildren().get(chars[i]), chars, nextI, keys);
                }
            }
        }
        return keys;
    }

    private StringBuilder recoveryName(NodeName nodeName, StringBuilder sb) {
        if (!nodeName.getName().equals("")) {
            sb.insert(0, nodeName.getName());
            recoveryName(nodeName.getParent(), sb);
        }
        return sb;
    }

    private void initAllSuggestion() {
        try (BufferedReader sampleBuffer = new BufferedReader(new InputStreamReader(
                this.getClass().getResourceAsStream(CITY_FILE_NAME)))) {
            sampleBuffer.readLine(); // skip first
            String line;
            while ((line = sampleBuffer.readLine()) != null) {
                List<String> value = Arrays.asList(line.split("\t"));
                String fullName = getFullName(value.get(1), value.get(10), value.get(8));
                allSuggestion.put(fullName, Suggestion.builder()
                        .name(fullName)
                        .latitude(Double.parseDouble(value.get(4)))
                        .longitude(Double.parseDouble(value.get(5)))
                        .build());
            }
        } catch (Exception e) {
            log.error("Something wrong with reading city file");
            e.printStackTrace();
        }
    }

    private String getFullName(String city, String state, String country) {
        return city + DELIMITER + state + DELIMITER + COUNTRY_MAP.get(country);
    }

    private void initRootNode(Collection<Suggestion> allSuggestion) {
        for (Suggestion suggestion : allSuggestion) {
            String[] chars = suggestion.getName().split("");
            addNodeName(rootNode, chars, 0);
        }
    }

    private void addNodeName(NodeName nodeName, String[] chars, int i) {
        if (nodeName.getName().equals(chars[i])) {
            if (chars.length - 1 == i) {
                nodeName.setIsNameExist(true);
            } else {
                int nextI = ++i;
                if (!nodeName.getChildren().containsKey(chars[nextI])) {
                    nodeName.getChildren().put(chars[nextI], NodeName.builder()
                            .name(chars[nextI])
                            .parent(nodeName)
                            .build());
                }
                addNodeName(nodeName.getChildren().get(chars[nextI]), chars, nextI);
            }
        } else if (nodeName.getName().equals("")) {
            if (!nodeName.getChildren().containsKey(chars[i])) {
                nodeName.getChildren().put(chars[i], NodeName.builder()
                        .name(chars[i])
                        .parent(nodeName)
                        .build());
            }
            addNodeName(nodeName.getChildren().get(chars[i]), chars, i);
        } else {
            throw new RuntimeException("Something wrong");
        }
    }

}
