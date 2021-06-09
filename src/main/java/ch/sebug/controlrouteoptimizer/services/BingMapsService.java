package ch.sebug.controlrouteoptimizer.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import ch.sebug.controlrouteoptimizer.models.BingMapsResponse;
import ch.sebug.controlrouteoptimizer.models.MapResult;
import ch.sebug.controlrouteoptimizer.models.RouteRequest;
import ch.sebug.controlrouteoptimizer.models.Shelter;
import reactor.core.publisher.Mono;

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
        Shelter f = r.getFromShelter();
        Mono<BingMapsResponse> response = this.webClient.get().uri("/REST/v1/Locations?countryRegion={countryRegion}" +
         "&locality={locality}&postalCode={postalCode}&addressLine={addressLine}" +
         "&key={key}",
         f.getCountry(), f.getCity(), f.getZip(), f.getStreet(), bingMapsKey).retrieve()
         .bodyToMono(BingMapsResponse.class);

         System.out.println(response.block());

        return result;
    }
}
