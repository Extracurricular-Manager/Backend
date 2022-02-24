package fr.periscol.backend.service.mapper;

import fr.periscol.backend.domain.Child;
import fr.periscol.backend.service.dto.ChildDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Child} and its DTO {@link ChildDTO}.
 */
@Mapper(
    componentModel = "spring",
    uses = {
        ClassroomMapper.class, FamilyMapper.class, GradeLevelMapper.class, DietMapper.class, FacturationMapper.class,
    }
)
public interface ChildMapper extends EntityMapper<ChildDTO, Child> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ChildDTO toDtoId(Child child);

    @Mapping(target = "removeDiet", ignore = true)
    Child toEntity(ChildDTO childDTO);

}
