package ch.sebug.controlrouteoptimizer.models;

import lombok.Data;

@Data
public class RouteRequest {
    private Shelter fromShelter;
    private Shelter toShelter;
    private String imageLink;
    private Integer minutes;
    private Integer seconds;
}
