package ch.sebug.controlrouteoptimizer;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Scope(value = "session")
@Component(value = "teamList")
@ELBeanName(value = "teamList")
@Join(path = "/", to="/team-list.jsf")
public class TeamListController {
    @Autowired
    private TeamRepository teamRepository;

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
