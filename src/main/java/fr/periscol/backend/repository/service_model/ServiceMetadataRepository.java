package fr.periscol.backend.repository.service_model;

import fr.periscol.backend.domain.User;
import fr.periscol.backend.domain.service_model.ServiceMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data SQL repository for the ServiceMetadata entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServiceMetadataRepository extends JpaRepository<ServiceMetadata, Long> {

    @Query("select service from ServiceMetadata service where service.name = :name")
    Optional<ServiceMetadata> findOneByName(@Param("name") String name);

}
