package capsthon.backend.deeplung.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/paperweight")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "문진표 API", description = "폐 건강 문진표 관련 API 엔드포인트")
public class PaperweightController {
	private final PaperweightService paperweightService;

	@Operation(summary = "일반 폐 건강 예측", description = "사용자의 문진표 정보를 기반으로 폐 건강 상태를 예측합니다.")
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "예측 성공", 
		content = @Content(schema = @Schema(implementation = ApiResponse.class)))
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "예측 실패", 
		content = @Content(schema = @Schema(implementation = ApiResponse.class)))
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류", 
		content = @Content(schema = @Schema(implementation = ApiResponse.class)))
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

	@Operation(summary = "전문가용 폐 건강 예측", description = "사용자의 문진표 정보와 X-ray 이미지를 기반으로 폐 건강 상태를 예측합니다.")
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "예측 성공", 
		content = @Content(schema = @Schema(implementation = ApiResponse.class)))
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "예측 실패", 
		content = @Content(schema = @Schema(implementation = ApiResponse.class)))
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류", 
		content = @Content(schema = @Schema(implementation = ApiResponse.class)))
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

	@Operation(summary = "문진표 목록 조회", description = "사용자의 모든 문진표 목록을 조회합니다.")
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", 
		content = @Content(schema = @Schema(implementation = ApiResponse.class)))
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", 
		content = @Content(schema = @Schema(implementation = ApiResponse.class)))
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

	@Operation(summary = "문진표 상세 조회", description = "특정 문진표의 상세 정보를 조회합니다.")
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", 
		content = @Content(schema = @Schema(implementation = ApiResponse.class)))
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", 
		content = @Content(schema = @Schema(implementation = ApiResponse.class)))
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "문진표를 찾을 수 없음", 
		content = @Content(schema = @Schema(implementation = ApiResponse.class)))
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

	@Operation(summary = "문진표 개수 조회", description = "사용자의 문진표 개수를 조회합니다.")
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "조회 성공", 
		content = @Content(schema = @Schema(implementation = ApiResponse.class)))
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", 
		content = @Content(schema = @Schema(implementation = ApiResponse.class)))
	@GetMapping("/count")
	public ResponseEntity<?> countPaperweights() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		long count = paperweightService.countPaperweights(userDetails);
		ApiResponse<?> response = ApiResponse.builder()
			.status(200)
			.data(count)
			.message("문진 개수 조회를 완료하였습니다!!")
			.build();
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "X-ray 이미지 조회", description = "특정 문진표에 연결된 X-ray 이미지를 조회합니다.")
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "이미지 조회 성공", 
		content = @Content(mediaType = "image/png"))
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "이미지를 찾을 수 없음")
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "이미지가 없음")
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
	@GetMapping("/{paperweight_id}/xray")
	public ResponseEntity<byte[]> getXrayImage(@PathVariable Long paperweight_id) {
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();

			byte[] imageBytes = paperweightService.getXrayImage(userDetails, paperweight_id);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_PNG);
			headers.setContentLength(imageBytes.length);

			return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (IllegalStateException e) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (IOException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
