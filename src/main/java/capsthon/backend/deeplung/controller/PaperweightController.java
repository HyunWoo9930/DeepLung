package capsthon.backend.deeplung.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import capsthon.backend.deeplung.domain.dto.request.PaperweightRequest;
import capsthon.backend.deeplung.domain.dto.response.ApiResponse;
import capsthon.backend.deeplung.domain.dto.response.PaperweightDetailResponse;
import capsthon.backend.deeplung.domain.dto.response.PaperweightResponse;
import capsthon.backend.deeplung.service.PaperweightService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/paperweight")
@RequiredArgsConstructor
public class PaperweightController {
	private final PaperweightService paperweightService;

	@PostMapping("/normal")
	public ResponseEntity<?> normal(@AuthenticationPrincipal UserDetails userDetails,
		@RequestBody PaperweightRequest paperweightRequest) throws IOException {
		String prediction = paperweightService.runLungPrediction(userDetails, paperweightRequest);
		ApiResponse<?> response = ApiResponse.builder()
			.status(200)
			.data(null)
			.message(prediction)
			.build();
		return ResponseEntity.ok(response);
	}

	@PostMapping(value = "/professional",
		consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<?> professional(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestPart("survey") PaperweightRequest paperweightRequest,
		@RequestPart("file") MultipartFile file
	) throws IOException {
		String prediction = paperweightService.runMultimodalPrediction(userDetails, file, paperweightRequest);
		ApiResponse<?> response = ApiResponse.builder()
			.status(200)
			.data(null)
			.message(prediction)
			.build();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/")
	public ResponseEntity<?> getPaperweights(@AuthenticationPrincipal UserDetails userDetails) {
		List<PaperweightResponse> paperweights = paperweightService.getPaperweights(userDetails);
		ApiResponse<?> response = ApiResponse.builder()
			.status(200)
			.data(paperweights)
			.message("조회를 완료하였습니다!!")
			.build();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{paperweight_id}")
	public ResponseEntity<?> getPaperweightDetail(@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable Long paperweight_id) {
		PaperweightDetailResponse paperweightDetail = paperweightService.getPaperweightDetail(userDetails,
			paperweight_id);
		ApiResponse<?> response = ApiResponse.builder()
			.status(200)
			.data(paperweightDetail)
			.message("조회를 완료하였습니다!!")
			.build();
		return ResponseEntity.ok(response);
	}
}
