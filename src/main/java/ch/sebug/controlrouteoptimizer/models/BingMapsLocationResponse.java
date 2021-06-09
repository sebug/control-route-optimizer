package ch.sebug.controlrouteoptimizer.models;

import java.util.List;

import lombok.Data;

@Data
public class BingMapsLocationResponse {
    private String authenticationResultCode;

    List<BingMapsResourceSet<BingMapsLocationResource>> resourceSets;
}
