package app.kuriobackend.Controllers;

import app.kuriobackend.Entities.DTO.UserResponse;
import app.kuriobackend.Services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;

    @Test
    void findByIdReturnsUserResponse() {
        UserResponse response = new UserResponse("user-1", "alice", "alice@example.com", "avatar.png", "2026-05-12");
        when(service.findById("user-1")).thenReturn(response);

        var result = controller.findById("user-1");

        assertEquals(200, result.getStatusCode().value());
        assertEquals("user-1", result.getBody().id());
        assertEquals("alice", result.getBody().username());
        assertEquals("alice@example.com", result.getBody().email());
    }
}