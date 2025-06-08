package capsthon.backend.deeplung.domain.dto.request;

import capsthon.backend.deeplung.domain.enums.Gender;
import capsthon.backend.deeplung.domain.enums.UserType;
import lombok.Data;

@Data
public class JoinRequest {
	private String userId;
	private String password;
	private String name;
	private Gender gender;
	private UserType userType;
	private String birthYear;
	private Boolean isPrivateInformAgreed;
}
