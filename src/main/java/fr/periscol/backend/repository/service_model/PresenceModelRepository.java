package fr.periscol.backend.repository.service_model;

import fr.periscol.backend.domain.service_model.PresenceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PresenceModel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PresenceModelRepository extends JpaRepository<PresenceModel, Long> {}
