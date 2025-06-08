package capsthon.backend.deeplung.domain.entity;

import java.time.LocalDate;

import capsthon.backend.deeplung.domain.enums.Gender;
import capsthon.backend.deeplung.domain.enums.UserType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String userId;
	private String password;
	private String name;
	private Gender gender;
	private UserType userType;
	private String birthYear;
	private Boolean isPrivateInformAgreed;

	@Builder
	public User(String userId, String password, String name, Gender gender, UserType userType, String birthYear,
		Boolean isPrivateInformAgreed) {
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.gender = gender;
		this.userType = userType;
		this.birthYear = birthYear;
		this.isPrivateInformAgreed = isPrivateInformAgreed;
	}
}
