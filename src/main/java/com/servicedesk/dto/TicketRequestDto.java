package com.servicedesk.dto;

import com.servicedesk.entity.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for creating or updating a support ticket.
 * Validates incoming client request data before reaching the business layer.
 */
public class TicketRequestDto {

    @NotBlank(message = "Ticket title is required and cannot be blank.")
    @Size(min = 5, max = 150, message = "Ticket title must be between 5 and 150 characters.")
    private String title;

    @NotBlank(message = "Ticket description is required.")
    @Size(min = 10, message = "Please provide a detailed description (at least 10 characters).")
    private String description;

    @NotNull(message = "Priority is required (LOW, MEDIUM, HIGH, CRITICAL).")
    private Priority priority;

    @NotNull(message = "Category ID is required.")
    private Long categoryId;

    @NotNull(message = "Creator Employee User ID is required.")
    private Long createdById;

    public TicketRequestDto() {
    }

    public TicketRequestDto(String title, String description, Priority priority, Long categoryId, Long createdById) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.categoryId = categoryId;
        this.createdById = createdById;
    }

    // Getters and Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }
}
