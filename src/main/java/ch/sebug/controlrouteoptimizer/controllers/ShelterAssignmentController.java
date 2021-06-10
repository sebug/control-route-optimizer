package ch.sebug.controlrouteoptimizer.controllers;

import java.util.ArrayList;
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
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ch.sebug.controlrouteoptimizer.ShelterConverter;
import ch.sebug.controlrouteoptimizer.models.MapResult;
import ch.sebug.controlrouteoptimizer.models.RouteRequest;
import ch.sebug.controlrouteoptimizer.models.Shelter;
import ch.sebug.controlrouteoptimizer.models.ShelterAssignment;
import ch.sebug.controlrouteoptimizer.models.ShelterAssignmentLineViewModel;
import ch.sebug.controlrouteoptimizer.models.ShelterAssignmentViewModel;
import ch.sebug.controlrouteoptimizer.models.Team;
import ch.sebug.controlrouteoptimizer.models.TimeSlot;
import ch.sebug.controlrouteoptimizer.repositories.ShelterAssignmentRepository;
import ch.sebug.controlrouteoptimizer.repositories.ShelterRepository;
import ch.sebug.controlrouteoptimizer.repositories.TeamRepository;
import ch.sebug.controlrouteoptimizer.repositories.TimeSlotRepository;
import ch.sebug.controlrouteoptimizer.services.MapService;

@Scope(value = "session")
@Component(value = "shelterAssignmentController")
@ELBeanName(value = "shelterAssignmentController")
@Join(path = "/shelterAssignment", to = "/shelterAssignment-form.jsf")
public class ShelterAssignmentController {
    private final ShelterRepository shelterRepository;
    private final TeamRepository teamRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final ShelterAssignmentRepository shelterAssignmentRepository;
    private final MapService mapService;

    public ShelterAssignmentController(ShelterRepository shelterRepository,
    ShelterAssignmentRepository shelterAssignmentRepository,
    TeamRepository teamRepository,
    TimeSlotRepository timeSlotRepository,
    MapService mapService) {
        this.shelterRepository = shelterRepository;
        this.shelterAssignmentRepository = shelterAssignmentRepository;
        this.teamRepository = teamRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.mapService = mapService;
    }

    private List<Team> teams;

    public List<Team> getTeams() {
        return teams;
    }

    private List<TimeSlot> timeSlots;

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    private List<ShelterAssignmentLineViewModel> lineViewModels;
    public List<ShelterAssignmentLineViewModel> getLineViewModels() {
        return lineViewModels;
    }

    private DataTable scheduleTable;

    public DataTable getScheduleTable() {
        return scheduleTable;
    }

    public void setScheduleTable(DataTable scheduleTable) {
        this.scheduleTable = scheduleTable;
    }

    @Deferred
    @RequestAction
    @IgnorePostback
    public void loadData() {
        List<ShelterAssignment> shelterAssignments = shelterAssignmentRepository.findAll();
        teams = teamRepository.findAll();
        timeSlots = timeSlotRepository.findAll();

        lineViewModels = calculateShelterAssignmentLineViewModels(shelterAssignments);

        System.out.println("Shelter assignment lines size is " + lineViewModels.size());

        initScheduleTable();
    }

    private void initScheduleTable() {
        scheduleTable = new DataTable();
        scheduleTable.setVar("shelterAssignmentLine");

        FacesContext fc = FacesContext.getCurrentInstance();
        Application application = fc.getApplication();
        ExpressionFactory ef = application.getExpressionFactory();
        ELContext elc = fc.getELContext();

        ValueExpression tableValueExpression = ef.createValueExpression(elc, "#{shelterAssignmentController.lineViewModels}", Object.class);
        scheduleTable.setValueExpression("value", tableValueExpression);

        Column dateAndTimeColumn = new Column();
        dateAndTimeColumn.setHeaderText("Date and Time");
        ValueExpression valueExpression = ef.createValueExpression(elc, "#{shelterAssignmentLine.date}", Object.class);
        HtmlOutputText output = (HtmlOutputText)application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        output.setValueExpression("value", valueExpression);
        dateAndTimeColumn.getChildren().add(output);

        scheduleTable.getChildren().add(dateAndTimeColumn);

        for (Team team : teams) {
            Column teamColumn = new Column();
            teamColumn.setHeaderText(team.getName());
            scheduleTable.getChildren().add(teamColumn);
        }

        System.out.println(scheduleTable);
    }

    private List<ShelterAssignmentLineViewModel> calculateShelterAssignmentLineViewModels(List<ShelterAssignment> shelterAssignments) {
        ArrayList<ShelterAssignmentLineViewModel> result = new ArrayList<ShelterAssignmentLineViewModel>();

        for (TimeSlot timeSlot : timeSlots) {
            ShelterAssignmentLineViewModel line = new ShelterAssignmentLineViewModel();
            line.setTimeSlotId(timeSlot.getId());
            line.setDate(timeSlot.getStartDate());
            ArrayList<ShelterAssignmentViewModel> assignmentViewModels = new ArrayList<ShelterAssignmentViewModel>();
            for (Team team : teams) {
                ShelterAssignmentViewModel assignmentViewModel = new ShelterAssignmentViewModel();
                assignmentViewModel.setTeamId(team.getId());
            }
            line.setAssignments(assignmentViewModels);
            result.add(line);
        }

        return result;
    }
}
