package org.cyberedplatform.forumservice.dto;

public class CreateCommentRequest {
    private Long userId;
    private Long postId;
    private String content;

    public CreateCommentRequest() {}

    public CreateCommentRequest(Long userId, Long postId, String content) {
        this.userId = userId;
        this.postId = postId;
        this.content = content;
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
