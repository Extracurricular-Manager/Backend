package fr.periscol.backend.repository;

import fr.periscol.backend.domain.Modifier;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Modifier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModifierRepository extends JpaRepository<Modifier, Long> {}
