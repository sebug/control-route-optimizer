package ch.sebug.controlrouteoptimizer.models;

import lombok.Data;

@Data
public class BingMapsLocationResource {
    private String name;
    private String confidence;
    private BingMapsPoint point;
}
