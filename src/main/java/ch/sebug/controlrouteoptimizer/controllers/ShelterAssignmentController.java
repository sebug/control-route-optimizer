package ch.sebug.controlrouteoptimizer.controllers;

import java.util.List;
import java.util.Optional;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.Parameter;
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
import ch.sebug.controlrouteoptimizer.models.ShelterAssignment;
import ch.sebug.controlrouteoptimizer.repositories.ShelterAssignmentRepository;
import ch.sebug.controlrouteoptimizer.repositories.ShelterRepository;
import ch.sebug.controlrouteoptimizer.repositories.TeamRepository;
import ch.sebug.controlrouteoptimizer.services.MapService;

@Scope(value = "session")
@Component(value = "shelterAssignmentController")
@ELBeanName(value = "shelterAssignmentController")
@Join(path = "/shelterAssignment", to = "/shelterAssignment-form.jsf")
public class ShelterAssignmentController {
    private final ShelterRepository shelterRepository;
    private final TeamRepository teamRepository;
    private final ShelterAssignmentRepository shelterAssignmentRepository;
    private final MapService mapService;

    public ShelterAssignmentController(ShelterRepository shelterRepository,
    ShelterAssignmentRepository shelterAssignmentRepository,
    TeamRepository teamRepository,
    MapService mapService) {
        this.shelterRepository = shelterRepository;
        this.shelterAssignmentRepository = shelterAssignmentRepository;
        this.teamRepository = teamRepository;
        this.mapService = mapService;
    }

    @Deferred
    @RequestAction
    @IgnorePostback
    public void loadData() {
        List<ShelterAssignment> shelterAssignments = shelterAssignmentRepository.findAll();

        System.out.println("Shelter assignments size is " + shelterAssignments.size());
    }
}
