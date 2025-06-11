package capsthon.backend.deeplung.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import capsthon.backend.deeplung.domain.entity.User;
import capsthon.backend.deeplung.domain.enums.Gender;
import capsthon.backend.deeplung.domain.enums.UserType;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByUserIdShouldReturnUser() {
        // Given
        User user = new User();
        user.setUserId("testuser");
        user.setPassword("password123");
        user.setName("Test User");
        user.setGender(Gender.MALE);
        user.setUserType(UserType.NORMAL);
        user.setBirthYear("1990");
        user.setIsPrivateInformAgreed(true);
        
        entityManager.persist(user);
        entityManager.flush();

        // When
        User foundUser = userRepository.findByUserId("testuser");

        // Then
        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUserId());
        assertEquals("Test User", foundUser.getName());
        assertEquals(Gender.MALE, foundUser.getGender());
        assertEquals(UserType.NORMAL, foundUser.getUserType());
        assertEquals("1990", foundUser.getBirthYear());
        assertEquals(true, foundUser.getIsPrivateInformAgreed());
    }

    @Test
    void findByUserIdShouldReturnNullWhenUserNotFound() {
        // When
        User foundUser = userRepository.findByUserId("nonexistentuser");

        // Then
        assertNull(foundUser);
    }
}