package app.kuriobackend.Controllers;

import app.kuriobackend.Entities.DTO.FollowRequest;
import app.kuriobackend.Services.FollowService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowControllerTest {

    @Mock
    private FollowService service;

    @InjectMocks
    private FollowController controller;

    @Test
    void isFollowingReturnsOkWhenRelationshipExists() {
        when(service.isFollowing(any())).thenReturn(true);

        var result = controller.isFollowing(new FollowRequest("user-1", "user-2"));

        assertEquals(200, result.getStatusCode().value());
        assertEquals("0", result.getBody());
    }
}