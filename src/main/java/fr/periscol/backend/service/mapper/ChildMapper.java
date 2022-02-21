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
        ClassroomMapper.class, FamilyMapper.class, GradeLevelMapper.class, DietMapper.class, TarifBaseMapper.class, FacturationMapper.class,
    }
)
public interface ChildMapper extends EntityMapper<ChildDTO, Child> {
    @Mapping(target = "classroom", source = "classroom", qualifiedByName = "id")
    @Mapping(target = "adelphie", source = "adelphie", qualifiedByName = "id")
    @Mapping(target = "gradeLevel", source = "gradeLevel", qualifiedByName = "id")
    @Mapping(target = "diets", source = "diets", qualifiedByName = "idSet")
    @Mapping(target = "tarif", source = "tarif", qualifiedByName = "id")
    @Mapping(target = "facturation", source = "facturation", qualifiedByName = "id")
    @Mapping(target = "birthday", source = "birthday")
    ChildDTO toDto(Child s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ChildDTO toDtoId(Child child);

    @Mapping(target = "removeDiet", ignore = true)
    Child toEntity(ChildDTO childDTO);
}
