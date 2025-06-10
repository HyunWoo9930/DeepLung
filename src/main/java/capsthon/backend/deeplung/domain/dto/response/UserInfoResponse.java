package capsthon.backend.deeplung.domain.dto.response;

import capsthon.backend.deeplung.domain.enums.Gender;
import capsthon.backend.deeplung.domain.enums.UserType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserInfoResponse {
	private Long id;
	private String userId;
	private String password;
	private String name;
	private Gender gender;
	private UserType userType;
	private String birthYear;
	private Boolean isPrivateInformAgreed;

	@Builder
	public UserInfoResponse(Long id, String userId, String password, String name, Gender gender, UserType userType,
		String birthYear, Boolean isPrivateInformAgreed) {
		this.id = id;
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.gender = gender;
		this.userType = userType;
		this.birthYear = birthYear;
		this.isPrivateInformAgreed = isPrivateInformAgreed;
	}
}
