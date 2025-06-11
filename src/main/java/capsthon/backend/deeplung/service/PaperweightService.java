package capsthon.backend.deeplung.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import capsthon.backend.deeplung.domain.dto.request.PaperweightRequest;
import capsthon.backend.deeplung.domain.dto.response.PaperweightDetailResponse;
import capsthon.backend.deeplung.domain.dto.response.PaperweightResponse;
import capsthon.backend.deeplung.domain.entity.Paperweight;
import capsthon.backend.deeplung.domain.entity.User;
import capsthon.backend.deeplung.domain.enums.PaperweightType;
import capsthon.backend.deeplung.domain.enums.RiskLevel;
import capsthon.backend.deeplung.repository.PaperweightRepository;
import capsthon.backend.deeplung.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaperweightService {

	private final UserRepository userRepository;
	private final PaperweightRepository paperweightRepository;

	public String runLungPrediction(UserDetails userDetails, PaperweightRequest paperweightRequest) throws IOException {
		System.out.println(userDetails.getUsername());

		User user = userRepository.findByUserId(userDetails.getUsername());

		String scriptPath = new File("python/lung_predict.py").getAbsolutePath();
		String pythonPath = resolvePythonPath();

		ProcessBuilder builder = new ProcessBuilder(pythonPath, scriptPath);
		builder.redirectErrorStream(true);
		Process process = builder.start();

		Map<String, Object> input = new HashMap<>();
		input.put("Age", paperweightRequest.getAge());
		input.put("Gender", paperweightRequest.getGender());  // Male: 1, Female: 0
		input.put("Air Pollution", paperweightRequest.getAirPollution());
		input.put("Alcohol use", paperweightRequest.getAlcoholUse());
		input.put("Dust Allergy", paperweightRequest.getDustAllergy());
		input.put("OccuPational Hazards", paperweightRequest.getOccuPationalHazards());
		input.put("Genetic Risk", paperweightRequest.getGeneticRisk());
		input.put("chronic Lung Disease", paperweightRequest.getChronicLungDisease());
		input.put("Balanced Diet", paperweightRequest.getBalancedDiet());
		input.put("Obesity", paperweightRequest.getObesity());
		input.put("Smoking", paperweightRequest.getSmoking());
		input.put("Passive Smoker", paperweightRequest.getPassiveSmoker());
		input.put("Chest Pain", paperweightRequest.getChestPain());
		input.put("Coughing of Blood", paperweightRequest.getCoughingOfBlood());
		input.put("Fatigue", paperweightRequest.getFatigue());
		input.put("Weight Loss", paperweightRequest.getWeightLoss());
		input.put("Shortness of Breath", paperweightRequest.getShortnessOfBreath());
		input.put("Wheezing", paperweightRequest.getWheezing());
		input.put("Swallowing Difficulty", paperweightRequest.getSwallowingDifficulty());
		input.put("Clubbing of Finger Nails", paperweightRequest.getClubbingOfFingerNails());
		input.put("Frequent Cold", paperweightRequest.getFrequentCold());
		input.put("Dry Cough", paperweightRequest.getDryCough());
		input.put("Snoring", paperweightRequest.getSnoring());

		// 입력 스트림에 JSON 쓰기
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
			String json = new ObjectMapper().writeValueAsString(input);
			writer.write(json);
			writer.flush();
		}

		// 출력 스트림 읽기
		StringBuilder result = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println("PYTHON ▶ " + line); // 디버깅 출력
				result.append(line).append("\n");
			}
		}

		Pattern pattern = Pattern.compile("\\b(Low|Medium|High)\\b");
		Matcher matcher = pattern.matcher(result.toString());

		if (matcher.find()) {
			String prediction = matcher.group(1);

			Paperweight newPaperweight = Paperweight.builder()
				.riskLevel(prediction.equals("Low") ? RiskLevel.LOW :
					(prediction.equals("High") ? RiskLevel.HIGH : RiskLevel.MEDIUM))
				.user(user)
				.paperweightType(PaperweightType.PROFESSIONAL)
				.age(paperweightRequest.getAge())
				.gender(paperweightRequest.getGender())
				.airPollution(paperweightRequest.getAirPollution())
				.alcoholUse(paperweightRequest.getAlcoholUse())
				.dustAllergy(paperweightRequest.getDustAllergy())
				.occuPationalHazards(paperweightRequest.getOccuPationalHazards())
				.geneticRisk(paperweightRequest.getGeneticRisk())
				.chronicLungDisease(paperweightRequest.getChronicLungDisease())
				.balancedDiet(paperweightRequest.getBalancedDiet())
				.obesity(paperweightRequest.getObesity())
				.smoking(paperweightRequest.getSmoking())
				.passiveSmoker(paperweightRequest.getPassiveSmoker())
				.chestPain(paperweightRequest.getChestPain())
				.coughingOfBlood(paperweightRequest.getCoughingOfBlood())
				.fatigue(paperweightRequest.getFatigue())
				.weightLoss(paperweightRequest.getWeightLoss())
				.shortnessOfBreath(paperweightRequest.getShortnessOfBreath())
				.wheezing(paperweightRequest.getWheezing())
				.swallowingDifficulty(paperweightRequest.getSwallowingDifficulty())
				.clubbingOfFingerNails(paperweightRequest.getClubbingOfFingerNails())
				.frequentCold(paperweightRequest.getFrequentCold())
				.dryCough(paperweightRequest.getDryCough())
				.snoring(paperweightRequest.getSnoring())
				.build();

			paperweightRepository.save(newPaperweight);
			return prediction;
		} else {
			throw new IllegalStateException("예측 결과를 찾을 수 없습니다.");
		}
	}

	public String runMultimodalPrediction(UserDetails userDetails, MultipartFile file,
		PaperweightRequest paperweightRequest) throws IOException {
		User user = userRepository.findByUserId(userDetails.getUsername());

		// 1. 이미지 파일 저장 (임시)
		File tempImage = File.createTempFile("xray_", ".png");
		file.transferTo(tempImage);

		// 2. paperweightRequest를 Map으로 변환 + xray_path 추가
		Map<String, Object> input = new HashMap<>();
		input.put("xray_path", tempImage.getAbsolutePath());
		input.put("Age", paperweightRequest.getAge());
		input.put("Gender", paperweightRequest.getGender());
		input.put("Air Pollution", paperweightRequest.getAirPollution());
		input.put("Alcohol use", paperweightRequest.getAlcoholUse());
		input.put("Dust Allergy", paperweightRequest.getDustAllergy());
		input.put("OccuPational Hazards", paperweightRequest.getOccuPationalHazards());
		input.put("Genetic Risk", paperweightRequest.getGeneticRisk());
		input.put("chronic Lung Disease", paperweightRequest.getChronicLungDisease());
		input.put("Balanced Diet", paperweightRequest.getBalancedDiet());
		input.put("Obesity", paperweightRequest.getObesity());
		input.put("Smoking", paperweightRequest.getSmoking());
		input.put("Passive Smoker", paperweightRequest.getPassiveSmoker());
		input.put("Chest Pain", paperweightRequest.getChestPain());
		input.put("Coughing of Blood", paperweightRequest.getCoughingOfBlood());
		input.put("Fatigue", paperweightRequest.getFatigue());
		input.put("Weight Loss", paperweightRequest.getWeightLoss());
		input.put("Shortness of Breath", paperweightRequest.getShortnessOfBreath());
		input.put("Wheezing", paperweightRequest.getWheezing());
		input.put("Swallowing Difficulty", paperweightRequest.getSwallowingDifficulty());
		input.put("Clubbing of Finger Nails", paperweightRequest.getClubbingOfFingerNails());
		input.put("Frequent Cold", paperweightRequest.getFrequentCold());

		// 3. 파이썬 스크립트 실행
		String scriptPath = new File("python/multimodal_predict.py").getAbsolutePath();
		String pythonPath = resolvePythonPath();
		ProcessBuilder builder = new ProcessBuilder(pythonPath, scriptPath);
		builder.redirectErrorStream(true);
		Process process = builder.start();

		// 4. JSON 전달
		ObjectMapper mapper = new ObjectMapper();
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
			writer.write(mapper.writeValueAsString(input));
			writer.flush();
		}

		// 5. 출력 수신
		StringBuilder result = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println("PYTHON ▶ " + line);
				result.append(line).append("\n");
			}
		}

		Pattern pattern = Pattern.compile("\\b(Low|Medium|High)\\b");
		Matcher matcher = pattern.matcher(result.toString());

		if (matcher.find()) {
			String prediction = matcher.group(1);
			// 6. 예측에 사용된 이미지 저장
			String saveDirPath = "saved_xrays";
			File saveDir = new File(saveDirPath);
			if (!saveDir.exists()) {
				saveDir.mkdirs();
			}

			String timestamp = String.valueOf(System.currentTimeMillis());
			String saveFilename = String.format("xray_%s_%s.png", user.getUserId(), timestamp);
			File savedImage = new File(saveDir, saveFilename);
			Files.copy(tempImage.toPath(), savedImage.toPath(), StandardCopyOption.REPLACE_EXISTING);

			Paperweight newPaperweight = Paperweight.builder()
				.imageUrl(savedImage.getPath())
				.riskLevel(prediction.equals("Low") ? RiskLevel.LOW :
					(prediction.equals("High") ? RiskLevel.HIGH : RiskLevel.MEDIUM))
				.user(user)
				.paperweightType(PaperweightType.PROFESSIONAL)
				.age(paperweightRequest.getAge())
				.gender(paperweightRequest.getGender())
				.airPollution(paperweightRequest.getAirPollution())
				.alcoholUse(paperweightRequest.getAlcoholUse())
				.dustAllergy(paperweightRequest.getDustAllergy())
				.occuPationalHazards(paperweightRequest.getOccuPationalHazards())
				.geneticRisk(paperweightRequest.getGeneticRisk())
				.chronicLungDisease(paperweightRequest.getChronicLungDisease())
				.balancedDiet(paperweightRequest.getBalancedDiet())
				.obesity(paperweightRequest.getObesity())
				.smoking(paperweightRequest.getSmoking())
				.passiveSmoker(paperweightRequest.getPassiveSmoker())
				.chestPain(paperweightRequest.getChestPain())
				.coughingOfBlood(paperweightRequest.getCoughingOfBlood())
				.fatigue(paperweightRequest.getFatigue())
				.weightLoss(paperweightRequest.getWeightLoss())
				.shortnessOfBreath(paperweightRequest.getShortnessOfBreath())
				.wheezing(paperweightRequest.getWheezing())
				.swallowingDifficulty(paperweightRequest.getSwallowingDifficulty())
				.clubbingOfFingerNails(paperweightRequest.getClubbingOfFingerNails())
				.frequentCold(paperweightRequest.getFrequentCold())
				.dryCough(paperweightRequest.getDryCough())
				.snoring(paperweightRequest.getSnoring())
				.build();

			paperweightRepository.save(newPaperweight);

			return prediction;
		} else {
			throw new IllegalStateException("예측 결과를 찾을 수 없습니다.");
		}
	}

	private String resolvePythonPath() {
		String localPython = "/Users/hyunwoo/DeepLung-venv/bin/python";
		String serverPython = "/usr/bin/python3";

		if (new File(localPython).exists())
			return localPython;
		else if (new File(serverPython).exists())
			return serverPython;
		else
			return "python3";
	}

	public List<PaperweightResponse> getPaperweights(UserDetails userDetails) {
		User user = userRepository.findByUserId(userDetails.getUsername());

		return paperweightRepository.findAllByUserOrderByCreatedAtDesc(user).stream().map(
			paperweight -> PaperweightResponse.builder()
				.id(paperweight.getId())
				.createdAt(paperweight.getCreatedAt())
				.build()
		).toList();
	}

	public PaperweightDetailResponse getPaperweightDetail(UserDetails userDetails, Long paperweightId) {
		User user = userRepository.findByUserId(userDetails.getUsername());

		Paperweight paperweight = paperweightRepository.findByIdAndUser(paperweightId, user);

		return PaperweightDetailResponse.builder()
			.paperweightType(paperweight.getPaperweightType())
			.age(paperweight.getAge())
			.airPollution(paperweight.getAirPollution())
			.balancedDiet(paperweight.getBalancedDiet())
			.alcoholUse(paperweight.getAlcoholUse())
			.chronicLungDisease(paperweight.getChronicLungDisease())
			.clubbingOfFingerNails(paperweight.getClubbingOfFingerNails())
			.coughingOfBlood(paperweight.getCoughingOfBlood())
			.fatigue(paperweight.getFatigue())
			.gender(paperweight.getGender())
			.chestPain(paperweight.getChestPain())
			.id(paperweightId)
			.dryCough(paperweight.getDryCough())
			.obesity(paperweight.getObesity())
			.dustAllergy(paperweight.getDustAllergy())
			.frequentCold(paperweight.getFrequentCold())
			.geneticRisk(paperweight.getGeneticRisk())
			.occuPationalHazards(paperweight.getOccuPationalHazards())
			.passiveSmoker(paperweight.getPassiveSmoker())
			.snoring(paperweight.getSnoring())
			.smoking(paperweight.getSmoking())
			.userName(user.getName())
			.swallowingDifficulty(paperweight.getSwallowingDifficulty())
			.weightLoss(paperweight.getWeightLoss())
			.wheezing(paperweight.getWheezing())
			.riskLevel(paperweight.getRiskLevel())
			.shortnessOfBreath(paperweight.getShortnessOfBreath())
			.build();
	}

	public long countPaperweights(UserDetails userDetails) {
		User user = userRepository.findByUserId(userDetails.getUsername());
		return paperweightRepository.countByUser(user);
	}

	public byte[] getXrayImage(UserDetails userDetails, Long paperweightId) throws IOException {
		User user = userRepository.findByUserId(userDetails.getUsername());
		Paperweight paperweight = paperweightRepository.findByIdAndUser(paperweightId, user);

		if (paperweight == null) {
			throw new IllegalArgumentException("해당 ID의 문진 기록을 찾을 수 없습니다.");
		}

		if (paperweight.getImageUrl() == null || paperweight.getImageUrl().isEmpty()) {
			throw new IllegalStateException("해당 문진 기록에 X-ray 이미지가 없습니다.");
		}

		File imageFile = new File(paperweight.getImageUrl());
		if (!imageFile.exists() || !imageFile.isFile()) {
			throw new IOException("X-ray 이미지 파일을 찾을 수 없습니다.");
		}

		return Files.readAllBytes(imageFile.toPath());
	}
}
