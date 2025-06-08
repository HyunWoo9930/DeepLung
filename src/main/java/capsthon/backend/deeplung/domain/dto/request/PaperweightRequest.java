package capsthon.backend.deeplung.domain.dto.request;

import lombok.Data;

@Data
public class PaperweightRequest {
	private int age;
	private int gender;  // Male: 1, Female: 0
	private int airPollution;
	private int alcoholUse;
	private int dustAllergy;
	private int occuPationalHazards;
	private int geneticRisk;
	private int chronicLungDisease;
	private int balancedDiet;
	private int obesity;
	private int smoking;
	private int passiveSmoker;
	private int chestPain;
	private int coughingOfBlood;
	private int fatigue;
	private int weightLoss;
	private int shortnessOfBreath;
	private int wheezing;
	private int swallowingDifficulty;
	private int clubbingOfFingerNails;
	private int frequentCold;
	private int dryCough;
	private int snoring;
}