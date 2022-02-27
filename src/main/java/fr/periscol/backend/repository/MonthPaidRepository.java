package fr.periscol.backend.repository;

import fr.periscol.backend.domain.MonthPaid;
import fr.periscol.backend.domain.service_model.PeriodModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the Facturation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonthPaidRepository extends JpaRepository<MonthPaid, Long> {


    @Query("select m from MonthPaid m where m.child.id = :idChild AND m.date = :date")
    Optional<MonthPaid> findOneByChildAndDate(Long idChild, LocalDateTime date);

    @Query("select m from MonthPaid m where m.child.id = :idChild")
    Optional<MonthPaid> findAllByChild(Long idChild);

}
