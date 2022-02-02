package fr.periscol.backend.service.mapper;

import fr.periscol.backend.domain.Modifier;
import fr.periscol.backend.service.dto.ModifierDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Modifier} and its DTO {@link ModifierDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ModifierMapper extends EntityMapper<ModifierDTO, Modifier> {}
