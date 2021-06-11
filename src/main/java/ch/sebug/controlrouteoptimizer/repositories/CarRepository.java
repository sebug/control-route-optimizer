package ch.sebug.controlrouteoptimizer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.sebug.controlrouteoptimizer.models.Car;

public interface CarRepository extends JpaRepository<Car, Long> {
    
}
