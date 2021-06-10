package ch.sebug.controlrouteoptimizer.models;

import java.util.Date;
import java.util.Map;

import lombok.Data;

@Data
public class ShelterAssignmentLineViewModel {
    private Long timeSlotId;
    private Date date;
    private Map<Long, ShelterAssignmentViewModel> assignments;
}
