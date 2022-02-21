package fr.periscol.backend.repository;

import fr.periscol.backend.domain.MetaDataBackend;
import fr.periscol.backend.domain.Modifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MetaDataBackendRepository extends JpaRepository<MetaDataBackend, UUID> {
}
