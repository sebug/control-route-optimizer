package ch.sebug.controlrouteoptimizer.controllers;

import java.util.List;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ch.sebug.controlrouteoptimizer.ShelterConverter;
import ch.sebug.controlrouteoptimizer.models.MapResult;
import ch.sebug.controlrouteoptimizer.models.RouteRequest;
import ch.sebug.controlrouteoptimizer.models.Shelter;
import ch.sebug.controlrouteoptimizer.repositories.ShelterRepository;
import ch.sebug.controlrouteoptimizer.services.MapService;

@Scope(value = "session")
@Component(value = "routeRequestController")
@ELBeanName(value = "routeRequestController")
@Join(path = "/routeRequest", to = "/routeRequest-form.jsf")
public class RouteRequestController {

    private final ShelterRepository shelterRepository;
    private final MapService mapService;

    public RouteRequestController(ShelterRepository shelterRepository,
    MapService mapService) {
        this.shelterRepository = shelterRepository;
        this.mapService = mapService;
    }

    private List<Shelter> shelters;

    private RouteRequest routeRequest = new RouteRequest();

    private Shelter fromShelter;

    private Shelter toShelter;

    @Deferred
    @RequestAction
    @IgnorePostback
    public void loadData() {
        shelters = shelterRepository.findAll();
    }

    public void calculateRoute() {
        if (fromShelter != null) {
            routeRequest.setFromShelter(fromShelter);
        }
        if (toShelter != null) {
            routeRequest.setToShelter(toShelter);
        }
        MapResult mapResult = mapService.CalculateRoute(routeRequest);
        if (mapResult != null) {
            routeRequest.setImageLink(mapResult.getImageLink());
        }
    }

    public RouteRequest getRouteRequest() {
        return routeRequest;
    }

    public Shelter getFromShelter() {
        return fromShelter;
    }

    public void setFromShelter(Shelter fromShelter) {
        this.fromShelter = fromShelter;
    }

    public Shelter getToShelter() {
        return toShelter;
    }

    public void setToShelter(Shelter toShelter) {
        this.toShelter = toShelter;
    }

    public List<Shelter> getShelters() {
        System.out.println("Shelter size: " + shelters.size());
        return shelters;
    }

    public ShelterConverter getShelterConverter() {
        return new ShelterConverter(shelters);
    }
}
