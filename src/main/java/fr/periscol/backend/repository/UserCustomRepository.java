package fr.periscol.backend.repository;

import fr.periscol.backend.domain.UserCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data SQL repository for the UserCustom entity.
 */
@Repository
public interface UserCustomRepository extends JpaRepository<UserCustom, Long> {
    @Query(
        value = "select distinct userCustom from UserCustom userCustom left join fetch userCustom.roles",
        countQuery = "select count(distinct userCustom) from UserCustom userCustom"
    )
    Page<UserCustom> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct userCustom from UserCustom userCustom left join fetch userCustom.roles")
    List<UserCustom> findAllWithEagerRelationships();

    @Query("select userCustom from UserCustom userCustom left join fetch userCustom.roles where userCustom.id =:id")
    Optional<UserCustom> findOneWithEagerRelationships(@Param("id") Long id);
}
