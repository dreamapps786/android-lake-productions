package com.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.helpers.collision.CollisionHelper;
import com.helpers.collision.CollisionHelper.PolygonCollisionResult;
import com.helpers.collision.Polygon;
import com.helpers.collision.PolygonFactory;
import com.helpers.collision.Vector;
import com.helpers.extensions.BubbleQueueList;
import com.helpers.extensions.BubbleTexture;
import com.model.BubbleGridRectangle;
import com.model.CollisionObject;
import com.model.CollisionObject.Direction;
import com.simulation.AnimatedSprite;

public class BubbleGrid {
	private BubbleGridRectangle[][] boxes;
	private final int gridWidth = 14;
	private int gridHeight = 28;
	private final int initialPopHeight = 24;
	private final int marginY = 6;
	private final int marginX = 1;
	private final BubbleTexture[] bubbleTextures;
    private final List<BubbleTexture> allowedBubbleTextures;
    private BubbleQueueList queuedBubbles;
	
	public static final int sidesOfCollisionBubbles = 6;

	public BubbleGrid(BubbleTexture[] bubbleTextures, float x, float y) {
		boxes = new BubbleGridRectangle[gridHeight][gridWidth];
		GameRuler.setMaxRowCount(gridHeight-1);
		this.bubbleTextures = bubbleTextures;
        this.allowedBubbleTextures = new ArrayList<BubbleTexture>(Arrays.asList(bubbleTextures));
		populate(bubbleTextures, false);
	}

	private void populate(BubbleTexture[] bubbleTextures, boolean justTopRow) {
		int counter = bubbleTextures.length;
		for (int yIndex = 0; yIndex < (justTopRow ? 1 : initialPopHeight); yIndex++) {
			for (int xIndex = 0; xIndex < boxes[yIndex].length; xIndex++) {
				placeBubble(bubbleTextures[(int) (Math.random() * counter)], xIndex, yIndex);
				// int offSetX = yIndex % 2 == 0 ? 0 : 16;
				// boxes[yIndex][xIndex] = new BubbleGridRectangle(xIndex * 32 +
				// (xIndex * marginX) + offSetX, yIndex * -32 + 800
				// - (yIndex * -marginY), 32, 32, xIndex, yIndex, );
			}
		}
	}

	// public BubbleGridRectangle[][] getGrid() {
	// return boxes;
	// }
	
	//TODO Replace with the other method, when it's done
	public PolygonCollisionResult checkForCollision(float centerX, float centerY, float radius, double direction, double distance) {
		float xVel = (float) (distance * Math.cos(direction));
		float yVel = (float) (distance * Math.sin(direction));
		
		Vector velocity = new Vector(xVel, yVel);
		
		Polygon polygonA = PolygonFactory.getPolygon(centerX, centerY, radius, direction, sidesOfCollisionBubbles);
//		Polygon polygonB;
		
		for (int yIndex = 0; yIndex < boxes.length; yIndex++) {
			for (int xIndex = 0; xIndex < boxes[yIndex].length; xIndex++) {
				BubbleGridRectangle bubble = getBubbleAt(xIndex, yIndex);
				if (bubble != null) {
//					polygonB = PolygonFactory.getPolygon(bubble.getX() + bubble.getWidth()/2,
//							bubble.getY() + bubble.getHeight()/2,
//							bubble.getHeight() / 2, 0, sidesOfCollisionBubbles);
					PolygonCollisionResult polColRes = CollisionHelper.bubbleCollision(polygonA, bubble, bubble.getWidth(), velocity);
					if (polColRes != null && (polColRes.intersect() || polColRes.willIntersect())) {
                        return polColRes;
                    }
				}
			}
		}
		return null;
	}

	//TODO Replace this method with the new one above
//	public CollisionObject checkForCollision(float centerX, float centerY, float radius, double direction, double distance) {
//
//
//		centerY += 32; // FIXME Constant to solve collision offset
//		for (int yIndex = 0; yIndex < boxes.length; yIndex++) {
//			for (int xIndex = 0; xIndex < boxes[yIndex].length; xIndex++) {
//				if (boxes[yIndex][xIndex] != null) {
//					List<Direction> collidingDirections = checkFourPointCollision(boxes[yIndex][xIndex], centerX, centerY, radius);
//					if (collidingDirections.size() >= 1) {
//						System.out.println("Hit " + collidingDirections.size() + " directions: " + collidingDirections + " Bubbles:"
//								+ boxes[yIndex][xIndex].toString());
//						return new CollisionObject(boxes[yIndex][xIndex], collidingDirections);
//					}
//				}
//			}
//		}
//		return null;
//	}

	private boolean isFirstIteration = true;

	private List<Direction> checkFourPointCollision(BubbleGridRectangle gridBubbleRect, float activeBubbleCenterX,
			float activeBubbleCenterY, float radius) {
		List<Direction> collidingDirections = new ArrayList<Direction>(2);
		float wX = activeBubbleCenterX - radius;
		float wY = activeBubbleCenterY;
		float nwX = activeBubbleCenterX + radius * (float) Math.cos(Math.toRadians(135));
		float nwY = activeBubbleCenterY + radius * (float) Math.sin(Math.toRadians(135));
		float nX = activeBubbleCenterX;
		float nY = activeBubbleCenterY + radius;
		float neX = activeBubbleCenterX + radius * (float) Math.cos(Math.toRadians(45));
		float neY = activeBubbleCenterY + radius * (float) Math.sin(Math.toRadians(45));
		float eX = activeBubbleCenterX + radius;
		float eY = activeBubbleCenterY;
		if (isFirstIteration) { // Kun til debugging
			isFirstIteration = false;
			// System.out.println("west(" + wX + ", " + wY + ")");
			// System.out.println("northwest(" + nwX + ", " + nwY + ")");
			// System.out.println("north(" + nX + ", " + nY + ")");
			// System.out.println("northeast(" + neX + ", " + neY + ")");
			// System.out.println("east(" + eX + ", " + eY + ")");
		}
		// .overlapsLowerHalf() har erstattet .contains()
		if (gridBubbleRect.overlapsLowerHalf(wX, wY)) {
			collidingDirections.add(Direction.W);
		}
		if (gridBubbleRect.overlapsLowerHalf(nwX, nwY)) {
			collidingDirections.add(Direction.NW);
		}
		if (gridBubbleRect.overlapsLowerHalf(neX, neY)) {
			collidingDirections.add(Direction.NE);
		}
		if (gridBubbleRect.overlapsLowerHalf(eX, eY)) {
			collidingDirections.add(Direction.E);
		}
		return collidingDirections;
		// if (gridBubbleRect.overlapsLowerHalf(wX, wY) || // west point
		// gridBubbleRect.overlapsLowerHalf(nwX, nwY) || // northwest point
		// gridBubbleRect.overlapsLowerHalf(nX, nY) || // north point
		// gridBubbleRect.overlapsLowerHalf(neX, neY) || // northeast point
		// gridBubbleRect.overlapsLowerHalf(eX, eY)) { // east point
		// return true;
		// }
		// return false;
	}

	// public void setOccupied(float x) {
	// int iPos = (int) (x / 32);
	// boxes[iPos][0].setOccupied(true);
	// }

	public ArrayList<AnimatedSprite> getBubbles() {
		ArrayList<AnimatedSprite> results = new ArrayList<AnimatedSprite>(boxes.length * boxes[0].length);
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				if (boxes[i][j] != null) {
					results.add(boxes[i][j].getBubble());
				}
			}
		}
		return results;
	}

	public List<BubbleGridRectangle> getHangingBubbles(int fromRow) {
		List<BubbleGridRectangle> hangingBubbles = new ArrayList<BubbleGridRectangle>();
		List<BubbleGridRectangle> checkedBubbles = new ArrayList<BubbleGridRectangle>();
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				BubbleGridRectangle bubble = boxes[i][j];
				ArrayList<BubbleGridRectangle> hangingCluster = new ArrayList<BubbleGridRectangle>();
				if (bubble != null) {
					if (!checkedBubbles.contains(bubble)) {
						hangingCluster = isHangingBubble(bubble, new ArrayList<BubbleGridRectangle>());
						checkedBubbles.add(bubble);
					}
				}

				if (hangingCluster.size() > 0) {
					for (BubbleGridRectangle bubbleGridRectangle : hangingCluster) {
						if (!hangingBubbles.contains(bubbleGridRectangle)) {
							hangingBubbles.add(bubbleGridRectangle);
						}
					}
				}
			}
		}
		return hangingBubbles;
	}

	private ArrayList<BubbleGridRectangle> isHangingBubble(BubbleGridRectangle bubble, ArrayList<BubbleGridRectangle> bubblesChecked) {
		BubbleGridRectangle leftParent = getLeftParentOfBubble(bubble);
		BubbleGridRectangle rightParent = getRightParentOfBubble(bubble);
		BubbleGridRectangle leftSibling = getLeftSiblingOfBubble(bubble);
		BubbleGridRectangle rightSibling = getRightSiblingOfBubble(bubble);
		BubbleGridRectangle leftChild = getLeftChildOfBubble(bubble);
		BubbleGridRectangle rightChild = getRightChildOfBubble(bubble);

		if (bubble != null) {
			bubblesChecked.add(bubble);
		}

		if ((leftParent != null && leftParent.getCoordinateY() == 0) || (rightParent != null && rightParent.getCoordinateY() == 0)
				|| (leftSibling != null && leftSibling.getCoordinateY() == 0)
				|| (rightSibling != null && rightSibling.getCoordinateY() == 0) || (leftChild != null && leftChild.getCoordinateY() == 0)
				|| (rightChild != null && rightChild.getCoordinateY() == 0) || bubble.getCoordinateY() == 0) {
			return new ArrayList<BubbleGridRectangle>();
		}

		if (leftParent != null && !bubblesChecked.contains(leftParent)) { // Is
																			// attached
																			// to
																			// the
																			// top
			ArrayList<BubbleGridRectangle> hangingCluster = isHangingBubble(leftParent, bubblesChecked);
			if (hangingCluster.size() == 0) {
				return new ArrayList<BubbleGridRectangle>();
			}
		}

		if (rightParent != null && !bubblesChecked.contains(rightParent)) { // Is
																			// attached
																			// to
																			// the
																			// top
			ArrayList<BubbleGridRectangle> hangingCluster = isHangingBubble(rightParent, bubblesChecked);
			if (hangingCluster.size() == 0) {
				return new ArrayList<BubbleGridRectangle>();
			}
		}

		if (leftSibling != null && !bubblesChecked.contains(leftSibling)) {
			ArrayList<BubbleGridRectangle> hangingCluster = isHangingBubble(leftSibling, bubblesChecked);
			if (hangingCluster.size() == 0) {
				return new ArrayList<BubbleGridRectangle>();
			}
		}

		if (rightSibling != null && !bubblesChecked.contains(rightSibling)) {
			ArrayList<BubbleGridRectangle> hangingCluster = isHangingBubble(rightSibling, bubblesChecked);
			if (hangingCluster.size() == 0) {
				return new ArrayList<BubbleGridRectangle>();
			}
		}

		if (leftChild != null && !bubblesChecked.contains(leftChild)) {
			ArrayList<BubbleGridRectangle> hangingCluster = isHangingBubble(leftChild, bubblesChecked);
			if (hangingCluster.size() == 0) {
				return new ArrayList<BubbleGridRectangle>();
			}
		}

		if (rightChild != null && !bubblesChecked.contains(rightChild)) {
			ArrayList<BubbleGridRectangle> hangingCluster = isHangingBubble(rightChild, bubblesChecked);
			if (hangingCluster.size() == 0) {
				return new ArrayList<BubbleGridRectangle>();
			}
		}

		return bubblesChecked;
	}

	public List<BubbleGridRectangle> getNeighboursOfBubble(BubbleGridRectangle ofBubble) {
		List<BubbleGridRectangle> res = new ArrayList<BubbleGridRectangle>();
		res.add(getLeftParentOfBubble(ofBubble));
		res.add(getRightParentOfBubble(ofBubble));
		res.add(getLeftSiblingOfBubble(ofBubble));
		res.add(getRightSiblingOfBubble(ofBubble));
		res.add(getLeftChildOfBubble(ofBubble));
		res.add(getRightChildOfBubble(ofBubble));
		return res;
	}

	public BubbleGridRectangle getLeftParentOfBubble(BubbleGridRectangle ofBubble) {
		if (!isValidBubble(ofBubble))
			return null;

		int bcX = ofBubble.getCoordinateX();
		int bcY = ofBubble.getCoordinateY();

		int leftParentX = (bcY % 2 == 0 ? bcX - 1 : bcX);
		int leftParentY = bcY - 1;

		if (!isInsideGridCoordinates(leftParentX, leftParentY))
			return null;
		return boxes[leftParentY][leftParentX];
	}

	public BubbleGridRectangle getRightParentOfBubble(BubbleGridRectangle ofBubble) {
		if (!isValidBubble(ofBubble))
			return null;

		int bcX = ofBubble.getCoordinateX();
		int bcY = ofBubble.getCoordinateY();

		int rightParentX = (bcY % 2 == 0 ? bcX : bcX + 1);
		int rightParentY = bcY - 1;

		if (!isInsideGridCoordinates(rightParentX, rightParentY))
			return null;
		return boxes[rightParentY][rightParentX];
	}

	public BubbleGridRectangle getLeftSiblingOfBubble(BubbleGridRectangle ofBubble) {
		if (!isValidBubble(ofBubble))
			return null;

		int bcX = ofBubble.getCoordinateX();
		int bcY = ofBubble.getCoordinateY();

		int leftSiblingX = bcX - 1;
		int leftSiblingY = bcY;

		if (!isInsideGridCoordinates(leftSiblingX, leftSiblingY))
			return null;
		return boxes[leftSiblingY][leftSiblingX];
	}

	public BubbleGridRectangle getRightSiblingOfBubble(BubbleGridRectangle ofBubble) {
		if (!isValidBubble(ofBubble))
			return null;

		int bcX = ofBubble.getCoordinateX();
		int bcY = ofBubble.getCoordinateY();

		int rightSiblingX = bcX + 1;
		int rightSiblingY = bcY;

		if (!isInsideGridCoordinates(rightSiblingX, rightSiblingY))
			return null;
		return boxes[rightSiblingY][rightSiblingX];
	}

	public BubbleGridRectangle getLeftChildOfBubble(BubbleGridRectangle ofBubble) {
		if (!isValidBubble(ofBubble))
			return null;

		int bcX = ofBubble.getCoordinateX();
		int bcY = ofBubble.getCoordinateY();

		int leftChildX = (bcY % 2 == 0 ? bcX - 1 : bcX);
		int leftChildY = bcY + 1;

		if (!isInsideGridCoordinates(leftChildX, leftChildY))
			return null;
		return boxes[leftChildY][leftChildX];
	}

	public BubbleGridRectangle getRightChildOfBubble(BubbleGridRectangle ofBubble) {
		if (!isValidBubble(ofBubble))
			return null;

		int bcX = ofBubble.getCoordinateX();
		int bcY = ofBubble.getCoordinateY();

		int rightChildX = (bcY % 2 == 0 ? bcX : bcX + 1);
		int rightChildY = bcY + 1;

		if (!isInsideGridCoordinates(rightChildX, rightChildY))
			return null;
		return boxes[rightChildY][rightChildX];
	}

	private boolean isValidBubble(BubbleGridRectangle bubble) {
		if (bubble == null)
			return false;
		if (!isInsideGridCoordinates(bubble.getCoordinateX(), bubble.getCoordinateY()))
			return false; // Bubble doesn't exist in grid
		return true;
	}

	public boolean isInsideGridCoordinates(int coordX, int coordY) {
		return coordX >= 0 && coordY >= 0 && coordX < boxes[0].length && coordY < boxes.length;
	}

	@SuppressWarnings("unused")
	private void printBubbleGrid() {
		for (int i = 0; i < boxes.length; i++) {
			for (int j = 0; j < boxes[i].length; j++) {
				System.out.print(boxes[i][j]);
				System.out.print(", ");
			}
			System.out.println();
		}
	}

	public int getGridWidth() {
		return gridWidth;
	}

    public BubbleTexture[] getBubbleTextures(){
        return this.bubbleTextures;
    }

	public BubbleGridRectangle getBubbleAt(int coordinateX, int coordinateY) {
		return boxes[coordinateY][coordinateX];
	}

	public void removeBubbleAt(int coordinateX, int coordinateY) {
		boxes[coordinateY][coordinateX] = null;
	}

	public void insertNewRow() {
		moveRowsOneLine();
	}

	private void moveRowsOneLine() {
		for (int yIndex = boxes.length - 1; yIndex >= 0; yIndex--) {
			for (int xIndex = 0; xIndex < boxes[yIndex].length; xIndex++) {
				BubbleGridRectangle bubble = getBubbleAt(xIndex, yIndex);
				if (bubble != null) {
					bubble.setCoordinateY(bubble.getCoordinateY() + 1);
					if (yIndex < gridHeight - 1) {
						boxes[yIndex + 1][xIndex] = bubble;
						boxes[yIndex][xIndex] = null;
					}
				}
			}
		}
		populate(bubbleTextures, true);
	}

	public BubbleGridRectangle placeBubble(BubbleTexture bubbleTexture, int coordXOfBubbleToPlace, int coordYOfBubbleToPlace) {
		int offSetX = coordYOfBubbleToPlace % 2 == 0 ? 0 : 16;
        GameRuler.setCurrentRowCount(coordYOfBubbleToPlace);
		return boxes[coordYOfBubbleToPlace][coordXOfBubbleToPlace] = new BubbleGridRectangle(coordXOfBubbleToPlace * 32
				+ (coordXOfBubbleToPlace * marginX) + offSetX, coordYOfBubbleToPlace * -32 + 800 - (coordYOfBubbleToPlace * -marginY), 32,
				32, coordXOfBubbleToPlace, coordYOfBubbleToPlace, bubbleTexture);
	}

    public BubbleGridRectangle placeBubbleOnCeiling(BubbleTexture bubbleTexture, float x) {
        float tmp = x/(32 + marginX);
        int gridCoordinateX = (int) tmp;

        return placeBubble(bubbleTexture, gridCoordinateX, 0);
    }

    public void removeColor(BubbleTexture.BubbleColor color){
        for (int i = 0; i < allowedBubbleTextures.size(); i++) {
            BubbleTexture bubbleTexture = allowedBubbleTextures.get(i);
            if (bubbleTexture.getColor() == color){
                System.out.println("Removing color: "+color.toString());
                allowedBubbleTextures.remove(i);
            }

        }
    }

    public BubbleQueueList getQueuedBubbles() {
        return queuedBubbles;
    }

    public void setQueuedBubbles(BubbleQueueList queuedBubbles) {
        this.queuedBubbles = queuedBubbles;
    }

    public List<BubbleTexture> getAllowedBubbleTextures() {
        return allowedBubbleTextures;
    }
}