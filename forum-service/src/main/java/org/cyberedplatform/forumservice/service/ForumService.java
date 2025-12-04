package org.cyberedplatform.forumservice.service;

import org.cyberedplatform.forumservice.client.UserServiceClient;
import org.cyberedplatform.forumservice.dto.PostDTO;
import org.cyberedplatform.forumservice.dto.UserDTO;
import org.cyberedplatform.forumservice.model.Comment;
import org.cyberedplatform.forumservice.model.Post;
import org.cyberedplatform.forumservice.repository.CommentRepository;
import org.cyberedplatform.forumservice.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ForumService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    /**
     * Create a new post - demonstrates inter-service communication
     */
    @Transactional
    public Post createPost(Long userId, String title, String content) {
        System.out.println("\n📝 Creating post for user " + userId);

        // Validate user exists (call User Service)
        UserDTO user = userServiceClient.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        System.out.println("✅ User validated: " + user.getUsername());

        // Create post
        Post post = new Post(userId, user.getUsername(), title, content);
        Post savedPost = postRepository.save(post);
        System.out.println("✅ Post created: #" + savedPost.getId());
        System.out.println("📢 Event: POST_CREATED (Post ID: " + savedPost.getId() + ")\n");

        return savedPost;
    }

    /**
     * Add comment to a post
     */
    @Transactional
    public Comment addComment(Long userId, Long postId, String content) {
        System.out.println("\n💬 Adding comment to post " + postId + " by user " + userId);

        // Validate user exists
        UserDTO user = userServiceClient.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Validate post exists
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        // Create comment
        Comment comment = new Comment(userId, user.getUsername(), content, post);
        Comment savedComment = commentRepository.save(comment);
        System.out.println("✅ Comment added: #" + savedComment.getId());
        System.out.println("📢 Event: COMMENT_ADDED (Post ID: " + postId + ", Comment ID: " + savedComment.getId() + ")\n");

        return savedComment;
    }

    /**
     * Get all active posts
     */
    public List<PostDTO> getAllPosts() {
        return postRepository.findByStatus("ACTIVE").stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get post by ID with comments
     */
    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    /**
     * Get posts by user ID
     */
    public List<PostDTO> getPostsByUserId(Long userId) {
        return postRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get comments for a post
     */
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    /**
     * Moderate post (admin only)
     */
    @Transactional
    public Post moderatePost(Long postId, String status) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setStatus(status);
        return postRepository.save(post);
    }

    /**
     * Delete post
     */
    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setStatus("DELETED");
        postRepository.save(post);
    }

    /**
     * Convert Post to PostDTO
     */
    private PostDTO convertToDTO(Post post) {
        return new PostDTO(
                post.getId(),
                post.getUserId(),
                post.getUsername(),
                post.getTitle(),
                post.getContent(),
                post.getStatus(),
                post.getCreatedAt(),
                post.getComments().size()
        );
    }
}
