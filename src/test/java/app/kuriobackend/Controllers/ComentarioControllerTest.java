package app.kuriobackend.Controllers;

import app.kuriobackend.Entities.DTO.ComentarioRequest;
import app.kuriobackend.Services.ComentarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ComentarioControllerTest {

    @Mock
    private ComentarioService service;

    @InjectMocks
    private ComentarioController controller;

    @Test
    void guardarReturnsOkWhenServiceSucceeds() {
        when(service.guardar(any())).thenReturn(0);

        var result = controller.guardar(new ComentarioRequest("post-1", "user-1", "Buen post"));

        assertEquals(200, result.getStatusCode().value());
        assertEquals("0", result.getBody());
    }
}