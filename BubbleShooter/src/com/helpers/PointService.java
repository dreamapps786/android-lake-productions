package com.helpers;

public class PointService {

	private static int totalPoints;

	public PointService() {
		totalPoints = 0;
	}

	public static void Score() {
		totalPoints++;
	}
	
	public static void Score(int count){
		
	}

	public static int getTotalPoints() {
		return totalPoints;
	}

}
