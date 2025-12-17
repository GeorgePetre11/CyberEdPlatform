package org.cyberedplatform.forumservice.controller;

import org.cyberedplatform.forumservice.dto.CreateCommentRequest;
import org.cyberedplatform.forumservice.dto.CreatePostRequest;
import org.cyberedplatform.forumservice.dto.PostDTO;
import org.cyberedplatform.forumservice.model.Comment;
import org.cyberedplatform.forumservice.model.Post;
import org.cyberedplatform.forumservice.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/forum")
public class ForumController {

    @Autowired
    private ForumService forumService;

    @PostMapping("/posts")
    public ResponseEntity<?> createPost(@RequestBody CreatePostRequest request) {
        try {
            Post post = forumService.createPost(
                    request.getUserId(),
                    request.getTitle(),
                    request.getContent()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(post);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/comments")
    public ResponseEntity<?> addComment(@RequestBody CreateCommentRequest request) {
        try {
            Comment comment = forumService.addComment(
                    request.getUserId(),
                    request.getPostId(),
                    request.getContent()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(comment);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        return ResponseEntity.ok(forumService.getAllPosts());
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        return forumService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping("/posts/user/{userId}")
    public ResponseEntity<List<PostDTO>> getPostsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(forumService.getPostsByUserId(userId));
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Long postId) {
        return ResponseEntity.ok(forumService.getCommentsByPostId(postId));
    }

    @PutMapping("/posts/{id}/moderate")
    public ResponseEntity<?> moderatePost(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String status = request.get("status");
            Post post = forumService.moderatePost(id, status);
            return ResponseEntity.ok(post);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        try {
            forumService.deletePost(id);
            return ResponseEntity.ok(Map.of("message", "Post deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "forum-service"));
    }
}
