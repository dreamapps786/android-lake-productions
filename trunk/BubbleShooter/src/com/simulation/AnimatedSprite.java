package com.simulation;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimatedSprite extends Sprite implements ITouchable {
	protected int rowCount;
	protected int columnCount;
	protected float animationRateInSeconds = 0.02f;
	protected boolean isPlay = false;
	protected int curRow;
	protected int curColumn;
	protected int startX;
	protected int startY;
	protected float frameTimeCounter;
	private float xRel2Screen;
	private float yRel2Screen;
	private List<TextureRegion> frameRegions;

	public AnimatedSprite(final Texture texture, final int srcX,
			final int scrY, final int tileWidth, final int tileHeight,
			final int rowCount, final int columnCount, float xRel2Screen, float yRel2Screen) {
		super(texture, srcX, scrY, tileWidth, tileHeight);
		this.frameRegions = new ArrayList<TextureRegion>();
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.startX = srcX;
		this.startY = scrY;
		this.xRel2Screen = xRel2Screen;
		this.yRel2Screen = yRel2Screen;
		addFrames(texture);
	}

	public void addFrames(Texture texture) {
		for (int column = 0; column < columnCount; column++) {
			for (int row = 0; row < rowCount; row++) {
				frameRegions.add(new TextureRegion(texture, startX + column
						* (int) getWidth(), startY + row * (int) getHeight(),
						(int) getWidth(), (int) getHeight()));
			}
		}
	}

	public void setAnimationRate(final float pAnimationRateInSeconds) {
		animationRateInSeconds = pAnimationRateInSeconds;
	}

	public void play() {
		isPlay = true;
		frameTimeCounter = 0;
	}

	public void pause() {
		isPlay = false;
		frameTimeCounter = 0;
	}

	public void goToFrame(int pFrameRow, int pFrameColumn) {
		setRegion(frameRegions.get(pFrameColumn));
		if (pFrameColumn == rowCount - 1) {
			pause();
		}
	}

	public void update(final float secondsElapsed) {
		if (isPlay) {
			this.frameTimeCounter += secondsElapsed;
			if (frameTimeCounter > animationRateInSeconds) {
				frameTimeCounter = 0;
				++curColumn;
				if (curColumn == columnCount) {
					curRow = ++curRow % rowCount;
				}
				curColumn = curColumn % columnCount;
				goToFrame(curColumn, curRow);
			}
		}
	}

	@Override
	public void isTouched() {
		play();
	}

	public float getXrel2Screen() {
		return xRel2Screen;
	}

	public float getYrel2Screen() {
		return yRel2Screen;
	}
}