package fr.periscol.backend.repository.service_model;

import fr.periscol.backend.domain.service_model.PeriodModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TimeSlotModel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeriodRepository extends JpaRepository<PeriodModel, Long> {}
