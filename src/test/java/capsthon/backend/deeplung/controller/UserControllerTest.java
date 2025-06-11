package capsthon.backend.deeplung.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import capsthon.backend.deeplung.domain.dto.request.JoinRequest;
import capsthon.backend.deeplung.domain.enums.Gender;
import capsthon.backend.deeplung.domain.enums.UserType;
import capsthon.backend.deeplung.jwt.JwtTokenProvider;
import capsthon.backend.deeplung.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserController userController;

    private JoinRequest joinRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();

        joinRequest = new JoinRequest();
        joinRequest.setUserId("testuser");
        joinRequest.setPassword("password123");
        joinRequest.setName("Test User");
        joinRequest.setGender(Gender.MALE);
        joinRequest.setUserType(UserType.NORMAL);
        joinRequest.setBirthYear("1990");
        joinRequest.setIsPrivateInformAgreed(true);
    }

    @Test
    @WithMockUser
    void joinShouldReturnSuccess() throws Exception {
        // Given
        doNothing().when(userService).createUser(any(JoinRequest.class));

        // When & Then
        mockMvc.perform(post("/api/v1/user/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(joinRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("회원가입 성공"));
    }

    @Test
    @WithMockUser
    void joinShouldReturnErrorWhenDuplicateUserId() throws Exception {
        // Given
        String errorMessage = "이미 존재하는 회원입니다!";
        doThrow(new RuntimeException(errorMessage)).when(userService).createUser(any(JoinRequest.class));

        // When & Then
        mockMvc.perform(post("/api/v1/user/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(joinRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value(errorMessage));
    }
}
