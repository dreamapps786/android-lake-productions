package com.gui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.math.Vector2;
import com.model.Renderer;
import com.simulation.Button;
import com.simulation.Simulation;
import com.simulation.SimulationListener;

public class MainMenu implements Frame, SimulationListener {
	/** the SpriteBatch used to draw the background, logo and text **/
//	private final SpriteBatch spriteBatch;
//	private final Texture background;
//	private final Texture title;
//	private final Texture play;
//	private final BitmapFont font;
//	private Mesh explosionMesh;
	private final Simulation simulation;
	/** the renderer **/
	private final Renderer renderer;

	/** is done flag **/
	private boolean isDisposable = false;
	/** view & transform matrix **/
//	private final Matrix4 viewMatrix = new Matrix4();
//	private final Matrix4 transformMatrix = new Matrix4();

	public MainMenu(Application app) {
		simulation = new Simulation();
		simulation.listener = this;
		renderer = new Renderer(app);
		
		
//		spriteBatch = new SpriteBatch();
//		background = new Texture(
//				Gdx.files.internal("res/background_portrait.png"));
//		background.setFilter(TextureFilter.Linear, TextureFilter.Linear);
//		title = new Texture(Gdx.files.internal("res/title.png"));
//		title.setFilter(TextureFilter.Linear, TextureFilter.Linear);
//		play = new Texture(Gdx.files.internal("res/btn_play.png"));
//		play.setFilter(TextureFilter.Linear, TextureFilter.Linear);
//
//		font = new BitmapFont();
//		font.setColor(Color.RED);
	}

	@Override
	public void dispose()  {
//		spriteBatch.dispose();
//		background.dispose();
//		title.dispose();
//		play.dispose();
	}

	@Override
	public void update(Application app) {
		simulation.update(app.getGraphics().getDeltaTime());
		if (app.getInput().isTouched()) {
//			System.out.println(app.getInput().getX());
//			System.out.println(app.getInput().getY());
			if (app.getInput().getX() > ((480-512)/2)+103 && app.getInput().getX() < ((480-512)/2)+103+306
					&& app.getInput().getY() > 200
					&& app.getInput().getY() < 282) {
				simulation.button = new Button(new Vector2(-16,306));
				simulation.button1.play();
//				simulation.test.play();
				renderer.renderButtonAnimations(Gdx.gl10, simulation.button1);
//				renderer.renderButtonAnimations(Gdx.gl10, simulation.test);
			}
		}
	}

	@Override
	public void render(Application app) {
		app.getGraphics().getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
		renderer.render(app, simulation);
		
//		app.getGraphics().getGL10().glClear(GL10.GL_COLOR_BUFFER_BIT);
//		viewMatrix.setToOrtho2D(0,0,480,800);
//		spriteBatch.setProjectionMatrix(viewMatrix);
//		spriteBatch.setTransformMatrix(transformMatrix);
//		spriteBatch.begin();
//		spriteBatch.disableBlending();
//		spriteBatch.setColor(Color.WHITE);
		
		//We want to place it in the upper left corner and subtract the whitespace, so it doesn't show
		//draw(element,x-background whitespace,y-background whitespace,width,height,cropX,cropY,prescale res Width,prescale res Height,mirror left, mirror top)
//		spriteBatch.draw(background, 0-272,0-112,1024,1024,0,0, 1024,1024,false,false); 
//		spriteBatch.enableBlending();
		
//		spriteBatch.draw(title,(480-title.getWidth())/2, 630, 256, 256, 0, 0, 256, 256, false,
//				false);
		
		//We want to find the horizontal center of the screen
		//draw(element,(target width-picture width)/2,y,width,height,cropX,cropY,prescale res Width,prescale res Height,mirror left, mirror top)
//		spriteBatch.draw(play,(480-play.getWidth())/2, 306, 512, 512,0,0, 512, 512,false, false);
//		font.draw(spriteBatch, "W: " + Gdx.graphics.getWidth() + " H: "
//				+ Gdx.graphics.getHeight(), 200, 300);
//		spriteBatch.setBlendFunction(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);	
//		spriteBatch.end();
	}

	@Override
	public boolean isDisposable() {
		return isDisposable;
	}

	@Override
	public void playPushed() {
		//TODO Play sound
	}
}
