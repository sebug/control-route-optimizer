package ch.sebug.controlrouteoptimizer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.sebug.controlrouteoptimizer.models.CarAssignment;

/**
 * The reason we don't model this as deep loaded M to N (or even have one car at most
 * per team)? We later on may want to specify preferences / weight (if you can be in either
 * car 1 or car 2). Also I'm not well versed in JPA yet :-)
 */
public interface CarAssignmentRepository extends JpaRepository<CarAssignment, Long> {
    
}
