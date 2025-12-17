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

    @Transactional
    public Post createPost(Long userId, String title, String content) {
        UserDTO user = userServiceClient.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Post post = new Post(userId, user.getUsername(), title, content);
        Post savedPost = postRepository.save(post);

        return savedPost;
    }

    @Transactional
    public Comment addComment(Long userId, Long postId, String content) {
        UserDTO user = userServiceClient.getUserById(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        Comment comment = new Comment(userId, user.getUsername(), content, post);
        Comment savedComment = commentRepository.save(comment);

        return savedComment;
    }

    public List<PostDTO> getAllPosts() {
        return postRepository.findByStatus("ACTIVE").stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public List<PostDTO> getPostsByUserId(Long userId) {
        return postRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    @Transactional
    public Post moderatePost(Long postId, String status) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setStatus(status);
        return postRepository.save(post);
    }

    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setStatus("DELETED");
        postRepository.save(post);
    }

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
