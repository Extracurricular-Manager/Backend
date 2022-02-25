package fr.periscol.backend.repository;

import fr.periscol.backend.domain.MonthPaid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Facturation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonthPaidRepository extends JpaRepository<MonthPaid, Long> {}
