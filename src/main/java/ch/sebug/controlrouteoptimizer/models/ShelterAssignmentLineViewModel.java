package ch.sebug.controlrouteoptimizer.models;

import java.util.List;

import lombok.Data;

@Data
public class ShelterAssignmentLineViewModel {
    private Long timeSlotId;
    private List<ShelterAssignmentViewModel> assignments;
}
