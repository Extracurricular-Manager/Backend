package fr.periscol.backend.repository;

import fr.periscol.backend.domain.TimeSlotModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TimeSlotModel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimeSlotModelRepository extends JpaRepository<TimeSlotModel, Long> {}
