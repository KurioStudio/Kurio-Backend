package app.kuriobackend.Controllers;

import app.kuriobackend.Entities.DTO.PostResponse;
import app.kuriobackend.Entities.Model.User;
import app.kuriobackend.Services.PostService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @Mock
    private PostService service;

    @InjectMocks
    private PostController controller;

    @Test
    void findByIdReturnsPostResponse() {
        User user = new User("user-1", "alice", "alice@example.com", "avatar.png", "2026-05-12");
        PostResponse response = new PostResponse(
                "post-1",
                "Titulo",
                "Descripcion",
                new ArrayList<>(),
                new ArrayList<>(),
                3,
                user,
                "oid-1",
                "CC",
                "2026-05-12"
        );
        when(service.findById("post-1")).thenReturn(response);

        var result = controller.findById("post-1");

        assertEquals(200, result.getStatusCode().value());
        assertEquals("post-1", result.getBody().id());
        assertEquals("Titulo", result.getBody().titulo());
        assertEquals("user-1", result.getBody().user().id());
    }
}