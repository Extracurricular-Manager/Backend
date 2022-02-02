package fr.periscol.backend.repository;

import fr.periscol.backend.domain.TarifBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TarifBase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TarifBaseRepository extends JpaRepository<TarifBase, Long> {}
