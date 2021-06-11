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

import ch.sebug.controlrouteoptimizer.models.Car;
import ch.sebug.controlrouteoptimizer.repositories.CarRepository;


@Scope(value = "session")
@Component(value = "carController")
@ELBeanName(value = "carController")
@Join(path = "/car", to = "/car-form.jsf")
public class CarController {
    private final CarRepository carRepository;

    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Parameter
    @Deferred
    private String carId;

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    private Car car;

    @Deferred
    @RequestAction
    @IgnorePostback
    public void loadData() {
        if (carId == null || carId == "") {
            car = new Car();
        } else {
            try {
                Optional<Car> maybeCar = carRepository.findById(Long.parseLong(carId));
                if (maybeCar.isPresent()) {
                    car = maybeCar.get();
                } else {
                    car = new Car();
                }
            } catch (Exception ex) {
                System.out.println(ex);
                car = new Car();
            }
        }
    }

    public String save() {
        carRepository.save(car);
        car = new Car();
        return "/car-list.xhtml?faces-redirect=true";
    }

    public Car getCar() {
        return car;
    }
}
