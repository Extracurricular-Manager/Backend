package fr.periscol.backend.repository;

import fr.periscol.backend.domain.ServiceMetadata;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ServiceMetadata entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceMetadataRepository extends JpaRepository<ServiceMetadata, Long> {}
