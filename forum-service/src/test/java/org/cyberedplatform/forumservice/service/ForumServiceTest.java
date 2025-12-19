package org.cyberedplatform.forumservice.service;

import org.cyberedplatform.forumservice.client.UserServiceClient;
import org.cyberedplatform.forumservice.dto.UserDTO;
import org.cyberedplatform.forumservice.model.Comment;
import org.cyberedplatform.forumservice.model.Post;
import org.cyberedplatform.forumservice.repository.CommentRepository;
import org.cyberedplatform.forumservice.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForumServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private ForumService forumService;

    private UserDTO testUser;
    private Post testPost;

    @BeforeEach
    void setUp() {
        testUser = new UserDTO();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testPost = new Post();
        testPost.setId(1L);
        testPost.setUserId(1L);
        testPost.setUsername("testuser");
        testPost.setTitle("Test Post");
        testPost.setContent("Test Content");
        testPost.setStatus("ACTIVE");
    }

    @Test
    void createPost_Success() {
        // Arrange
        when(userServiceClient.getUserById(1L)).thenReturn(testUser);
        when(postRepository.save(any(Post.class))).thenReturn(testPost);

        // Act
        Post result = forumService.createPost(1L, "Test Post", "Test Content");

        // Assert
        assertNotNull(result);
        assertEquals("Test Post", result.getTitle());
        assertEquals("testuser", result.getUsername());
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void createPost_UserNotFound_ThrowsException() {
        // Arrange
        when(userServiceClient.getUserById(1L)).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            forumService.createPost(1L, "Test Post", "Test Content");
        });
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void addComment_Success() {
        // Arrange
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setUserId(1L);
        comment.setUsername("testuser");
        comment.setContent("Test Comment");
        
        when(userServiceClient.getUserById(1L)).thenReturn(testUser);
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // Act
        Comment result = forumService.addComment(1L, 1L, "Test Comment");

        // Assert
        assertNotNull(result);
        assertEquals("Test Comment", result.getContent());
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void addComment_PostNotFound_ThrowsException() {
        // Arrange
        when(userServiceClient.getUserById(1L)).thenReturn(testUser);
        when(postRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            forumService.addComment(1L, 99L, "Test Comment");
        });
        assertEquals("Post not found", exception.getMessage());
    }

    @Test
    void moderatePost_Success() {
        // Arrange
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(postRepository.save(any(Post.class))).thenReturn(testPost);

        // Act
        Post result = forumService.moderatePost(1L, "MODERATED");

        // Assert
        assertNotNull(result);
        assertEquals("MODERATED", result.getStatus());
        verify(postRepository).save(testPost);
    }

    @Test
    void deletePost_Success() {
        // Arrange
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));
        when(postRepository.save(any(Post.class))).thenReturn(testPost);

        // Act
        forumService.deletePost(1L);

        // Assert
        verify(postRepository).save(testPost);
        assertEquals("DELETED", testPost.getStatus());
    }

    @Test
    void getPostById_Found() {
        // Arrange
        when(postRepository.findById(1L)).thenReturn(Optional.of(testPost));

        // Act
        Optional<Post> result = forumService.getPostById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Post", result.get().getTitle());
    }
}
