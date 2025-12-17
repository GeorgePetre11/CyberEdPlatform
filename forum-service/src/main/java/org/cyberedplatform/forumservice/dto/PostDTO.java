package org.cyberedplatform.forumservice.dto;

import java.time.Instant;

public class PostDTO {
    private Long id;
    private Long userId;
    private String username;
    private String title;
    private String content;
    private String status;
    private Instant createdAt;
    private int commentCount;

    public PostDTO() {}

    public PostDTO(Long id, Long userId, String username, String title, String content, 
                   String status, Instant createdAt, int commentCount) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.title = title;
        this.content = content;
        this.status = status;
        this.createdAt = createdAt;
        this.commentCount = commentCount;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
