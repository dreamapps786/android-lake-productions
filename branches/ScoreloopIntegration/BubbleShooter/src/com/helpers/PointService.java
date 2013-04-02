package com.helpers;

public class PointService {

	private static int totalPoints;

	public PointService() {
		totalPoints = 0;
	}

	public static void Score(int count) {
		int pointsAwarded = 0;
		if (count <= 3) {
			pointsAwarded = count;
		}
		else if (count < 6) {
			pointsAwarded = count * 2;
		}
		else if (count < 10) {
			pointsAwarded = count * 3;
		}
		else {
			pointsAwarded = count * 5;
		}
		totalPoints+= pointsAwarded;
	}

	public static int getTotalPoints() {
		return totalPoints;
	}

}
