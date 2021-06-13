package ch.sebug.controlrouteoptimizer.controllers;

import java.util.List;
import java.util.Optional;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.Parameter;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ch.sebug.controlrouteoptimizer.CarConverter;
import ch.sebug.controlrouteoptimizer.TeamConverter;
import ch.sebug.controlrouteoptimizer.models.Car;
import ch.sebug.controlrouteoptimizer.models.CarAssignment;
import ch.sebug.controlrouteoptimizer.models.Team;
import ch.sebug.controlrouteoptimizer.repositories.CarAssignmentRepository;
import ch.sebug.controlrouteoptimizer.repositories.CarRepository;
import ch.sebug.controlrouteoptimizer.repositories.TeamRepository;


@Scope(value = "session")
@Component(value = "carAssignmentController")
@ELBeanName(value = "carAssignmentController")
@Join(path = "/carAssignment", to = "/carAssignment-form.jsf")
public class CarAssignmentController {
    private final CarAssignmentRepository carAssignmentRepository;
    private final CarRepository carRepository;
    private final TeamRepository teamRepository;

    public CarAssignmentController(CarAssignmentRepository carAssignmentRepository,
    CarRepository carRepository,
    TeamRepository teamRepository) {
        this.carAssignmentRepository = carAssignmentRepository;
        this.carRepository = carRepository;
        this.teamRepository = teamRepository;
    }

    private List<Team> teams;

    public List<Team> getTeams() {
        return teams;
    }

    private List<Car> cars;

    public List<Car> getCars() {
        return cars;
    }

    private CarAssignment carAssignment = new CarAssignment();

    @Parameter
    @Deferred
    private String carAssignmentId;

    public String getCarAssignmentId() {
        return carAssignmentId;
    }

    public void setCarAssignmentId(String carAssignmentId) {
        this.carAssignmentId = carAssignmentId;
    }

    @Parameter
    @Deferred
    private String teamId;

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
        if (teamId == null || teamId == "") {
            this.carAssignment.setTeamId(null);
        } else {
            this.carAssignment.setTeamId(Long.parseLong(teamId));
        }
    }

    @Parameter
    @Deferred
    private String carId;

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
        if (carId == null || carId == "") {
            this.carAssignment.setCarId(null);
        } else {
            this.carAssignment.setCarId(Long.parseLong(carId));
        }
    }

    @Deferred
    @RequestAction
    @IgnorePostback
    public void loadData() {
        this.cars = this.carRepository.findAll();
        this.teams = this.teamRepository.findAll();
        if (carAssignmentId == null || carAssignmentId == "") {
            carAssignment = new CarAssignment();
        } else {
            try {
                Optional<CarAssignment> maybeCarAssignment = carAssignmentRepository.findById(Long.parseLong(carAssignmentId));
                if (maybeCarAssignment.isPresent()) {
                    carAssignment = maybeCarAssignment.get();
                } else {
                    carAssignment = new CarAssignment();
                }
            } catch (Exception ex) {
                System.out.println(ex);
                carAssignment = new CarAssignment();
            }
        }
    }

    public String save() {
        carAssignmentRepository.save(carAssignment);
        carAssignment = new CarAssignment();
        return "/carAssignment-list.xhtml?faces-redirect=true";
    }

    public CarAssignment getCarAssignment() {
        return carAssignment;
    }

    public Car getCar() {
        if (this.carAssignment.getCarId() != null) {
            Optional<Car> maybeCar = this.carRepository.findById(this.carAssignment.getCarId());
            return maybeCar.orElse(null);
        }
        return null;
    }

    public void setCar(Car car) {
        if (car == null) {
            this.setCarId(null);
        } else {
            this.setCarId(car.getId().toString());
        }
    }

    public Team getTeam() {
        if (this.carAssignment.getTeamId() != null) {
            Optional<Team> maybeTeam = this.teamRepository.findById(this.carAssignment.getTeamId());
            return maybeTeam.orElse(null);
        }
        return null;
    }

    public void setTeam(Team team) {
        if (team == null) {
            this.setTeamId(null);
        } else {
            this.setTeamId(team.getId().toString());
        }
    }

    public CarConverter getCarConverter() {
        return new CarConverter(cars);
    }

    public TeamConverter getTeamConverter() {
        return new TeamConverter(teams);
    }
}
