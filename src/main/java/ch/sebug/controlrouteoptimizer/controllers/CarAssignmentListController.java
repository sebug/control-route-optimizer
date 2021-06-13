package ch.sebug.controlrouteoptimizer.controllers;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ch.sebug.controlrouteoptimizer.models.CarAssignment;
import ch.sebug.controlrouteoptimizer.models.Shelter;
import ch.sebug.controlrouteoptimizer.repositories.CarAssignmentRepository;
import ch.sebug.controlrouteoptimizer.repositories.ShelterRepository;

import java.util.List;

@Scope(value = "session")
@Component(value = "carAssignmentList")
@ELBeanName(value = "carAssignmentList")
@Join(path = "/carAssignmentList", to="/carAssignment-list.jsf")
public class CarAssignmentListController {
    private final CarAssignmentRepository carAssignmentRepository;

    public CarAssignmentListController(CarAssignmentRepository carAssignmentRepository) {
        this.carAssignmentRepository = carAssignmentRepository;
    }

    private List<CarAssignment> carAssignments;

    @Deferred
    @RequestAction
    @IgnorePostback
    public void loadData() {
        carAssignments = carAssignmentRepository.findAll();
    }

    public List<CarAssignment> getCarAssignments() {
        return carAssignments;
    }
}
