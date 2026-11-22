package com.servicedesk.repository;

import com.servicedesk.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA Repository for {@link Comment} entity.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Retrieves all comments on a ticket ordered chronologically from oldest to newest.
     */
    List<Comment> findByTicketIdOrderByCreatedAtAsc(Long ticketId);

    /**
     * Counts comments associated with a specific ticket.
     */
    long countByTicketId(Long ticketId);
}
