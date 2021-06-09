package ch.sebug.controlrouteoptimizer.controllers;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ch.sebug.controlrouteoptimizer.models.Shelter;
import ch.sebug.controlrouteoptimizer.repositories.ShelterRepository;

import java.util.List;

@Scope(value = "session")
@Component(value = "shelterList")
@ELBeanName(value = "shelterList")
@Join(path = "/shelterList", to="/shelter-list.jsf")
public class ShelterListController {
    @Autowired
    private ShelterRepository shelterRepository;

    private List<Shelter> shelters;

    @Deferred
    @RequestAction
    @IgnorePostback
    public void loadData() {
        shelters = shelterRepository.findAll();
    }

    public List<Shelter> getShelters() {
        return shelters;
    }
}
