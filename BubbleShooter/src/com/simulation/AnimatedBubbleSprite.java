package com.simulation;

import com.helpers.extensions.BubbleTexture;

public class AnimatedBubbleSprite extends AnimatedSprite{
	private BubbleTexture bubbleTexture;

	public AnimatedBubbleSprite(String name, BubbleTexture texture, int srcX, int scrY, int tileWidth, int tileHeight, int rowCount,
			int columnCount, float xRel2Screen, float yRel2Screen) {
		super(name, texture, srcX, scrY, tileWidth, tileHeight, rowCount, columnCount, xRel2Screen, yRel2Screen);
		this.bubbleTexture = texture;
	}

	public BubbleTexture getBubbleTexture() {
		return bubbleTexture;
	}

	public void setBubbleTexture(BubbleTexture bubbleTexture) {
		this.bubbleTexture = bubbleTexture;
		this.setTexture(bubbleTexture);
	}
	
	@Override
	public String toString() {
		return super.getName();
	}
}
