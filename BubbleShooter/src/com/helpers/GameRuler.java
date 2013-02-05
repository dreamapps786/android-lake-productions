package com.helpers;

public class GameRuler {

	private static int shotCount;
	private static final int maxCount = 3;
	private static int totalBubbleCount;
	private static int destroyCount;
	private static int maxRowCount;
	private static int currentRowCount;
	private static BubbleGrid bubbleGrid;
	private static boolean isVictorious;
	private static int currentBubblesDestroyed;

	public GameRuler(BubbleGrid grid) {
		shotCount = 0;
		bubbleGrid = grid;
		isVictorious = false;
	}

	public static void bubbleShot() {
		System.out.println("bubbleShot");
		shotCount++;
		totalBubbleCount++;
		checkForMax();
	}
	

	public static void bubblesDestroyed(int count, boolean hanging) {
		currentBubblesDestroyed += count;
		destroyCount += count;
		checkForVictory();
		if (hanging) {
			clearCurrentBubblesDestroyed();			
		}
	}

	public static void clearCurrentBubblesDestroyed() {
		System.out.println("Gaining points for: " + currentBubblesDestroyed);
		PointService.Score(currentBubblesDestroyed);
		if (currentBubblesDestroyed > 0) {
			shotCount--;			
		}
		currentBubblesDestroyed = 0;
	}

	private static void checkForMax() {
		if (shotCount == maxCount) {
			System.out.println("Gameruler: Max Shot");
			shotCount = 0;
			bubbleGrid.insertNewRow();
		}
	}

	private static void checkForVictory() {
		System.out.println("destroyCount: " + destroyCount + " totalCount: " + totalBubbleCount);
		if (destroyCount == totalBubbleCount) {
			isVictorious = true;
		}
	}

	private static void checkForLoss() {
		if (currentRowCount == maxRowCount) {
			// renderer.Loss();
		}
	}

	public static void setTotalBubbleCount(int totalBubbleCount) {
		GameRuler.totalBubbleCount += totalBubbleCount;
	}

	public static void setMaxRowCount(int maxRowCount) {
		GameRuler.maxRowCount = maxRowCount;
	}

	public static boolean isVictorious() {
		return isVictorious;
	}
}
