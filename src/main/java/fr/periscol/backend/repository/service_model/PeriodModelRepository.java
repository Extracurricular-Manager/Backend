package fr.periscol.backend.repository.service_model;

import fr.periscol.backend.domain.service_model.PeriodModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the TimeSlotModel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeriodModelRepository extends JpaRepository<PeriodModel, Long> {


    @Query("select p from PeriodModel p where p.serviceId = :serviceId AND (p.begin BETWEEN :startDate AND :endDate)")
    List<PeriodModel> findAllByDate(Long serviceId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("select p from PeriodModel p where p.child.id = :idChild AND p.serviceId = :serviceId AND p.begin BETWEEN :startDate AND :endDate")
    Optional<PeriodModel> findOneByDate(Long serviceId, Long idChild, LocalDateTime startDate, LocalDateTime endDate);

}
