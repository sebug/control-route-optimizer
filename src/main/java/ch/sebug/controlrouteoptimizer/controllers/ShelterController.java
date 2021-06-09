package ch.sebug.controlrouteoptimizer.controllers;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ch.sebug.controlrouteoptimizer.models.Shelter;
import ch.sebug.controlrouteoptimizer.repositories.ShelterRepository;


@Scope(value = "session")
@Component(value = "shelterController")
@ELBeanName(value = "shelterController")
@Join(path = "/shelter", to = "/shelter-form.jsf")
public class ShelterController {
    @Autowired
    private ShelterRepository shelterRepository;

    private Shelter shelter = new Shelter();

    public String save() {
        shelterRepository.save(shelter);
        shelter = new Shelter();
        return "/shelter-list.xhtml?faces-redirect=true";
    }

    public Shelter getShelter() {
        return shelter;
    }
}
