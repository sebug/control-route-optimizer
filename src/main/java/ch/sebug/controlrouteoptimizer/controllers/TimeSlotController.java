package ch.sebug.controlrouteoptimizer.controllers;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ch.sebug.controlrouteoptimizer.models.TimeSlot;
import ch.sebug.controlrouteoptimizer.repositories.TimeSlotRepository;


@Scope(value = "session")
@Component(value = "timeSlotController")
@ELBeanName(value = "timeSlotController")
@Join(path = "/timeSlot", to = "/timeSlot-form.jsf")
public class TimeSlotController {
    private final TimeSlotRepository timeSlotRepository;

    public TimeSlotController(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    private TimeSlot timeSlot = new TimeSlot();

    public String save() {
        timeSlotRepository.save(timeSlot);
        timeSlot = new TimeSlot();
        return "/timeSlot-list.xhtml?faces-redirect=true";
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }
}
