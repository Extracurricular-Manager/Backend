package fr.periscol.backend.service.mapper;

import fr.periscol.backend.domain.tarification.Criteria;
import fr.periscol.backend.service.dto.CriteriaDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Criteria} and its DTO {@link CriteriaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CriteriaMapper extends EntityMapper<CriteriaDTO, Criteria> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CriteriaDTO toDtoId(Criteria criteria);
}
