package ch.sebug.controlrouteoptimizer.controllers;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ch.sebug.controlrouteoptimizer.models.Team;
import ch.sebug.controlrouteoptimizer.repositories.TeamRepository;


@Scope(value = "session")
@Component(value = "teamController")
@ELBeanName(value = "teamController")
@Join(path = "/team", to = "/team-form.jsf")
public class TeamController {
    @Autowired
    private TeamRepository teamRepository;

    private Team team = new Team();

    public String save() {
        teamRepository.save(team);
        team = new Team();
        return "/team-list.xhtml?faces-redirect=true";
    }

    public Team getTeam() {
        return team;
    }
}