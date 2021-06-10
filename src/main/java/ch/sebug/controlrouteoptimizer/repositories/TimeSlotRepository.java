package ch.sebug.controlrouteoptimizer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.sebug.controlrouteoptimizer.models.TimeSlot;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    
}
