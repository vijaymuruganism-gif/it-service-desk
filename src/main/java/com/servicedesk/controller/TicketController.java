package com.servicedesk.controller;

import com.servicedesk.dto.CommentRequestDto;
import com.servicedesk.dto.CommentResponseDto;
import com.servicedesk.dto.TicketRequestDto;
import com.servicedesk.dto.TicketResponseDto;
import com.servicedesk.entity.Priority;
import com.servicedesk.entity.TicketStatus;
import com.servicedesk.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Tickets", description = "Endpoints for ticket creation, triage, engineer assignment, and resolution notes")
@SecurityRequirement(name = "Bearer Authentication")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    @Operation(summary = "Raise a new support ticket", description = "Accessible by any authenticated user (EMPLOYEE, SUPPORT_ENGINEER, ADMIN).")
    public ResponseEntity<TicketResponseDto> createTicket(
            @Valid @RequestBody TicketRequestDto request,
            Principal principal) {
        String currentUsername = principal != null ? principal.getName() : null;
        TicketResponseDto createdTicket = ticketService.createTicket(request, currentUsername);
        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }

    @GetMapping("/my-tickets")
    @Operation(summary = "Get current user's role-scoped tickets", description = "Employees see their raised tickets; Engineers see assigned tickets; Admins see all.")
    public ResponseEntity<List<TicketResponseDto>> getMyTickets(Principal principal) {
        return ResponseEntity.ok(ticketService.getMyTickets(principal.getName()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPPORT_ENGINEER', 'ADMIN')")
    @Operation(summary = "Get all tickets across organization", description = "Restricted to Support Engineers and Admins.")
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

    @GetMapping("/{id}")
    @Operation(summary = "Get a ticket by its ID")
    public ResponseEntity<TicketResponseDto> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @PatchMapping("/{id}/assign")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Assign a ticket to an engineer", description = "Restricted to Admins.")
    public ResponseEntity<TicketResponseDto> assignTicket(
            @PathVariable Long id,
            @RequestParam Long engineerId) {
        return ResponseEntity.ok(ticketService.assignTicket(id, engineerId));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('SUPPORT_ENGINEER', 'ADMIN')")
    @Operation(summary = "Update ticket status", description = "Restricted to Support Engineers and Admins.")
    public ResponseEntity<TicketResponseDto> updateTicketStatus(
            @PathVariable Long id,
            @RequestParam TicketStatus status) {
        return ResponseEntity.ok(ticketService.updateTicketStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a ticket", description = "Restricted to Admins only.")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/comments")
    @Operation(summary = "Post a comment or resolution note on a ticket")
    public ResponseEntity<CommentResponseDto> addComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequestDto request,
            Principal principal) {
        CommentResponseDto comment = ticketService.addComment(id, request, principal.getName());
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/comments")
    @Operation(summary = "Get all comments / resolution notes for a ticket")
    public ResponseEntity<List<CommentResponseDto>> getCommentsForTicket(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getCommentsForTicket(id));
    }
}
