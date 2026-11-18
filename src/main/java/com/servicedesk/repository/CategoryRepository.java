package com.servicedesk.repository;

import com.servicedesk.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA Repository for {@link Category} entity.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Finds a category by its unique name (e.g., 'Hardware', 'Network').
     */
    Optional<Category> findByName(String name);

    /**
     * Checks if a category with the specified name already exists.
     */
    boolean existsByName(String name);
}
