package capsthon.backend.deeplung.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import capsthon.backend.deeplung.domain.dto.request.PaperweightRequest;
import capsthon.backend.deeplung.domain.dto.response.PaperweightDetailResponse;
import capsthon.backend.deeplung.domain.dto.response.PaperweightResponse;
import capsthon.backend.deeplung.domain.entity.Paperweight;
import capsthon.backend.deeplung.domain.entity.User;
import capsthon.backend.deeplung.domain.enums.Gender;
import capsthon.backend.deeplung.domain.enums.PaperweightType;
import capsthon.backend.deeplung.domain.enums.RiskLevel;
import capsthon.backend.deeplung.domain.enums.UserType;
import capsthon.backend.deeplung.repository.PaperweightRepository;
import capsthon.backend.deeplung.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class PaperweightServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PaperweightRepository paperweightRepository;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private PaperweightService paperweightService;

    private User user;
    private Paperweight paperweight;
    private PaperweightRequest paperweightRequest;

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

        paperweight = new Paperweight();
        paperweight.setId(1L);
        paperweight.setRiskLevel(RiskLevel.HIGH);
        paperweight.setCreatedAt(LocalDateTime.now());
        paperweight.setPaperweightType(PaperweightType.NORMAL);
        paperweight.setUser(user);
        paperweight.setAge(45);
        paperweight.setGender(1); // Male: 1, Female: 0
        paperweight.setAirPollution(1);
        paperweight.setAlcoholUse(1);
        paperweight.setDustAllergy(1);
        paperweight.setOccuPationalHazards(1);
        paperweight.setGeneticRisk(1);
        paperweight.setChronicLungDisease(1);
        paperweight.setBalancedDiet(1);
        paperweight.setObesity(1);
        paperweight.setSmoking(1);
        paperweight.setPassiveSmoker(1);
        paperweight.setChestPain(1);
        paperweight.setCoughingOfBlood(1);
        paperweight.setFatigue(1);
        paperweight.setWeightLoss(1);
        paperweight.setShortnessOfBreath(1);
        paperweight.setWheezing(1);
        paperweight.setSwallowingDifficulty(1);
        paperweight.setClubbingOfFingerNails(1);
        paperweight.setFrequentCold(1);
        paperweight.setDryCough(1);
        paperweight.setSnoring(1);

        paperweightRequest = new PaperweightRequest();
        paperweightRequest.setAge(45);
        paperweightRequest.setGender(1); // Male: 1, Female: 0
        paperweightRequest.setAirPollution(1);
        paperweightRequest.setAlcoholUse(1);
        paperweightRequest.setDustAllergy(1);
        paperweightRequest.setOccuPationalHazards(1);
        paperweightRequest.setGeneticRisk(1);
        paperweightRequest.setChronicLungDisease(1);
        paperweightRequest.setBalancedDiet(1);
        paperweightRequest.setObesity(1);
        paperweightRequest.setSmoking(1);
        paperweightRequest.setPassiveSmoker(1);
        paperweightRequest.setChestPain(1);
        paperweightRequest.setCoughingOfBlood(1);
        paperweightRequest.setFatigue(1);
        paperweightRequest.setWeightLoss(1);
        paperweightRequest.setShortnessOfBreath(1);
        paperweightRequest.setWheezing(1);
        paperweightRequest.setSwallowingDifficulty(1);
        paperweightRequest.setClubbingOfFingerNails(1);
        paperweightRequest.setFrequentCold(1);
        paperweightRequest.setDryCough(1);
        paperweightRequest.setSnoring(1);
    }

    @Test
    void getPaperweightsShouldReturnList() {
        // Given
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userRepository.findByUserId("testuser")).thenReturn(user);
        when(paperweightRepository.findAllByUserOrderByCreatedAtDesc(user)).thenReturn(Arrays.asList(paperweight));

        // When
        List<PaperweightResponse> responses = paperweightService.getPaperweights(userDetails);

        // Then
        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getId());
    }

    @Test
    void getPaperweightDetailShouldReturnDetail() {
        // Given
        when(userDetails.getUsername()).thenReturn("testuser");
        when(userRepository.findByUserId("testuser")).thenReturn(user);
        when(paperweightRepository.findByIdAndUser(1L, user)).thenReturn(paperweight);

        // When
        PaperweightDetailResponse response = paperweightService.getPaperweightDetail(userDetails, 1L);

        // Then
        assertEquals(1L, response.getId());
        assertEquals(PaperweightType.NORMAL, response.getPaperweightType());
        assertEquals(RiskLevel.HIGH, response.getRiskLevel());
        assertEquals(45, response.getAge());
        assertEquals(1, response.getGender());
        assertEquals("Test User", response.getUserName());
    }

    @Test
    void runLungPredictionShouldReturnPrediction() throws IOException {
        // This test is more complex due to the Python script execution
        // We'll mock the Process and its input/output streams
        
        // Given
        Process processMock = mock(Process.class);
        ProcessBuilder processBuilderMock = mock(ProcessBuilder.class);
        
        // Mock the ProcessBuilder to return our mocked Process
        when(processBuilderMock.start()).thenReturn(processMock);
        when(processBuilderMock.redirectErrorStream(true)).thenReturn(processBuilderMock);
        
        // Mock the Process input/output streams
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Prediction: High\n".getBytes());
        when(processMock.getOutputStream()).thenReturn(outputStream);
        when(processMock.getInputStream()).thenReturn(inputStream);
        
        // Use MockedStatic to mock the static methods
        try (MockedStatic<ProcessBuilder> processBuilderMockedStatic = Mockito.mockStatic(ProcessBuilder.class)) {
            processBuilderMockedStatic.when(() -> new ProcessBuilder(any(String.class), any(String.class)))
                    .thenReturn(processBuilderMock);
            
            when(userDetails.getUsername()).thenReturn("testuser");
            when(userRepository.findByUserId("testuser")).thenReturn(user);
            
            // When
            String prediction = paperweightService.runLungPrediction(userDetails, paperweightRequest);
            
            // Then
            assertEquals("High", prediction);
        }
    }

    @Test
    void runMultimodalPredictionShouldReturnPrediction() throws IOException {
        // This test is more complex due to the Python script execution and file operations
        // We'll mock the Process, its input/output streams, and the file operations
        
        // Given
        Process processMock = mock(Process.class);
        ProcessBuilder processBuilderMock = mock(ProcessBuilder.class);
        MultipartFile fileMock = mock(MultipartFile.class);
        
        // Mock the ProcessBuilder to return our mocked Process
        when(processBuilderMock.start()).thenReturn(processMock);
        when(processBuilderMock.redirectErrorStream(true)).thenReturn(processBuilderMock);
        
        // Mock the Process input/output streams
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Prediction: High\n".getBytes());
        when(processMock.getOutputStream()).thenReturn(outputStream);
        when(processMock.getInputStream()).thenReturn(inputStream);
        
        // Use MockedStatic to mock the static methods
        try (MockedStatic<ProcessBuilder> processBuilderMockedStatic = Mockito.mockStatic(ProcessBuilder.class)) {
            processBuilderMockedStatic.when(() -> new ProcessBuilder(any(String.class), any(String.class)))
                    .thenReturn(processBuilderMock);
            
            when(userDetails.getUsername()).thenReturn("testuser");
            when(userRepository.findByUserId("testuser")).thenReturn(user);
            
            // When
            String prediction = paperweightService.runMultimodalPrediction(userDetails, fileMock, paperweightRequest);
            
            // Then
            assertEquals("High", prediction);
        }
    }
}