package ch.sebug.controlrouteoptimizer.controllers;

import org.ocpsoft.rewrite.annotation.Join;
import org.ocpsoft.rewrite.annotation.RequestAction;
import org.ocpsoft.rewrite.el.ELBeanName;
import org.ocpsoft.rewrite.faces.annotation.Deferred;
import org.ocpsoft.rewrite.faces.annotation.IgnorePostback;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ch.sebug.controlrouteoptimizer.models.Car;
import ch.sebug.controlrouteoptimizer.repositories.CarRepository;

import java.util.List;

@Scope(value = "session")
@Component(value = "carList")
@ELBeanName(value = "carList")
@Join(path = "/carList", to="/car-list.jsf")
public class CarListController {
    private final CarRepository carRepository;

    public CarListController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    private List<Car> cars;

    @Deferred
    @RequestAction
    @IgnorePostback
    public void loadData() {
        cars = carRepository.findAll();
    }

    public List<Car> getCars() {
        return cars;
    }
}
