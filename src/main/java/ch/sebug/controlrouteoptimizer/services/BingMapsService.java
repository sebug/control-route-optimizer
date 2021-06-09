package ch.sebug.controlrouteoptimizer.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ch.sebug.controlrouteoptimizer.models.MapResult;
import ch.sebug.controlrouteoptimizer.models.RouteRequest;

@Service
public class BingMapsService implements MapService {
    private final WebClient webClient;

    private String bingMapsKey;

    public BingMapsService(@Value("${bingmaps.key}") String bingMapsKey) {
        this.bingMapsKey = bingMapsKey;
        System.out.println("Bing maps key is " + bingMapsKey);
        this.webClient = WebClient.builder()
            .baseUrl("https://dev.virtualearth.net")
            .build();
    }


    @Override
    public MapResult CalculateRoute(RouteRequest r) {
        if (r == null) {
            return null;
        }
        MapResult result = new MapResult();
        result.setLink(r.getFromShelterId() + " - " + r.getToShelterId());
        System.out.println(this.webClient);
        return result;
    }
    
}
