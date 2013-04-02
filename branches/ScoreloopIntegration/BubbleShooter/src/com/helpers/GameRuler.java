package com.helpers;

import com.helpers.extensions.BubbleTexture;
import com.simulation.AnimatedSprite;

import javax.xml.transform.sax.SAXSource;
import java.util.ArrayList;

public class GameRuler {

	private static int shotCount;
	private static final int maxCount = 100;
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
		shotCount++;
		checkForMax();
	}
	

	public static void bubblesDestroyed(int count, boolean hanging) {
		currentBubblesDestroyed += count;
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
			shotCount = 0;
			bubbleGrid.insertNewRow();
		}
	}


    private static void checkForVictory() {
        ArrayList<AnimatedSprite> bubbles = bubbleGrid.getBubbles();
        bubbles.addAll(bubbleGrid.getQueuedBubbles());
        BubbleTexture[] bubbleTextures = bubbleGrid.getBubbleTextures();
        for (BubbleTexture bubbleTexture : bubbleTextures) {
            boolean found = false;
            for (AnimatedSprite bubble : bubbles) {
                if (((BubbleTexture)bubble.getTexture()).getColor() == bubbleTexture.getColor()) {
                    found = true;
                    break;
                }
            }
            if (!found){
                bubbleGrid.removeColor(bubbleTexture.getColor());
            }
        }
        if(bubbles.size()-bubbleGrid.getQueuedBubbles().size() == 0){
            isVictorious = true;}
        else{
            isVictorious = false;
        }
    }

	public static void checkForLoss() {
        System.out.println("CurrentRowCount: "+currentRowCount+ " MaxRowCount: "+ maxRowCount);
        if (currentRowCount >= maxRowCount) {
            // renderer.Loss();
            System.out.println("YOU LOST!");
		}
	}

	public static void setMaxRowCount(int maxRowCount) {
		GameRuler.maxRowCount = maxRowCount;
	}

	public static boolean isVictorious() {
		return isVictorious;
	}

    public static void setCurrentRowCount(int currentRowCount) {
        GameRuler.currentRowCount = currentRowCount;
        checkForLoss();
    }
}
