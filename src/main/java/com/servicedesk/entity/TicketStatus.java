package com.servicedesk.entity;

/**
 * Represents the lifecycle stages of an IT support ticket.
 */
public enum TicketStatus {
    /**
     * Ticket has been created by an employee and is awaiting triage.
     */
    OPEN,

    /**
     * Ticket has been assigned to an IT support engineer.
     */
    ASSIGNED,

    /**
     * Engineer is actively working on resolving the ticket.
     */
    IN_PROGRESS,

    /**
     * Solution or workaround has been applied by the engineer.
     */
    RESOLVED,

    /**
     * Resolution has been confirmed by employee or SLA timer, ticket is closed.
     */
    CLOSED
}
