package com.servicedesk.controller;

import com.servicedesk.dto.TicketRequestDto;
import com.servicedesk.dto.TicketResponseDto;
import com.servicedesk.entity.Priority;
import com.servicedesk.entity.TicketStatus;
import com.servicedesk.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller exposing HTTP endpoints for ticket operations.
 */
@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Raises a new technical support ticket.
     * Validates payload with {@link Valid}.
     */
    @PostMapping
    public ResponseEntity<TicketResponseDto> createTicket(@Valid @RequestBody TicketRequestDto request) {
        TicketResponseDto createdTicket = ticketService.createTicket(request);
        return new ResponseEntity<>(createdTicket, HttpStatus.CREATED);
    }

    /**
     * Retrieves tickets, with optional filtering by status or priority.
     */
    @GetMapping
    public ResponseEntity<List<TicketResponseDto>> getTickets(
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
     */
    @PatchMapping("/{id}/assign")
    public ResponseEntity<TicketResponseDto> assignTicket(
            @PathVariable Long id,
            @RequestParam Long engineerId) {
        return ResponseEntity.ok(ticketService.assignTicket(id, engineerId));
    }

    /**
     * Updates the status of a ticket.
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<TicketResponseDto> updateTicketStatus(
            @PathVariable Long id,
            @RequestParam TicketStatus status) {
        return ResponseEntity.ok(ticketService.updateTicketStatus(id, status));
    }

    /**
     * Deletes a ticket by ID.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }
}
