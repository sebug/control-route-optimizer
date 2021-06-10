package ch.sebug.controlrouteoptimizer.controllers;

import java.util.Optional;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.Parameter;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
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

    @Parameter
    @Deferred
    private String timeSlotId;

    public String getTimeSlotId() {
        return timeSlotId;
    }

    public void setTimeSlotId(String timeSlotId) {
        this.timeSlotId = timeSlotId;
    }

    public TimeSlotController(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    private TimeSlot timeSlot;

    @Deferred
    @RequestAction
    @IgnorePostback
    public void loadData() {
        System.out.println("Time slot Id is " + timeSlotId);
        if (timeSlotId == null || timeSlotId == "") {
            timeSlot = new TimeSlot();
        } else {
            try {
                Optional<TimeSlot> maybeTimeSlot = timeSlotRepository.findById(Long.parseLong(timeSlotId));
                if (maybeTimeSlot.isPresent()) {
                    timeSlot = maybeTimeSlot.get();
                } else {
                    timeSlot = new TimeSlot();
                }
            } catch (Exception ex) {
                System.out.println(ex);
                timeSlot = new TimeSlot();
            }
        }
    }

    public String save() {
        timeSlotRepository.save(timeSlot);
        timeSlot = new TimeSlot();
        return "/timeSlot-list.xhtml?faces-redirect=true";
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }
}
