package ch.sebug.controlrouteoptimizer.models;

import java.util.List;

import lombok.Data;

@Data
public class BingMapsResourceSet<T> {
    List<T> resources;
}
