package com.servicedesk.entity;

/**
 * Represents the urgency and business impact of a support ticket.
 */
public enum Priority {
    /**
     * Minor issue or inquiry with low business impact (e.g., cosmetic UI issue).
     */
    LOW,

    /**
     * Standard technical issue affecting an individual employee (e.g., software reinstall).
     */
    MEDIUM,

    /**
     * High priority issue hindering daily work with no immediate workaround (e.g., laptop crash).
     */
    HIGH,

    /**
     * Critical incident affecting broad operations or production systems (e.g., company VPN down).
     */
    CRITICAL
}
