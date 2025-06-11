package capsthon.backend.deeplung.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import capsthon.backend.deeplung.domain.entity.User;
import capsthon.backend.deeplung.domain.enums.Gender;
import capsthon.backend.deeplung.domain.enums.UserType;
import capsthon.backend.deeplung.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    private User user;

    @BeforeEach
    void setUp() {
        // Setup test data
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
    void findByUserIdShouldReturnUser() {
        // Given
        when(userRepository.findByUserId("testuser")).thenReturn(user);

        // When
        User foundUser = customUserDetailsService.findByUserId("testuser");

        // Then
        assertEquals(1L, foundUser.getId());
        assertEquals("testuser", foundUser.getUserId());
        assertEquals("Test User", foundUser.getName());
        assertEquals(Gender.MALE, foundUser.getGender());
        assertEquals(UserType.NORMAL, foundUser.getUserType());
        assertEquals("1990", foundUser.getBirthYear());
        assertEquals(true, foundUser.getIsPrivateInformAgreed());
    }

    @Test
    void findByUserIdShouldReturnNullWhenUserNotFound() {
        // Given
        when(userRepository.findByUserId("nonexistentuser")).thenReturn(null);

        // When
        User foundUser = customUserDetailsService.findByUserId("nonexistentuser");

        // Then
        assertNull(foundUser);
    }
}