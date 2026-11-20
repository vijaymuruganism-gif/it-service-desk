package com.servicedesk.exception;

/**
 * Thrown when a requested resource (Ticket, User, Category) does not exist in the database.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
