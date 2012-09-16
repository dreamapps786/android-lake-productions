package com.simulation;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimatedSprite extends Sprite implements ITouchable {
	private String name;
	protected int rowCount;
	protected int columnCount;
	protected float animationRateInSeconds = 0.02f;
	protected boolean isPlay = false;
	protected int curFrameIndex = -1;
	protected int startX;
	protected int startY;
	protected float frameTimeCounter;
	private float xRel2Screen;
	private float yRel2Screen;
	private List<TextureRegion> frameRegions;
	private boolean active = true;
	private double direction;

	public AnimatedSprite(String name, final Texture texture, final int srcX,
			final int scrY, final int tileWidth, final int tileHeight,
			final int rowCount, final int columnCount, float xRel2Screen,
			float yRel2Screen) {
		super(texture, srcX, scrY, tileWidth, tileHeight);
		this.name = name;
		this.frameRegions = new ArrayList<TextureRegion>(rowCount*columnCount);
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

	public void setAnimationRate(final float animationRateInSeconds) {
		this.animationRateInSeconds = animationRateInSeconds;
	}

	public void play() {
		isPlay = true;
		frameTimeCounter = 0;
	}

	public void pause() {
		isPlay = false;
		frameTimeCounter = 0;
	}

	public void goToFrame(int frameIndex) {
		setRegion(frameRegions.get(frameIndex));
	}

	public void update(final float secondsElapsed) {
		if (isPlay) {
			this.frameTimeCounter += secondsElapsed;
			if (frameTimeCounter > animationRateInSeconds) {
				frameTimeCounter = 0;
				curFrameIndex = (curFrameIndex + 1) % (rowCount * columnCount); // Sørger for at animationen starter forfra
				goToFrame(curFrameIndex);

				if (curFrameIndex == rowCount * columnCount - 1) {
					pause();
					curFrameIndex = -1;
				}
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
	
	public void setYRel2Screen(float yRel2Screen){
		this.yRel2Screen = yRel2Screen;
	}

	public String getName() {
		return name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isPlay() {
		return isPlay;
	}

	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}
}