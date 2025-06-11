package capsthon.backend.deeplung.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class DefaultControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private defaultController defaultController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(defaultController).build();
    }

    @Test
    void defaultAPIShouldReturnHelloWorld() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/default"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World!!"));
    }
}