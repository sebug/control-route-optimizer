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
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.column.Column;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.link.Link;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Example;
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
        teams = teamRepository.findAll();
        timeSlots = timeSlotRepository.findAll();

        lineViewModels = calculateShelterAssignmentLineViewModels();

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

            ValueExpression routeLinkExpression = ef.createValueExpression(elc, "#{shelterAssignmentLine.assignments[" +
            team.getId() +
            "].routeLink}",Object.class);
            ValueExpression routeLinkRenderedExpression = ef.createValueExpression(elc, "#{not empty shelterAssignmentLine.assignments[" +
            team.getId() +
            "].routeLink}", Object.class);
            Link linkOutput = (Link)application.createComponent(Link.COMPONENT_TYPE);
            linkOutput.setValue("Route");
            linkOutput.setValueExpression("rendered", routeLinkRenderedExpression);
            linkOutput.setValueExpression("href", routeLinkExpression);
            teamColumn.getChildren().add(linkOutput);

            ValueExpression mapExpression = ef.createValueExpression(elc, "#{shelterAssignmentLine.assignments[" +
            team.getId() +
            "].shelter.shortAddressString}", Object.class);
            HtmlOutputText mapOutput = (HtmlOutputText)application.createComponent(HtmlOutputText.COMPONENT_TYPE);
            mapOutput.setValueExpression("value", mapExpression);
            teamColumn.getChildren().add(mapOutput);

            

            scheduleTable.getChildren().add(teamColumn);
        }

        System.out.println(scheduleTable);
    }

    private List<ShelterAssignmentLineViewModel> calculateShelterAssignmentLineViewModels() {
        ArrayList<ShelterAssignmentLineViewModel> result = new ArrayList<ShelterAssignmentLineViewModel>();

        HashMap<Long, Shelter> previousShelters = new HashMap<Long, Shelter>();
        for (TimeSlot timeSlot : timeSlots) {
            ShelterAssignmentLineViewModel line = new ShelterAssignmentLineViewModel();
            line.setTimeSlotId(timeSlot.getId());
            line.setDate(timeSlot.getStartDate());
            HashMap<Long, ShelterAssignmentViewModel> assignmentViewModels = new HashMap<Long, ShelterAssignmentViewModel>();
            
            Shelter exampleShelter = new Shelter();
            exampleShelter.setTimeSlotId(timeSlot.getId());
            List<Shelter> timeSlotShelters = this.shelterRepository.findAll(Example.of(exampleShelter));
            System.out.println("Found " + timeSlotShelters.size() + " candidate shelters");

            List<Shelter> unusedTimeSlotShelters = timeSlotShelters.stream().collect(java.util.stream.Collectors.toList());
            for (Team team : teams) {
                ShelterAssignmentViewModel assignmentViewModel = new ShelterAssignmentViewModel();
                assignmentViewModel.setTeamId(team.getId());

                for (Shelter candidateShelter : timeSlotShelters) {
                    ShelterAssignment exampleShelterAssignment = new ShelterAssignment();
                    exampleShelterAssignment.setTeamId(team.getId());
                    exampleShelterAssignment.setShelterId(candidateShelter.getId());
                    Optional<ShelterAssignment> foundShelterAssignment = this.shelterAssignmentRepository.findOne(Example.of(exampleShelterAssignment));
                    if (foundShelterAssignment.isPresent()) {
                        assignmentViewModel.setShelterId(candidateShelter.getId());
                        assignmentViewModel.setShelter(candidateShelter);
                        previousShelters.put(team.getId(), candidateShelter);
                        unusedTimeSlotShelters.remove(candidateShelter);
                     }
                }

                assignmentViewModels.put(team.getId(), assignmentViewModel);
            }

            List<ShelterAssignmentViewModel> assignments =
                shuffle(assignmentViewModels.values().stream().collect(java.util.stream.Collectors.toList()));
            for (ShelterAssignmentViewModel assignmentViewModel : assignments) {
                Shelter previousShelter = previousShelters.getOrDefault(assignmentViewModel.getTeamId(), null);

                if (assignmentViewModel.getShelterId() == null) {
                    Shelter chosenShelter = determineNextShelter(previousShelter, unusedTimeSlotShelters);
                    if (chosenShelter != null) {
                        assignmentViewModel.setShelterId(chosenShelter.getId());
                        assignmentViewModel.setShelter(chosenShelter);
                        previousShelters.put(assignmentViewModel.getTeamId(), chosenShelter);
                        unusedTimeSlotShelters.remove(chosenShelter);
                    }
                }

                if (previousShelter != null && assignmentViewModel.getShelterId() != null) {
                    assignmentViewModel.setRouteLink("/routeRequest?fromShelterId=" +
                        previousShelter.getId() + "&toShelterId=" +
                        assignmentViewModel.getShelterId());
                }
            }
            line.setAssignments(assignmentViewModels);
            result.add(line);
        }

        return result;
    }

    private Shelter determineNextShelter(Shelter previousShelter, List<Shelter> candidateShelters) {
        if (candidateShelters == null || candidateShelters.isEmpty()) {
            return null;
        }
        if (previousShelter == null) {
            // select one at random
            int idx = (int)(Math.random() * candidateShelters.size());
            Shelter chosen = candidateShelters.get(idx);
            return chosen;
        }
        MapResult closestMapResult = null;
        Shelter closestShelter = null;
        for (Shelter candidateShelter : candidateShelters) {
            RouteRequest routeRequest = new RouteRequest();
            routeRequest.setFromShelter(previousShelter);
            routeRequest.setToShelter(candidateShelter);
            MapResult checkResult = this.mapService.CalculateRoute(routeRequest);
            if (checkResult != null) {
                if (closestMapResult == null) {
                    closestMapResult = checkResult;
                    closestShelter = candidateShelter;
                } else {
                    if (checkResult.getMinutes() * 60 + checkResult.getSeconds() <
                        closestMapResult.getMinutes() * 60 + closestMapResult.getSeconds()) {
                            closestMapResult = checkResult;
                            closestShelter = candidateShelter;
                        }
                }
            }
        }
        return closestShelter;
    }

    private List<ShelterAssignmentViewModel> shuffle(List<ShelterAssignmentViewModel> original) {
        if (original == null || original.isEmpty()) {
            return original;
        }
        int n = original.size();
        for (int i = 0; i < n; i += 1) {
            int source = i;
            int destination = i + ((int)(Math.random() * (n - i)));
            if (source != destination) {
                System.out.println(source + " -> " + destination);
                ShelterAssignmentViewModel destinationAssignment =
                    original.get(destination);
                original.set(destination, original.get(source));
                original.set(source, destinationAssignment);
            } else {
                System.out.println(source);
            }
        }
        return original;
    }
}
