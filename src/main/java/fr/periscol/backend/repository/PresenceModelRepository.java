package fr.periscol.backend.repository;

import fr.periscol.backend.domain.PresenceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PresenceModel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PresenceModelRepository extends JpaRepository<PresenceModel, Long> {}
