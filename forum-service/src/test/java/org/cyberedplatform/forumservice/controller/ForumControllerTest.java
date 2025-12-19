package org.cyberedplatform.forumservice.controller;

import org.cyberedplatform.forumservice.dto.PostDTO;
import org.cyberedplatform.forumservice.model.Comment;
import org.cyberedplatform.forumservice.model.Post;
import org.cyberedplatform.forumservice.service.ForumService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ForumController.class)
class ForumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ForumService forumService;

    @Test
    void createPost_Success() throws Exception {
        // Arrange
        Post post = new Post(1L, "testuser", "Test Post", "Test Content");
        post.setId(1L);
        when(forumService.createPost(anyLong(), anyString(), anyString())).thenReturn(post);

        // Act & Assert
        mockMvc.perform(post("/api/forum/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":1,\"title\":\"Test Post\",\"content\":\"Test Content\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Post"))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void addComment_Success() throws Exception {
        // Arrange
        Post post = new Post(1L, "testuser", "Test Post", "Test Content");
        Comment comment = new Comment(1L, "testuser", "Test Comment", post);
        comment.setId(1L);
        when(forumService.addComment(anyLong(), anyLong(), anyString())).thenReturn(comment);

        // Act & Assert
        mockMvc.perform(post("/api/forum/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":1,\"postId\":1,\"content\":\"Test Comment\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Test Comment"));
    }

    @Test
    void getAllPosts_ReturnsListOfPosts() throws Exception {
        // Arrange
        PostDTO post1 = new PostDTO(1L, 1L, "user1", "Post 1", "Content 1", "ACTIVE", Instant.now(), 0);
        PostDTO post2 = new PostDTO(2L, 2L, "user2", "Post 2", "Content 2", "ACTIVE", Instant.now(), 0);
        when(forumService.getAllPosts()).thenReturn(Arrays.asList(post1, post2));

        // Act & Assert
        mockMvc.perform(get("/api/forum/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Post 1"));
    }

    @Test
    void getPostById_Found() throws Exception {
        // Arrange
        Post post = new Post(1L, "testuser", "Test Post", "Test Content");
        post.setId(1L);
        when(forumService.getPostById(1L)).thenReturn(Optional.of(post));

        // Act & Assert
        mockMvc.perform(get("/api/forum/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Post"));
    }

    @Test
    void getPostById_NotFound() throws Exception {
        // Arrange
        when(forumService.getPostById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/forum/posts/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void health_ReturnsUp() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/forum/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.service").value("forum-service"));
    }
}
