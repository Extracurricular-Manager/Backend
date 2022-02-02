package fr.periscol.backend.service.mapper;

import fr.periscol.backend.domain.Facturation;
import fr.periscol.backend.service.dto.FacturationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Facturation} and its DTO {@link FacturationDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FacturationMapper extends EntityMapper<FacturationDTO, Facturation> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FacturationDTO toDtoId(Facturation facturation);
}
