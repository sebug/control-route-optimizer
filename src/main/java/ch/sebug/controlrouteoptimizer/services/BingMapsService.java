package ch.sebug.controlrouteoptimizer.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import ch.sebug.controlrouteoptimizer.models.BingMapsLocationResource;
import ch.sebug.controlrouteoptimizer.models.BingMapsLocationResponse;
import ch.sebug.controlrouteoptimizer.models.BingMapsRouteResource;
import ch.sebug.controlrouteoptimizer.models.BingMapsRouteResponse;
import ch.sebug.controlrouteoptimizer.models.MapResult;
import ch.sebug.controlrouteoptimizer.models.RouteRequest;
import ch.sebug.controlrouteoptimizer.models.Shelter;
import reactor.core.publisher.Mono;

@Service
public class BingMapsService implements MapService {
    private final WebClient webClient;

    private String bingMapsKey;

    private String baseUrl = "https://dev.virtualearth.net";

    public BingMapsService(@Value("${bingmaps.key}") String bingMapsKey) {
        this.bingMapsKey = bingMapsKey;
        System.out.println("Bing maps key is " + bingMapsKey);
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }


    @Override
    public MapResult CalculateRoute(RouteRequest r) {
        if (r == null) {
            return null;
        }
        MapResult result = new MapResult();
        BingMapsLocationResource fromResource = getAddress(r.getFromShelter());
        BingMapsLocationResource toResource = getAddress(r.getToShelter());
        
        System.out.println(fromResource);
        System.out.println(toResource);

        if (fromResource != null && toResource != null) {
            String link = UriComponentsBuilder.fromUriString(baseUrl)
            .path("/REST/v1/Routes/Driving")
            .queryParam("waypoint.1", fromResource.getPoint().getCoordinates().get(0) + "," + fromResource.getPoint().getCoordinates().get(1))
            .queryParam("waypoint.2", toResource.getPoint().getCoordinates().get(0) + "," + toResource.getPoint().getCoordinates().get(1))
            .queryParam("key", bingMapsKey)
            .build().toString();

            Mono<BingMapsRouteResponse> routeResponseMono = this.webClient.get().uri(link).retrieve().bodyToMono(BingMapsRouteResponse.class);

            BingMapsRouteResponse routeResponse = routeResponseMono.block();

            BingMapsRouteResource routeResource = routeResponse.getResourceSets().get(0)
            .getResources().get(0);

            int seconds = routeResource.getTravelDuration();
            result.setMinutes(seconds / 60);
            result.setSeconds(seconds % 60);

            System.out.println(routeResource);

            String imageLink = UriComponentsBuilder.fromUriString(baseUrl)
            .path("/REST/v1/Imagery/Map/Road/Routes/Driving")
            .queryParam("waypoint.1", fromResource.getPoint().getCoordinates().get(0) + "," + fromResource.getPoint().getCoordinates().get(1))
            .queryParam("waypoint.2", toResource.getPoint().getCoordinates().get(0) + "," + toResource.getPoint().getCoordinates().get(1))
            .queryParam("key", bingMapsKey)
            .build().toString();

            result.setImageLink(imageLink);
        }

        return result;
    }

    private BingMapsLocationResource getAddress(Shelter s) {
        Mono<BingMapsLocationResponse> response = this.webClient.get().uri("/REST/v1/Locations?countryRegion={countryRegion}" +
         "&locality={locality}&postalCode={postalCode}&addressLine={addressLine}" +
         "&key={key}",
         s.getCountry(), s.getCity(), s.getZip(), s.getStreet(), bingMapsKey).retrieve()
         .bodyToMono(BingMapsLocationResponse.class);

         BingMapsLocationResponse result = response.block();

         return result.getResourceSets().get(0).getResources().get(0);
    }
}
