package capsthon.backend.deeplung.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import capsthon.backend.deeplung.domain.dto.request.PaperweightRequest;
import capsthon.backend.deeplung.domain.dto.response.PaperweightDetailResponse;
import capsthon.backend.deeplung.domain.dto.response.PaperweightResponse;
import capsthon.backend.deeplung.domain.enums.PaperweightType;
import capsthon.backend.deeplung.domain.enums.RiskLevel;
import capsthon.backend.deeplung.service.PaperweightService;

@ExtendWith(MockitoExtension.class)
public class PaperweightControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private PaperweightService paperweightService;

    @InjectMocks
    private PaperweightController paperweightController;

    @Mock
    private UserDetails userDetails;

    private PaperweightRequest paperweightRequest;
    private PaperweightResponse paperweightResponse;
    private PaperweightDetailResponse paperweightDetailResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paperweightController).build();
        objectMapper = new ObjectMapper();

        // Setup test data
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

        paperweightResponse = new PaperweightResponse();
        paperweightResponse.setId(1L);
        paperweightResponse.setCreatedAt(LocalDateTime.now());

        paperweightDetailResponse = new PaperweightDetailResponse();
        paperweightDetailResponse.setId(1L);
        paperweightDetailResponse.setPaperweightType(PaperweightType.NORMAL);
        paperweightDetailResponse.setRiskLevel(RiskLevel.HIGH);
        paperweightDetailResponse.setCreatedAt(LocalDateTime.now());
        paperweightDetailResponse.setUserName("Test User");
        paperweightDetailResponse.setAge(45);
        paperweightDetailResponse.setGender(1);
        paperweightDetailResponse.setAirPollution(1);
        paperweightDetailResponse.setAlcoholUse(1);
        paperweightDetailResponse.setDustAllergy(1);
        paperweightDetailResponse.setOccuPationalHazards(1);
        paperweightDetailResponse.setGeneticRisk(1);
        paperweightDetailResponse.setChronicLungDisease(1);
        paperweightDetailResponse.setBalancedDiet(1);
        paperweightDetailResponse.setObesity(1);
        paperweightDetailResponse.setSmoking(1);
        paperweightDetailResponse.setPassiveSmoker(1);
        paperweightDetailResponse.setChestPain(1);
        paperweightDetailResponse.setCoughingOfBlood(1);
        paperweightDetailResponse.setFatigue(1);
        paperweightDetailResponse.setWeightLoss(1);
        paperweightDetailResponse.setShortnessOfBreath(1);
        paperweightDetailResponse.setWheezing(1);
        paperweightDetailResponse.setSwallowingDifficulty(1);
        paperweightDetailResponse.setClubbingOfFingerNails(1);
        paperweightDetailResponse.setFrequentCold(1);
        paperweightDetailResponse.setDryCough(1);
        paperweightDetailResponse.setSnoring(1);
    }

    @Test
    @WithMockUser
    void normalShouldReturnSuccess() throws Exception {
        // Given
        when(paperweightService.runLungPrediction(any(UserDetails.class), any(PaperweightRequest.class)))
                .thenReturn("폐암 위험도가 높습니다.");

        // When & Then
        mockMvc.perform(post("/api/v1/paperweight/normal")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paperweightRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("폐암 위험도가 높습니다."));
    }

    @Test
    @WithMockUser
    void professionalShouldReturnSuccess() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file", 
                "test.jpg", 
                MediaType.IMAGE_JPEG_VALUE, 
                "test image content".getBytes());

        MockMultipartFile jsonFile = new MockMultipartFile(
                "survey", 
                "", 
                MediaType.APPLICATION_JSON_VALUE, 
                objectMapper.writeValueAsString(paperweightRequest).getBytes());

        when(paperweightService.runMultimodalPrediction(any(UserDetails.class), any(), any(PaperweightRequest.class)))
                .thenReturn("폐암 위험도가 높습니다.");

        // When & Then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/paperweight/professional")
                .file(file)
                .file(jsonFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("폐암 위험도가 높습니다."));
    }

    @Test
    @WithMockUser
    void getPaperweightsShouldReturnList() throws Exception {
        // Given
        List<PaperweightResponse> paperweights = Arrays.asList(paperweightResponse);
        when(paperweightService.getPaperweights(any(UserDetails.class))).thenReturn(paperweights);

        // When & Then
        mockMvc.perform(get("/api/v1/paperweight/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("조회를 완료하였습니다!!"))
                .andExpect(jsonPath("$.data[0].id").value(1));
    }

    @Test
    @WithMockUser
    void getPaperweightDetailShouldReturnDetail() throws Exception {
        // Given
        when(paperweightService.getPaperweightDetail(any(UserDetails.class), anyLong()))
                .thenReturn(paperweightDetailResponse);

        // When & Then
        mockMvc.perform(get("/api/v1/paperweight/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("조회를 완료하였습니다!!"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.paperweightType").value("NORMAL"))
                .andExpect(jsonPath("$.data.riskLevel").value("HIGH"))
                .andExpect(jsonPath("$.data.age").value(45))
                .andExpect(jsonPath("$.data.gender").value(1));
    }

    @Test
    void countPaperweightsShouldReturnCount() throws Exception {
        // Given
        when(paperweightService.countPaperweights(any(UserDetails.class)))
                .thenReturn(5L);

        // Create a mock Authentication
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Set up SecurityContextHolder
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // When & Then
        mockMvc.perform(get("/api/v1/paperweight/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("문진 개수 조회를 완료하였습니다!!"))
                .andExpect(jsonPath("$.data").value(5));

        // Clean up SecurityContextHolder
        SecurityContextHolder.clearContext();
    }

    @Test
    void getXrayImageShouldReturnImage() throws Exception {
        // Given
        byte[] mockImageBytes = "test image content".getBytes();
        when(paperweightService.getXrayImage(any(UserDetails.class), anyLong()))
                .thenReturn(mockImageBytes);

        // Create a mock Authentication
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Set up SecurityContextHolder
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // When & Then
        mockMvc.perform(get("/api/v1/paperweight/1/xray"))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    byte[] responseBytes = result.getResponse().getContentAsByteArray();
                    assert responseBytes.length == mockImageBytes.length;
                    assert result.getResponse().getContentType().equals(MediaType.IMAGE_PNG_VALUE);
                });

        // Clean up SecurityContextHolder
        SecurityContextHolder.clearContext();
    }

    @Test
    void getXrayImageShouldReturnNotFoundWhenPaperweightDoesNotExist() throws Exception {
        // Given
        when(paperweightService.getXrayImage(any(UserDetails.class), anyLong()))
                .thenThrow(new IllegalArgumentException("해당 ID의 문진 기록을 찾을 수 없습니다."));

        // Create a mock Authentication
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Set up SecurityContextHolder
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // When & Then
        mockMvc.perform(get("/api/v1/paperweight/999/xray"))
                .andExpect(status().isNotFound());

        // Clean up SecurityContextHolder
        SecurityContextHolder.clearContext();
    }

    @Test
    void getXrayImageShouldReturnNoContentWhenNoImage() throws Exception {
        // Given
        when(paperweightService.getXrayImage(any(UserDetails.class), anyLong()))
                .thenThrow(new IllegalStateException("해당 문진 기록에 X-ray 이미지가 없습니다."));

        // Create a mock Authentication
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Set up SecurityContextHolder
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // When & Then
        mockMvc.perform(get("/api/v1/paperweight/1/xray"))
                .andExpect(status().isNoContent());

        // Clean up SecurityContextHolder
        SecurityContextHolder.clearContext();
    }
}
