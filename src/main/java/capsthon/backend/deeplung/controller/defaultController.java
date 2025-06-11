package capsthon.backend.deeplung.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/")
@Tag(name = "기본 API", description = "기본 테스트용 API 엔드포인트")
public class defaultController {

	@Operation(summary = "기본 API 테스트", description = "서버가 정상적으로 동작하는지 확인하기 위한 테스트 API입니다.")
	@ApiResponse(responseCode = "200", description = "서버가 정상적으로 동작 중입니다.")
	@GetMapping("default")
	public String defaultAPI() {
		return "Hello World!!";
	}
}
