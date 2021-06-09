package ch.sebug.controlrouteoptimizer.services;

import org.springframework.stereotype.Service;

import ch.sebug.controlrouteoptimizer.models.MapResult;
import ch.sebug.controlrouteoptimizer.models.RouteRequest;

@Service
public class BingMapsService implements MapService {

    @Override
    public MapResult CalculateRoute(RouteRequest r) {
        if (r == null) {
            return null;
        }
        MapResult result = new MapResult();
        result.setLink(r.getFromShelterId() + " - " + r.getToShelterId());
        return result;
    }
    
}
