package fr.periscol.backend.service.mapper;

import fr.periscol.backend.domain.TarifBase;
import fr.periscol.backend.service.dto.TarifBaseDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TarifBase} and its DTO {@link TarifBaseDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TarifBaseMapper extends EntityMapper<TarifBaseDTO, TarifBase> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TarifBaseDTO toDtoId(TarifBase tarifBase);
}
