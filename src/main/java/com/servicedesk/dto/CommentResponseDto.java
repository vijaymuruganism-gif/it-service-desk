package com.servicedesk.dto;

import com.servicedesk.entity.Role;
import java.time.LocalDateTime;

/**
 * Response payload representing a comment or note posted on a support ticket.
 */
public class CommentResponseDto {

    private Long id;
    private String content;
    private Long ticketId;
    private Long userId;
    private String username;
    private Role userRole;
    private LocalDateTime createdAt;

    public CommentResponseDto() {
    }

    public CommentResponseDto(Long id, String content, Long ticketId, Long userId, String username, Role userRole, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.ticketId = ticketId;
        this.userId = userId;
        this.username = username;
        this.userRole = userRole;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getUserRole() {
        return userRole;
    }

    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
