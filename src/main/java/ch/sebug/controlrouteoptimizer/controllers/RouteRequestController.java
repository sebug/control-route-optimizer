package ch.sebug.controlrouteoptimizer.controllers;

import java.util.List;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ch.sebug.controlrouteoptimizer.models.RouteRequest;
import ch.sebug.controlrouteoptimizer.models.Shelter;
import ch.sebug.controlrouteoptimizer.repositories.ShelterRepository;

import org.springframework.beans.factory.annotation.Autowired;

@Scope(value = "session")
@Component(value = "routeRequestController")
@ELBeanName(value = "routeRequestController")
@Join(path = "/routeRequest", to = "/routeRequest-form.jsf")
public class RouteRequestController {
    @Autowired
    private ShelterRepository shelterRepository;

    private List<Shelter> shelters;

    private RouteRequest routeRequest = new RouteRequest();

    @Deferred
    @RequestAction
    @IgnorePostback
    public void loadData() {
        shelters = shelterRepository.findAll();
    }

    public String calculateRoute() {
        return "/shelter-list.xhtml?faces-redirect=true";
    }

    public RouteRequest getRouteRequest() {
        return routeRequest;
    }
}
