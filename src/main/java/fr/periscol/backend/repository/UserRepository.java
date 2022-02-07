package fr.periscol.backend.repository;

import fr.periscol.backend.domain.User;
import fr.periscol.backend.service.dto.UserDTO;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
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
public interface UserRepository extends JpaRepository<User, String> {
    @Query(
        value = "select distinct userCustom from User userCustom left join fetch userCustom.roles",
        countQuery = "select count(distinct userCustom) from User userCustom"
    )
    Page<User> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct userCustom from User userCustom left join fetch userCustom.roles")
    List<User> findAllWithEagerRelationships();

    @Query("select userCustom from User userCustom left join fetch userCustom.roles where userCustom.name =:name")
    Optional<User> findOneWithEagerRelationships(@Param("name") String name);


    @EntityGraph(attributePaths = "roles")
    Optional<User> findOneWithRolesByLogin(String login);
}
