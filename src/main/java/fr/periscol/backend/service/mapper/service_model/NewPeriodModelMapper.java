package fr.periscol.backend.service.mapper.service_model;

import fr.periscol.backend.domain.service_model.PeriodModel;
import fr.periscol.backend.service.dto.service_model.NewPeriodModelDTO;
import fr.periscol.backend.service.dto.service_model.PeriodModelDTO;
import fr.periscol.backend.service.mapper.ChildMapper;
import fr.periscol.backend.service.mapper.EntityMapper;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link PeriodModel} and its DTO {@link NewPeriodModelDTO}.
 */
@Mapper(componentModel = "spring", uses = { ChildMapper.class })
public interface NewPeriodModelMapper extends EntityMapper<NewPeriodModelDTO, PeriodModel> {

    @Mapping(target = "startBilling", source = "timeOfStartBilling")
    @Mapping(target = "begin", source = "timeOfArrival")
    @Mapping(target = "end", source = "timeOfDeparture")
    @Mapping(target = "serviceId", expression = "java(serviceId)")
    PeriodModel toEntity(NewPeriodModelDTO s, @Context Long serviceId);
}
