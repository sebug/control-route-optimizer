package ch.sebug.controlrouteoptimizer.models;

import java.util.List;

import lombok.Data;

@Data
public class BingMapsPoint {
    private String type;
    private List<Double> coordinates;
}
