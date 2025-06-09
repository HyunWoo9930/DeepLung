package capsthon.backend.deeplung.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import capsthon.backend.deeplung.domain.dto.request.JoinRequest;
import capsthon.backend.deeplung.domain.dto.request.LoginRequest;
import capsthon.backend.deeplung.domain.dto.response.ApiResponse;
import capsthon.backend.deeplung.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = "*")
public class UserController {

	private final UserService userService;

	@PostMapping("/join")
	public ResponseEntity<?> join(@RequestBody JoinRequest joinRequest) {
		userService.createUser(joinRequest);
		ApiResponse<?> response = ApiResponse.builder().status(200).message("회원가입 성공").data(null).build();
		return ResponseEntity.ok(response);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		ApiResponse<?> response = ApiResponse.builder()
			.status(200)
			.message("로그인 성공")
			.data(userService.login(loginRequest))
			.build();
		return ResponseEntity.ok(response);
	}
}
