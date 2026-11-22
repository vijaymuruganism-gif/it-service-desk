package com.servicedesk.controller;

import com.servicedesk.dto.CommentRequestDto;
import com.servicedesk.dto.CommentResponseDto;
import com.servicedesk.dto.TicketRequestDto;
import com.servicedesk.dto.TicketResponseDto;
import com.servicedesk.entity.Priority;
import com.servicedesk.entity.TicketStatus;
import com.servicedesk.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * REST Controller for ticket management, role-based operations, and resolution notes.
 */
@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Raises a new support ticket.
     * Accessible by any authenticated user (EMPLOYEE, SUPPORT_ENGINEER, ADMIN).
     */
    @PostMapping
    public ResponseEntity<TicketResponseDto> createTicket(
            @Valid @RequestBody TicketRequestDto request,
            Principal principal) {
        String currentUsername = principal != null ? principal.getName() : null;
        TicketResponseDto createdTicket = ticketService.createTicket(request, currentUsername);
        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }

    /**
     * Retrieves tickets relevant to the currently logged in user (Role-Based Access):
     * - Employees see only their raised tickets.
     * - Support Engineers see tickets assigned to them.
     * - Admins see all tickets.
     */
    @GetMapping("/my-tickets")
    public ResponseEntity<List<TicketResponseDto>> getMyTickets(Principal principal) {
        return ResponseEntity.ok(ticketService.getMyTickets(principal.getName()));
    }

    /**
     * Retrieves all support tickets across the company.
     * Restricted to Support Engineers and Admins.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPPORT_ENGINEER', 'ADMIN')")
    public ResponseEntity<List<TicketResponseDto>> getAllTickets(
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) Priority priority) {

        if (status != null) {
            return ResponseEntity.ok(ticketService.getTicketsByStatus(status));
        }
        if (priority != null) {
            return ResponseEntity.ok(ticketService.getTicketsByPriority(priority));
        }
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    /**
     * Retrieves a single ticket by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDto> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    /**
     * Assigns a ticket to an IT support engineer.
     * Restricted to Admins.
     */
    @PatchMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TicketResponseDto> assignTicket(
            @PathVariable Long id,
            @RequestParam Long engineerId) {
        return ResponseEntity.ok(ticketService.assignTicket(id, engineerId));
    }

    /**
     * Updates ticket status (e.g. IN_PROGRESS, RESOLVED, CLOSED).
     * Restricted to Support Engineers and Admins.
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('SUPPORT_ENGINEER', 'ADMIN')")
    public ResponseEntity<TicketResponseDto> updateTicketStatus(
            @PathVariable Long id,
            @RequestParam TicketStatus status) {
        return ResponseEntity.ok(ticketService.updateTicketStatus(id, status));
    }

    /**
     * Deletes a ticket by ID.
     * Restricted to Admins only.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Adds a comment or resolution note to a ticket.
     * Any authenticated user can comment on a ticket.
     */
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentResponseDto> addComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequestDto request,
            Principal principal) {
        CommentResponseDto comment = ticketService.addComment(id, request, principal.getName());
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    /**
     * Retrieves all comments / resolution notes posted on a ticket.
     */
    @GetMapping("/{id}/comments")
    public ResponseEntity<List<CommentResponseDto>> getCommentsForTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getCommentsForTicket(id));
    }
}
