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

import ch.sebug.controlrouteoptimizer.models.Shelter;
import ch.sebug.controlrouteoptimizer.repositories.ShelterRepository;


@Scope(value = "session")
@Component(value = "shelterController")
@ELBeanName(value = "shelterController")
@Join(path = "/shelter", to = "/shelter-form.jsf")
public class ShelterController {

    private final ShelterRepository shelterRepository;

    public ShelterController(ShelterRepository shelterRepository) {
        this.shelterRepository = shelterRepository;
    }

    private Shelter shelter = new Shelter();

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
}
