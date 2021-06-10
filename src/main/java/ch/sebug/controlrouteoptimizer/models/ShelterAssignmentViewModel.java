package ch.sebug.controlrouteoptimizer.models;

import lombok.Data;

@Data
public class ShelterAssignmentViewModel {
    private Long shelterId;
    private Long teamId;
    private Shelter shelter;
    private String routeLink;
}
