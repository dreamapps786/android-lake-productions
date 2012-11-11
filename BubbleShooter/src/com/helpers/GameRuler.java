package com.helpers;

public class GameRuler {
	
	private static int shotCount;
	private static final int maxCount = 5;
	private static int totalBubbleCount;
	private static int destroyCount;
	private static int maxRowCount;
	private static int currentRowCount;
	
	public GameRuler(){
		shotCount = 0;
	}
	
	public static void bubbleShot(){
		shotCount++;
		checkForMax();
	}
	
	public static void bubbleDestroyed(){
		destroyCount++;
		checkForVictory();
	}
	
	private static void checkForMax(){
		if (shotCount == maxCount) {
			shotCount = 0;
			//bubbleGrid.insertNewRow();
		}
	}
	
	private static void checkForVictory(){
		if (destroyCount == totalBubbleCount) {
			//renderer.Victory();
		}
	}
	
	private static void checkForLoss(){
		if (currentRowCount == maxRowCount) {
//			renderer.Loss();
		}
	}

	public static void setTotalBubbleCount(int totalBubbleCount) {
		GameRuler.totalBubbleCount = totalBubbleCount;
	}
	
	public static void setMaxRowCount(int maxRowCount){
		GameRuler.maxRowCount = maxRowCount;
	}
}
