package com.servicedesk.entity;

/**
 * Defines system roles for role-based access control (RBAC).
 */
public enum Role {
    /**
     * Standard employee who raises technical support tickets.
     */
    EMPLOYEE,

    /**
     * Technical support engineer who is assigned to resolve tickets.
     */
    SUPPORT_ENGINEER,

    /**
     * Administrator with privileges to manage users, categories, and system settings.
     */
    ADMIN
}
