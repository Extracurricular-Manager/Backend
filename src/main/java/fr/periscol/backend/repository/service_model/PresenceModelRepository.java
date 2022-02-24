package fr.periscol.backend.repository.service_model;

import fr.periscol.backend.domain.service_model.PeriodModel;
import fr.periscol.backend.domain.service_model.PresenceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the PresenceModel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PresenceModelRepository extends JpaRepository<PresenceModel, Long> {

    @Query("select p from PresenceModel p where p.serviceId = :serviceId AND p.date = :date")
    List<PresenceModel> findAllByDate(Long serviceId, LocalDate date);

    @Query("select p from PresenceModel p where p.child.id = :idChild AND p.serviceId = :serviceId AND p.date = :date")
    Optional<PresenceModel> findOneByDate(Long serviceId, Long idChild, LocalDate date);

}
