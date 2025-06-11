package capsthon.backend.deeplung.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import capsthon.backend.deeplung.domain.dto.request.JoinRequest;
import capsthon.backend.deeplung.domain.dto.request.LoginRequest;
import capsthon.backend.deeplung.domain.dto.response.UserInfoResponse;
import capsthon.backend.deeplung.domain.entity.User;
import capsthon.backend.deeplung.domain.enums.Gender;
import capsthon.backend.deeplung.domain.enums.UserType;
import capsthon.backend.deeplung.jwt.JwtAuthenticationResponse;
import capsthon.backend.deeplung.jwt.JwtTokenProvider;
import capsthon.backend.deeplung.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private UserService userService;

    private JoinRequest joinRequest;
    private LoginRequest loginRequest;
    private User user;

    @BeforeEach
    void setUp() {
        // Setup test data
        joinRequest = new JoinRequest();
        joinRequest.setUserId("testuser");
        joinRequest.setPassword("password123");
        joinRequest.setName("Test User");
        joinRequest.setGender(Gender.MALE);
        joinRequest.setUserType(UserType.NORMAL);
        joinRequest.setBirthYear("1990");
        joinRequest.setIsPrivateInformAgreed(true);

        loginRequest = new LoginRequest();
        loginRequest.setUserId("testuser");
        loginRequest.setPassword("password123");

        user = new User();
        user.setId(1L);
        user.setUserId("testuser");
        user.setPassword("encodedPassword");
        user.setName("Test User");
        user.setGender(Gender.MALE);
        user.setUserType(UserType.NORMAL);
        user.setBirthYear("1990");
        user.setIsPrivateInformAgreed(true);
    }

    @Test
    void createUserShouldSaveNewUser() {
        // Given
        when(userRepository.findByUserId("testuser")).thenReturn(null);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        // When
        userService.createUser(joinRequest);

        // Then
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUserShouldThrowExceptionWhenUserExists() {
        // Given
        when(userRepository.findByUserId("testuser")).thenReturn(user);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(joinRequest);
        });
        assertEquals("이미 존재하는 회원입니다!", exception.getMessage());
    }

    @Test
    void loginShouldReturnToken() {
        // Given
        when(userRepository.findByUserId("testuser")).thenReturn(user);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtTokenProvider.generateToken(user)).thenReturn("testToken");

        // When
        JwtAuthenticationResponse response = userService.login(loginRequest);

        // Then
        assertEquals("testToken", response.getAccessToken());
        assertEquals("Bearer", response.getTokenType());
    }

    @Test
    void loginShouldThrowExceptionWhenPasswordDoesNotMatch() {
        // Given
        when(userRepository.findByUserId("testuser")).thenReturn(user);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.login(loginRequest);
        });
        assertEquals("password가 일치하지 않습니다.", exception.getMessage());
    }

    @Test
    void infoShouldReturnUserInfo() {
        // Given
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userRepository.findByUserId("testuser")).thenReturn(user);

        // When
        UserInfoResponse response = userService.info(userDetails);

        // Then
        assertEquals(1L, response.getId());
        assertEquals("testuser", response.getUserId());
        assertEquals("Test User", response.getName());
        assertEquals(Gender.MALE, response.getGender());
        assertEquals(UserType.NORMAL, response.getUserType());
        assertEquals("1990", response.getBirthYear());
        assertEquals(true, response.getIsPrivateInformAgreed());
    }
}