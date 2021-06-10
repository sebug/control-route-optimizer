package ch.sebug.controlrouteoptimizer.models;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ShelterAssignmentLineViewModel {
    private Long timeSlotId;
    private Date date;
    private List<ShelterAssignmentViewModel> assignments;
}
