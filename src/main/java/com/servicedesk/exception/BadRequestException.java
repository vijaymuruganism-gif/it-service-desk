package com.servicedesk.exception;

/**
 * Thrown when a business rule or validation constraint is violated
 * (e.g., trying to assign a ticket to a non-engineer user).
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
