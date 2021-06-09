package ch.sebug.controlrouteoptimizer.controllers;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ch.sebug.controlrouteoptimizer.models.Team;
import ch.sebug.controlrouteoptimizer.repositories.TeamRepository;

import java.util.List;

@Scope(value = "session")
@Component(value = "teamList")
@ELBeanName(value = "teamList")
@Join(path = "/", to="/team-list.jsf")
public class TeamListController {

    private final TeamRepository teamRepository;

    public TeamListController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    private List<Team> teams;

    @Deferred
    @RequestAction
    @IgnorePostback
    public void loadData() {
        teams = teamRepository.findAll();
    }

    public List<Team> getTeams() {
        return teams;
    }
}
