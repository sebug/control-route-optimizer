package ch.sebug.controlrouteoptimizer.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.Parameter;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ch.sebug.controlrouteoptimizer.models.ResetRequest;
import ch.sebug.controlrouteoptimizer.repositories.ShelterAssignmentRepository;
import ch.sebug.controlrouteoptimizer.repositories.ShelterRepository;
import ch.sebug.controlrouteoptimizer.repositories.TeamRepository;
import ch.sebug.controlrouteoptimizer.repositories.TimeSlotRepository;

@Scope(value = "session")
@Component(value = "resetRequestController")
@ELBeanName(value = "resetRequestController")
@Join(path = "/resetRequest", to = "/resetRequest-form.jsf")
public class ResetRequestController {
    private final ShelterAssignmentRepository shelterAssignmentRepository;
    private final ShelterRepository shelterRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final TeamRepository teamRepository;

    public ResetRequestController(ShelterAssignmentRepository shelterAssignmentRepository,
        ShelterRepository shelterRepository,
        TimeSlotRepository timeSlotRepository,
        TeamRepository teamRepository) {
            this.shelterAssignmentRepository = shelterAssignmentRepository;
            this.shelterRepository = shelterRepository;
            this.timeSlotRepository = timeSlotRepository;
            this.teamRepository = teamRepository;
        }

        private ResetRequest resetRequest = new ResetRequest();
        public ResetRequest getResetRequest() {
            return resetRequest;
        }
    
        public void setMeetingFile(ResetRequest resetRequest) {
            this.resetRequest = resetRequest;
        }

        public String resetData() {
            System.out.println("Todo: reset");
            return "/team-list.xhtml?faces-redirect=true";
        }
}
