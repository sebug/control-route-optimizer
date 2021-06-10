package ch.sebug.controlrouteoptimizer.controllers;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ch.sebug.controlrouteoptimizer.models.TimeSlot;
import ch.sebug.controlrouteoptimizer.repositories.TimeSlotRepository;

import java.util.List;

@Scope(value = "session")
@Component(value = "timeSlotList")
@ELBeanName(value = "timeSlotList")
@Join(path = "/timeSlotList", to="/timeSlot-list.jsf")
public class TimeSlotListController {
    private final TimeSlotRepository timeSlotRepository;

    public TimeSlotListController(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    private List<TimeSlot> timeSlots;

    @Deferred
    @RequestAction
    @IgnorePostback
    public void loadData() {
        timeSlots = timeSlotRepository.findAll();
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }
}
