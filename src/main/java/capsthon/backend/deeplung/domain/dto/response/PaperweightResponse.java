package capsthon.backend.deeplung.domain.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaperweightResponse {
	private Long id;
	private LocalDateTime createdAt;

	@Builder
	public PaperweightResponse(Long id, LocalDateTime createdAt) {
		this.id = id;
		this.createdAt = createdAt;
	}
}
