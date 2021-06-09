package ch.sebug.controlrouteoptimizer.models;

import java.util.List;

import lombok.Data;

@Data
public class BingMapsRouteResponse {
    private List<BingMapsRouteResourceSet> resourceSets;
}
