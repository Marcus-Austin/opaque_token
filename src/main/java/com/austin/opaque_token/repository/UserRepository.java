package com.austin.opaque_token.repository;

import com.austin.opaque_token.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUsername(String username);

    @Query(
        """
        SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserEntity u
            WHERE com.austin.opaque_token.common.Role.ROLE_ADMIN MEMBER OF u.roles
        """
    )
    boolean isAnyAdminExist();
}
