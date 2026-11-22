package com.servicedesk.service;

import com.servicedesk.dto.CommentRequestDto;
import com.servicedesk.dto.CommentResponseDto;
import com.servicedesk.dto.TicketRequestDto;
import com.servicedesk.dto.TicketResponseDto;
import com.servicedesk.entity.Priority;
import com.servicedesk.entity.TicketStatus;

import java.util.List;

/**
 * Service contract defining business operations for ticket management and comments.
 */
public interface TicketService {

    /**
     * Raises a new support ticket in the system.
     */
    TicketResponseDto createTicket(TicketRequestDto request, String currentUsername);

    /**
     * Retrieves all support tickets (Admin / General view).
     */
    List<TicketResponseDto> getAllTickets();

    /**
     * Retrieves tickets relevant to the currently logged in user (RBAC):
     * - Employees see only their raised tickets.
     * - Engineers see tickets assigned to them.
     * - Admins see all tickets across the organization.
     */
    List<TicketResponseDto> getMyTickets(String currentUsername);

    /**
     * Retrieves a single ticket by its ID.
     */
    TicketResponseDto getTicketById(Long id);

    /**
     * Filters tickets by their lifecycle status (e.g. OPEN, IN_PROGRESS).
     */
    List<TicketResponseDto> getTicketsByStatus(TicketStatus status);

    /**
     * Filters tickets by priority level.
     */
    List<TicketResponseDto> getTicketsByPriority(Priority priority);

    /**
     * Assigns a ticket to an IT support engineer and transitions status to ASSIGNED.
     */
    TicketResponseDto assignTicket(Long ticketId, Long engineerId);

    /**
     * Updates the lifecycle status of a ticket (e.g. IN_PROGRESS, RESOLVED, CLOSED).
     */
    TicketResponseDto updateTicketStatus(Long ticketId, TicketStatus newStatus);

    /**
     * Deletes a ticket by its ID (Admin only).
     */
    void deleteTicket(Long id);

    /**
     * Adds a comment or resolution note to a ticket.
     */
    CommentResponseDto addComment(Long ticketId, CommentRequestDto request, String currentUsername);

    /**
     * Retrieves all comments posted on a ticket in chronological order.
     */
    List<CommentResponseDto> getCommentsForTicket(Long ticketId);
}
