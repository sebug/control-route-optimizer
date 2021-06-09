package ch.sebug.controlrouteoptimizer.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.sebug.controlrouteoptimizer.models.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
    
}
