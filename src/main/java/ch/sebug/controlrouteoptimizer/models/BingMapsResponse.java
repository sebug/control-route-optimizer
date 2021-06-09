package ch.sebug.controlrouteoptimizer.models;

import java.util.List;

import lombok.Data;

@Data
public class BingMapsResponse {
    private String authenticationResultCode;

    List<BingMapsResourceSet> resourceSets;
}
