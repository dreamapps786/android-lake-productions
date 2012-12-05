package com.model;

import com.badlogic.gdx.math.Rectangle;
import com.helpers.extensions.BubbleTexture;
import com.helpers.extensions.BubbleTexture.BubbleColor;
import com.simulation.AnimatedBubbleSprite;

@SuppressWarnings("serial")
public class BubbleGridRectangle extends Rectangle {
	// private Rectangle rectangle;
	private AnimatedBubbleSprite bubble;
	private BubbleColor color;
	private boolean isOccupied;
	private int coordinateX;
	private int coordinateY;
	private float gridPosY;

	public BubbleGridRectangle(float x, float y, float width, float height, int coordinateX, int coordinateY,
			BubbleTexture bubbleTexture) {
		super(x, y, width, height);
		this.gridPosY = 0 + 800 - bubbleTexture.getHeight();
		this.coordinateX = coordinateX;
		this.coordinateY = coordinateY;
		bubble = new AnimatedBubbleSprite("bubble" + x / 32 + "-" + y / 32, bubbleTexture, (int) (x),
				(int) (gridPosY + y + height), bubbleTexture.getWidth(), bubbleTexture.getHeight(), 0, 0, 0f, 0f);
		isOccupied = false;
		color = bubbleTexture.getColor();
	}

	public boolean overlapsLowerHalf(float x, float y) {
		float topY = this.y + (this.height / 2);
		float bottomY = this.y;
		float leftX = this.x;
		float rightX = this.x + this.width;
		boolean insideX = x > leftX && x < rightX;
		boolean insideY = y > bottomY && y < topY;
		return insideX && insideY;
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	public AnimatedBubbleSprite getBubble() {
		return bubble;
	}

	public BubbleColor getColor() {
		return color;
	}

	public int getCoordinateX() {
		return coordinateX;
	}

	public void setCoordinateX(int coordinateX) {
		this.coordinateX = coordinateX;
	}

	public int getCoordinateY() {
		return coordinateY;
	}

	public void setCoordinateY(int coordinateY) {
		this.coordinateY = coordinateY;
	}

	public void placeBubble(BubbleTexture color) {
		setOccupied(true);
		setBubbleTexture(color);
	}

	public void setBubbleTexture(BubbleTexture bt) {
		System.out.println("setting bubbleTexture from "+this.getBubble().getBubbleTexture().getColor()+" to "+bt.getColor());
		this.getBubble().setBubbleTexture(bt);
		this.color = bt.getColor();
	}

	@Override
	public String toString() {
		return "[X: " + this.x + " Y: " + this.y + "]" + " Coordinates[" + this.coordinateX + "," + this.coordinateY + "]";
	}

}
