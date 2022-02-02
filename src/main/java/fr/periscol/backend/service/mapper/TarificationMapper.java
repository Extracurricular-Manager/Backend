package fr.periscol.backend.service.mapper;

import fr.periscol.backend.domain.Tarification;
import fr.periscol.backend.service.dto.TarificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tarification} and its DTO {@link TarificationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TarificationMapper extends EntityMapper<TarificationDTO, Tarification> {}
