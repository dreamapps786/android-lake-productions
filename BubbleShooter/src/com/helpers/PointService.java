package com.helpers;

public class PointService {

	private static int totalPoints;

	public PointService() {
		totalPoints = 0;
	}

	public static void Score() {
		totalPoints++;
	}

	public static int getTotalPoints() {
		return totalPoints;
	}

}
