package com.servicedesk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request payload for posting a resolution note or comment on a ticket.
 */
public class CommentRequestDto {

    @NotBlank(message = "Comment content cannot be blank.")
    @Size(min = 2, max = 2000, message = "Comment must be between 2 and 2000 characters.")
    private String content;

    public CommentRequestDto() {
    }

    public CommentRequestDto(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
