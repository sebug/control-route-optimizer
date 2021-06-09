package ch.sebug.controlrouteoptimizer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.sebug.controlrouteoptimizer.models.Shelter;

public interface ShelterRepository extends JpaRepository<Shelter, Long>  {
    
}
