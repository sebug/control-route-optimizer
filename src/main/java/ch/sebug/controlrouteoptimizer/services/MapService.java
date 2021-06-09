package ch.sebug.controlrouteoptimizer.services;

import ch.sebug.controlrouteoptimizer.models.MapResult;
import ch.sebug.controlrouteoptimizer.models.RouteRequest;

public interface MapService {
    public MapResult CalculateRoute(RouteRequest r);
}
