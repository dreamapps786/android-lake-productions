package com.helpers;

public class GameRuler {
	
	private static int shotCount;
	private static final int maxCount = 2;
	private static int totalBubbleCount;
	private static int destroyCount;
	private static int maxRowCount;
	private static int currentRowCount;
	private static BubbleGrid bubbleGrid;
	
	public GameRuler(BubbleGrid grid){
		shotCount = 0;
		bubbleGrid = grid;
	}
	
	public static void bubbleShot(){
		shotCount++;
		checkForMax();
	}
	
	public static void bubbleDestroyed(){
		PointService.Score();
		destroyCount++;
		checkForVictory();
	}
	
	private static void checkForMax(){
		if (shotCount == maxCount) {
			System.out.println("Gameruler: Max Shot");
			shotCount = 0;
			bubbleGrid.insertNewRow();
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
