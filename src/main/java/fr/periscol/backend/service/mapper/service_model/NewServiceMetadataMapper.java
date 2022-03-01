package fr.periscol.backend.service.mapper.service_model;

import fr.periscol.backend.domain.service_model.ServiceMetadata;
import fr.periscol.backend.domain.tarification.Criteria;
import fr.periscol.backend.domain.tarification.CriteriaType;
import fr.periscol.backend.service.dto.service_model.NewServiceMetadataDTO;
import fr.periscol.backend.service.mapper.EntityMapper;
import fr.periscol.backend.web.rest.errors.NotFoundAlertException;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Mapper for the entity {@link ServiceMetadata} and its DTO {@link NewServiceMetadataDTO}.
 */
@Mapper(componentModel = "spring")
public interface NewServiceMetadataMapper extends EntityMapper<NewServiceMetadataDTO, ServiceMetadata> {

    @Mapping(target = "criterias", expression = "java(toCriteria(criteriaList))")
    ServiceMetadata toEntity(NewServiceMetadataDTO s, @Context List<Integer> criteriaList);


    default Set<Criteria> toCriteria(List<Integer> listIndexes){
        Set<Criteria> criteriaList = new HashSet<>();
        for (Integer index : listIndexes) {
            var critere = CriteriaType.getFromId(index);
            if (critere.isEmpty()) {
                throw new NotFoundAlertException("Criteria with id %d is not found".formatted(index));
            }
            criteriaList.add(critere.get());
        }
        return criteriaList;
    }

}
