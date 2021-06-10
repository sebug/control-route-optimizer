package ch.sebug.controlrouteoptimizer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.sebug.controlrouteoptimizer.models.ShelterAssignment;

public interface ShelterAssignmentRepository extends JpaRepository<ShelterAssignment, Long> {
    
}
