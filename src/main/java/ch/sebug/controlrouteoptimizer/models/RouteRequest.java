package ch.sebug.controlrouteoptimizer.models;

import lombok.Data;

@Data
public class RouteRequest {
    private Long fromShelterId;
    private Long toShelterId;
}
