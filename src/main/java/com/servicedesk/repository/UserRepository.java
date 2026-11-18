package com.servicedesk.repository;

import com.servicedesk.entity.Role;
import com.servicedesk.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA Repository for {@link User} entity.
 *
 * <p>By extending {@link JpaRepository}, Spring Data automatically provides CRUD
 * operations (save, findById, findAll, deleteById, count) without writing any SQL.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their unique username.
     * Query derived automatically: SELECT * FROM users WHERE username = ?
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a user by their unique email.
     * Query derived automatically: SELECT * FROM users WHERE email = ?
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if a user already exists with the given username.
     */
    boolean existsByUsername(String username);

    /**
     * Checks if a user already exists with the given email.
     */
    boolean existsByEmail(String email);

    /**
     * Finds all users belonging to a specific role (e.g. all SUPPORT_ENGINEERs).
     */
    List<User> findByRole(Role role);
}
