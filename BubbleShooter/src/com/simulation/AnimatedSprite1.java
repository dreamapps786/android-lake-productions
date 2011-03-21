package com.simulation;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class AnimatedSprite1 extends Sprite {
	protected TextureRegion region;

	protected List<TextureRegion> frameRegions;
	protected int currentFrame;

	protected float frameRate;
	protected float totalDeltaTime;
	protected boolean isPlaying;
	protected boolean isLooping;
	protected boolean isReversed;

	protected enum PlayType {
		RANDOM, NORMAL, PINGPONG, REVERSED
	};

	protected enum LoopType {
		NORMAL, PINGPONG
	};
	private LoopType looptype = LoopType.NORMAL;
	private PlayType playtype = PlayType.NORMAL;

	public boolean isAtlasSprite;

	public final boolean looping() {
		return isLooping;
	}

	/*
	 * Initialize
	 */
	private void Init() {
		frameRate = 1.0f;
		isPlaying = false;
		isLooping = true;
		isReversed = false;

		currentFrame = 0;

		frameRegions = new ArrayList<TextureRegion>();
	}

	// Initialize our textureregion (too bad the super constructor must be the
	// first call in overloaded constructors in java,
	// because it's the same code as in the super constructor. Maybe badlogic
	// can create a protected init in the sprite class ?)
	private void CreateSprite(final TextureRegion region) {
		setRegion(region);

		if (isAtlasSprite) {
			AtlasRegion atlasRegion = (AtlasRegion) region;
			if (atlasRegion.rotate)
				rotate90(true);
			setOrigin(atlasRegion.originalWidth / 2,
					atlasRegion.originalHeight / 2);
			super.setBounds(atlasRegion.offsetX, atlasRegion.offsetY,
					Math.abs(atlasRegion.getRegionWidth()),
					Math.abs(atlasRegion.getRegionHeight()));
		}

		setColor(1, 1, 1, 1);

		setSize(Math.abs(region.getRegionWidth()),
				Math.abs(region.getRegionHeight()));
		setOrigin(getWidth() / 2, getHeight() / 2);

		// Set our playhead at the correct position
		stop();
	}

	/*
	 * Create an animated sprite, based on texture-atlas content.
	 * 
	 * @param textureAtlas Reference to the TextureAtlas instance
	 */
	public AnimatedSprite1(final TextureAtlas textureAtlas) {
		Init();

		List<AtlasRegion> atlasFrameRegions = textureAtlas.getRegions();
		
		for (AtlasRegion rg : atlasFrameRegions)
			frameRegions.add((TextureRegion) rg);

		// Initialize our first frame
		CreateSprite(frameRegions.get(0));
	}

	/*
	 * Create an animated sprite, based on a filehandle
	 * 
	 * @param file File to load
	 * 
	 * @param tileWidth Width of each frame tile
	 * 
	 * @param tileHeight Height of each frame tile
	 */
	public AnimatedSprite1(final FileHandle file, final int tileWidth,
			final int tileHeight) {
		Texture texture = new Texture(file);
		CreateAnimatedSpriteFromTexture(texture, tileWidth, tileHeight);
	}

	/*
	 * Create an animated sprite, based on an existing texture instance
	 * 
	 * @param texture Reference to the texture instance
	 * 
	 * @param tileWidth Width of each frame tile
	 * 
	 * @param tileHeight Height of each frame tile
	 */
	public AnimatedSprite1(final Texture texture, final int tileWidth,
			final int tileHeight) {
		CreateAnimatedSpriteFromTexture(texture, tileWidth, tileHeight);
	}

	/*
	 * Wrapper for our texture based constructors.
	 * 
	 * @param texture Reference to the texture instance
	 * 
	 * @param tileWidth Width of each frame tile
	 * 
	 * @param tileHeight Height of each frame tile
	 */
	private void CreateAnimatedSpriteFromTexture(final Texture texture,
			final int tileWidth, final int tileHeight) {
		Init();

		// Split the texture into tiles (from upper left corner to bottom right
		// corner)
		TextureRegion[][] tiles = TextureRegion.split(texture, tileWidth,
				tileHeight);

		// Now unwrap the two dimensional array into a single array with all the
		// tiles in the correct animation order.
		for (int row = 0; row < tiles.length; row++) {
			for (int col = 0; col < tiles[row].length; col++) {
				frameRegions.add(tiles[row][col]);
			}
		}

		// Initialize our first frame
		CreateSprite(frameRegions.get(0));
	}

	/*
	 * Set at what rate the animated sprite should be playing.
	 * 
	 * @param fps Framerate in frames per second
	 */
	public void setAnimationRate(final int fps) {
		frameRate = 1.0f / (float) fps;
	}

	/*
	 * Loop the animation
	 * 
	 * @param isLooping If true: the animation plays infinite
	 */
	public void loop(boolean isLooping) {
		loop(isLooping, LoopType.NORMAL);
	}

	/*
	 * Loop the animation (use special looping abilities)
	 * 
	 * @param isLooping If true: the animation plays infinite
	 * 
	 * @param loopType NORMAL: last frame ? start at frame 0. PINGPONG: last
	 * frame ? reverse animation.
	 */
	public void loop(boolean isLooping, LoopType loopType) {
		this.looptype = loopType;
		this.isLooping = isLooping;
	}

	/*
	 * Stop (and therefore rewind and play)
	 */
	public void restart() {
		stop();
		play();
	}

	/*
	 * Start or resume animation
	 */
	public void play() {
		play(PlayType.NORMAL);
	}

	/*
	 * Start or resume animation (use special playing abilities)
	 * 
	 * @param playType NORMAL: start at first frame. REVERSED: start at last
	 * frame. RANDOM: pick a random frame.
	 */
	public void play(PlayType playType) {
		this.playtype = playType;
		isReversed = this.playtype == PlayType.REVERSED;
		isPlaying = true;
		totalDeltaTime = 0;
	}

	/*
	 * Pauses animation
	 */
	public void pause() {
		isPlaying = false;
		totalDeltaTime = 0;
	}

	/*
	 * Stops and rewinds animation.
	 */
	public void stop() {
		pause();
		currentFrame = getFirstFrameNumber();
	}

	/*
	 * Gets the index of the last frame.
	 * 
	 * @return Returns 0 or last frame index.
	 */
	private int getFirstFrameNumber() {
		return (this.playtype == PlayType.REVERSED ? frameRegions
				.size() - 1 : 0);
	}

	/*
	 * Updates and increments the animation playhead to the next frame
	 * 
	 * @param deltaTime Frame duration in seconds.
	 */
	public void update(final float deltaTime) {
		if (isPlaying) {
			totalDeltaTime += deltaTime;
			if (totalDeltaTime > frameRate) {
				totalDeltaTime = 0;

				if (this.playtype == PlayType.RANDOM) {
					currentFrame = (int) Math.round(Math.random()
							* (frameRegions.size() - 1));
				} else {
					currentFrame += (isReversed ? -1 : 1);

					// Back to first
					if (currentFrame >= frameRegions.size() || currentFrame < 0) {
						// If not looping, stop
						if (!isLooping) {
							stop();
						} else {
							switch (looptype) {
							case PINGPONG:
								isReversed = !isReversed;
								if (isReversed) {
									currentFrame = frameRegions.size() - 2;
									if (currentFrame < 0)
										currentFrame = 0;
								} else
									currentFrame = 0;

								break;
							case NORMAL:
							default:
								currentFrame = getFirstFrameNumber();
								break;
							}

						}
					}
				}

				TextureRegion region = frameRegions.get(currentFrame);
				// set the region
				setRegion(region);

				// Give an AtlasRegion some special attention (boundaries may be
				// trimmed)
				if (isAtlasSprite) {
					// Set the region and make sure the sprite is set at the
					// original cell size
					AtlasRegion atlasRegion = (AtlasRegion) region;
					if (atlasRegion.rotate)
						rotate90(true);
					setOrigin(atlasRegion.originalWidth / 2,
							atlasRegion.originalHeight / 2);
					super.setBounds(atlasRegion.offsetX, atlasRegion.offsetY,
							Math.abs(atlasRegion.getRegionWidth()),
							Math.abs(atlasRegion.getRegionHeight()));
				}

				setColor(1, 1, 1, 1);
			}
		}
	}

	public void setRegion(TextureRegion region) {
		isAtlasSprite = region instanceof AtlasRegion;
		this.region = region;
		super.setRegion(region);
	}

	public void setPosition(float x, float y) {
		if (isAtlasSprite)
			super.setPosition(x + ((AtlasRegion) region).offsetX, y
					+ ((AtlasRegion) region).offsetY);
		else
			super.setPosition(x, y);

	}

	public void setBounds(float x, float y, float width, float height) {
		if (isAtlasSprite)
			super.setBounds(x + ((AtlasRegion) region).offsetX, y
					+ ((AtlasRegion) region).offsetY, width, height);
		else
			super.setBounds(x, y, width, height);
	}

	public void setOrigin(float originX, float originY) {
		if (isAtlasSprite)
			super.setOrigin(originX + ((AtlasRegion) region).offsetX, originY
					+ ((AtlasRegion) region).offsetY);
		else
			super.setOrigin(originX, originY);
	}

	public void flip(boolean x, boolean y) {
		// Flip texture.
		super.flip(x, y);

		if (isAtlasSprite) {
			float oldOffsetX = ((AtlasRegion) region).offsetX;
			float oldOffsetY = ((AtlasRegion) region).offsetY;
			// Update x and y offsets.
			((AtlasRegion) region).flip(x, y);

			// Update position with new offsets.
			translate(((AtlasRegion) region).offsetX - oldOffsetX,
					((AtlasRegion) region).offsetY - oldOffsetY);
		}
	}

	public float getX() {
		return isAtlasSprite ? super.getX() - ((AtlasRegion) region).offsetX
				: super.getX();
	}

	public float getY() {
		return isAtlasSprite ? super.getY() - ((AtlasRegion) region).offsetY
				: super.getY();
	}
}
