package fr.periscol.backend.repository;

import fr.periscol.backend.domain.RoleRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RoleRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoleRoleRepository extends JpaRepository<RoleRole, Long> {}
