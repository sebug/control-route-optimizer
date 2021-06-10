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

import ch.sebug.controlrouteoptimizer.models.Team;
import ch.sebug.controlrouteoptimizer.repositories.TeamRepository;


@Scope(value = "session")
@Component(value = "teamController")
@ELBeanName(value = "teamController")
@Join(path = "/team", to = "/team-form.jsf")
public class TeamController {

    private final TeamRepository teamRepository;

    public TeamController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Parameter
    @Deferred
    private String teamId;

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    private Team team;

    @Deferred
    @RequestAction
    @IgnorePostback
    public void loadData() {
        System.out.println("Team Id is " + teamId);
        if (teamId == null || teamId == "") {
            team = new Team();
        } else {
            try {
                Optional<Team> maybeTimeSlot = teamRepository.findById(Long.parseLong(teamId));
                if (maybeTimeSlot.isPresent()) {
                    team = maybeTimeSlot.get();
                } else {
                    team = new Team();
                }
            } catch (Exception ex) {
                System.out.println(ex);
                team = new Team();
            }
        }
    }

    public String save() {
        teamRepository.save(team);
        team = new Team();
        return "/team-list.xhtml?faces-redirect=true";
    }

    public Team getTeam() {
        return team;
    }
}