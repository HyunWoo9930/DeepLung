package capsthon.backend.deeplung.domain.entity;

import java.time.LocalDateTime;

import capsthon.backend.deeplung.domain.enums.PaperweightType;
import capsthon.backend.deeplung.domain.enums.RiskLevel;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Paperweight {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private RiskLevel riskLevel;
	private LocalDateTime createdAt;
	private PaperweightType paperweightType;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	// ✅ 문진 항목들
	private String imageUrl;
	private Integer age;
	private Integer gender;
	private Integer airPollution;
	private Integer alcoholUse;
	private Integer dustAllergy;
	private Integer occuPationalHazards;
	private Integer geneticRisk;
	private Integer chronicLungDisease;
	private Integer balancedDiet;
	private Integer obesity;
	private Integer smoking;
	private Integer passiveSmoker;
	private Integer chestPain;
	private Integer coughingOfBlood;
	private Integer fatigue;
	private Integer weightLoss;
	private Integer shortnessOfBreath;
	private Integer wheezing;
	private Integer swallowingDifficulty;
	private Integer clubbingOfFingerNails;
	private Integer frequentCold;
	private Integer dryCough;
	private Integer snoring;

	@Builder
	public Paperweight(
		String imageUrl,
		RiskLevel riskLevel,
		User user,
		PaperweightType paperweightType,
		Integer age,
		Integer gender,
		Integer airPollution,
		Integer alcoholUse,
		Integer dustAllergy,
		Integer occuPationalHazards,
		Integer geneticRisk,
		Integer chronicLungDisease,
		Integer balancedDiet,
		Integer obesity,
		Integer smoking,
		Integer passiveSmoker,
		Integer chestPain,
		Integer coughingOfBlood,
		Integer fatigue,
		Integer weightLoss,
		Integer shortnessOfBreath,
		Integer wheezing,
		Integer swallowingDifficulty,
		Integer clubbingOfFingerNails,
		Integer frequentCold,
		Integer dryCough,
		Integer snoring
	) {
		this.imageUrl = imageUrl;
		this.riskLevel = riskLevel;
		this.createdAt = LocalDateTime.now();
		this.user = user;
		this.paperweightType = paperweightType;
		this.age = age;
		this.gender = gender;
		this.airPollution = airPollution;
		this.alcoholUse = alcoholUse;
		this.dustAllergy = dustAllergy;
		this.occuPationalHazards = occuPationalHazards;
		this.geneticRisk = geneticRisk;
		this.chronicLungDisease = chronicLungDisease;
		this.balancedDiet = balancedDiet;
		this.obesity = obesity;
		this.smoking = smoking;
		this.passiveSmoker = passiveSmoker;
		this.chestPain = chestPain;
		this.coughingOfBlood = coughingOfBlood;
		this.fatigue = fatigue;
		this.weightLoss = weightLoss;
		this.shortnessOfBreath = shortnessOfBreath;
		this.wheezing = wheezing;
		this.swallowingDifficulty = swallowingDifficulty;
		this.clubbingOfFingerNails = clubbingOfFingerNails;
		this.frequentCold = frequentCold;
		this.dryCough = dryCough;
		this.snoring = snoring;
	}
}