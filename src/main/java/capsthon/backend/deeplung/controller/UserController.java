package capsthon.backend.deeplung.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import capsthon.backend.deeplung.domain.dto.request.JoinRequest;
import capsthon.backend.deeplung.domain.dto.request.LoginRequest;
import capsthon.backend.deeplung.domain.dto.response.ApiResponse;
import capsthon.backend.deeplung.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*")
@Tag(name = "사용자 API", description = "사용자 관련 API 엔드포인트")
public class UserController {

	private final UserService userService;

	@Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "회원가입 성공", 
		content = @Content(schema = @Schema(implementation = ApiResponse.class)))
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "회원가입 실패", 
		content = @Content(schema = @Schema(implementation = ApiResponse.class)))
	@PostMapping("/join")
	public ResponseEntity<?> join(@RequestBody JoinRequest joinRequest) {
		try {
			userService.createUser(joinRequest);
			ApiResponse<?> response = ApiResponse.builder().status(200).message("회원가입 성공").data(null).build();
			return ResponseEntity.ok(response);
		} catch (RuntimeException e) {
			ApiResponse<?> response = ApiResponse.builder().status(400).message(e.getMessage()).data(null).build();
			return ResponseEntity.badRequest().body(response);
		}
	}

	@Operation(summary = "로그인", description = "사용자 로그인을 처리합니다.")
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그인 성공", 
		content = @Content(schema = @Schema(implementation = ApiResponse.class)))
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "로그인 실패", 
		content = @Content(schema = @Schema(implementation = ApiResponse.class)))
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		ApiResponse<?> response = ApiResponse.builder()
			.status(200)
			.message("로그인 성공")
			.data(userService.login(loginRequest))
			.build();
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "사용자 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공", 
		content = @Content(schema = @Schema(implementation = ApiResponse.class)))
	@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자", 
		content = @Content(schema = @Schema(implementation = ApiResponse.class)))
	@GetMapping("/info")
	public ResponseEntity<?> info(@AuthenticationPrincipal UserDetails userDetails) {
		ApiResponse<?> response = ApiResponse.builder()
			.status(200)
			.message("로그인 성공")
			.data(userService.info(userDetails))
			.build();
		return ResponseEntity.ok(response);
	}
}
