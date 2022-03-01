package fr.periscol.backend.service.mapper.service_model;

import fr.periscol.backend.domain.service_model.PresenceModel;
import fr.periscol.backend.domain.service_model.ServiceMetadata;
import fr.periscol.backend.domain.tarification.Criteria;
import fr.periscol.backend.domain.tarification.CriteriaType;
import fr.periscol.backend.service.dto.service_model.NewPresenceModelDTO;
import fr.periscol.backend.service.dto.service_model.ServiceMetadataDTO;
import fr.periscol.backend.service.mapper.EntityMapper;
import fr.periscol.backend.web.rest.errors.NotFoundAlertException;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ServiceMetadataMapper extends EntityMapper<ServiceMetadataDTO, ServiceMetadata> {


}
