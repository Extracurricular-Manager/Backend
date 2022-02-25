package fr.periscol.backend.service.mapper;

import fr.periscol.backend.domain.MonthPaid;
import fr.periscol.backend.service.dto.MonthPaidDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MonthPaid} and its DTO {@link MonthPaidDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MonthPaidMapper extends EntityMapper<MonthPaidDTO, MonthPaid> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MonthPaidDTO toDtoId(MonthPaid monthPaid);
}
