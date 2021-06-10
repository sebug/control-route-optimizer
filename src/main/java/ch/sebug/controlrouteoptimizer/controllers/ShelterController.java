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

import ch.sebug.controlrouteoptimizer.TimeSlotConverter;
import ch.sebug.controlrouteoptimizer.models.Shelter;
import ch.sebug.controlrouteoptimizer.models.TimeSlot;
import ch.sebug.controlrouteoptimizer.repositories.ShelterRepository;
import ch.sebug.controlrouteoptimizer.repositories.TimeSlotRepository;


@Scope(value = "session")
@Component(value = "shelterController")
@ELBeanName(value = "shelterController")
@Join(path = "/shelter", to = "/shelter-form.jsf")
public class ShelterController {

    private final ShelterRepository shelterRepository;
    private final TimeSlotRepository timeSlotRepository;

    public ShelterController(ShelterRepository shelterRepository,
        TimeSlotRepository timeSlotRepository) {
        this.shelterRepository = shelterRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    private Shelter shelter = new Shelter();

    private List<TimeSlot> timeSlots;

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public TimeSlot getTimeSlot() {
        if (shelter.getId() == null) {
            return null;
        }
        Optional<TimeSlot> maybeTimeSlot = this.timeSlotRepository.findById(shelter.getId());
        return maybeTimeSlot.orElse(null);
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        if (timeSlot == null) {
            shelter.setId(null);
        } else {
            shelter.setTimeSlotId(timeSlot.getId());
        }
    }

    @Parameter
    @Deferred
    private String shelterId;

    public String getShelterId() {
        return shelterId;
    }

    public void setShelterId(String shelterId) {
        this.shelterId = shelterId;
    }

    @Deferred
    @RequestAction
    @IgnorePostback
    public void loadData() {
        timeSlots = timeSlotRepository.findAll();
        System.out.println("Shelter Id is " + shelterId);
        if (shelterId == null || shelterId == "") {
            shelter = new Shelter();
        } else {
            try {
                Optional<Shelter> maybeShelter = shelterRepository.findById(Long.parseLong(shelterId));
                if (maybeShelter.isPresent()) {
                    shelter = maybeShelter.get();
                } else {
                    shelter = new Shelter();
                }
            } catch (Exception ex) {
                System.out.println(ex);
                shelter = new Shelter();
            }
        }
    }

    public String save() {
        shelterRepository.save(shelter);
        shelter = new Shelter();
        return "/shelter-list.xhtml?faces-redirect=true";
    }

    public Shelter getShelter() {
        return shelter;
    }

    public TimeSlotConverter getTimeSlotConverter() {
        return new TimeSlotConverter(timeSlots);
    }
}
