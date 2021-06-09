package ch.sebug.controlrouteoptimizer.controllers;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ch.sebug.controlrouteoptimizer.models.RouteRequest;

@Scope(value = "session")
@Component(value = "routeRequestController")
@ELBeanName(value = "routeRequestController")
@Join(path = "/routeRequest", to = "/routeRequest-form.jsf")
public class RouteRequestController {
    private RouteRequest routeRequest = new RouteRequest();

    public String calculateRoute() {
        return "/shelter-list.xhtml?faces-redirect=true";
    }

    public RouteRequest getRouteRequest() {
        return routeRequest;
    }
}
