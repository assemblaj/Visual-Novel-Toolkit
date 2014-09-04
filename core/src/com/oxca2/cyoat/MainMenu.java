package com.oxca2.cyoat;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;

// menuItem object
// String name
// command on click 
// Rectangle 
// 

// Menu Object 
// Prompt (optional)
// menuX, menuY, space, itemHeight, itemWidth;
// paddingV, paddingH
// for question answer type prompts, offset is always the length of the line 
// mousePos vector
//
// Shape renderer if it draws rectangle  around the option
// otherwise it just colors it, either way it eeds a color
//
// This will need a pluggable batch
public class MainMenu implements Screen, InputProcessor {
	final Main game;
	String[] items = {"Go to the park. ", "Go home. ", "Do nothing. ", "Whatever. "};
	Array<Rectangle> menuItems;
	//OrthographicCamera camera;
	int menuX = 25, menuY = 100;
	int space = 5;
	int itemHeight = 20, itemWidth = 300;
	int paddingV = 5, paddingH = 5;
	float offset;
	Vector3 mousePos;
	Menu menu;
	ShapeRenderer shape;
	
	public MainMenu(final Main game) {
		this.game = game;
		mousePos = new Vector3();
		
		menuItems = new Array<Rectangle>();
		//camera = new OrthographicCamera();
		//camera.setToOrtho(false, HellStories.WIDTH, HellStories.HEIGHT);
		int totalHeight = (int) game.fonts.get("vs_f5").getLineHeight() * items.length + menuY ;
		
		itemHeight = (int) game.fonts.get("vs_f5").getLineHeight();
		offset =  game.fonts.get("vs_f5").getLineHeight();
		
		int totalY =  menuY + ( items.length * itemHeight) + (items.length * space);
		
		for (int i = 0; i < items.length; i++){
			makeItem(totalY);
			totalY -= space;
			totalY -= itemHeight;
		}
		System.out.println("TOTALY: " + totalY);
		int increment = menuY;
		
		// menuY + i*itemHeight
		for (int i = 0; i < items.length; i++){
			increment += itemHeight;
			//makeItem(increment);
			increment += space;
		}
		System.out.println("INCREMENT: " + increment);
		System.out.println("GUESS: " + (menuY + ( items.length * itemHeight) + (items.length * space)));
		shape = new ShapeRenderer();
		Iterator<Rectangle> iter =  menuItems.iterator();
		//Gdx.input.setInputProcessor(this);
		
		menu = new Menu(
			game, space, 
			menuX, menuY, 
			itemHeight, itemWidth, 
			paddingV, paddingH, offset);
		menu.add(menu.new MenuItem() {
			public void runCommand() {
				game.setScreen(new SceneScreen(game, "test1"));
			}
		}.initialize("Hello "));
		
		menu.add(menu.new MenuItem() {
			public void runCommand() {
				System.out.println("I'm here");
			}
		}.initialize("Name 2 "));
		
		menu.add(menu.new MenuItem() {
			public void runCommand() {
				Gdx.app.exit();
			}
		}.initialize("Exit program. "));		
		
		menu.layoutMenu();
		Gdx.input.setInputProcessor(menu);
	}
	
	public void makeItem(int pos) {
		Rectangle item = new Rectangle();
		item.x = menuX; 
		item.y = pos;
		item.width = itemWidth;
		item.height = (int) game.fonts.get("vs_f5").getLineHeight();
		menuItems.add(item);
		
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.camera.update();
		
		game.batch.begin();/*
		for (int i = 0; i < items.length; i++) {			
			mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			game.camera.unproject(mousePos);
			
			/*
			if (menuItems.get(i).contains(mousePos.x, mousePos.y)){				
				game.fonts.get("vs_f5").setColor(Color.RED);;
				System.out.println("Mouse x: " + Gdx.input.getX() + "\nMouse y: " +  Gdx.input.getY() );
				System.out.println();
			}else
			game.fonts.get("vs_f5").setColor(Color.WHITE);*/
			/*
			// Need to add the lineLength due to font origin at top left whereas
			// rectangle origin is a bottom right.
			game.fonts.get("vs_f5").draw(game.batch, "What would you like to do?", 
					menuItems.get(i).x, 270 + offset);	
			//items.length-1-i
			game.fonts.get("vs_f5").draw(game.batch, items[i],
					menuItems.get(i).x, menuItems.get(i).y + offset);		*/
			
			//(menuItems.get(i).x + itemWidth / 2) - 50
		//}
		menu.draw();
		game.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	//----input stuff 
	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}
	
	;
	int selectedItem;
	boolean drawHoveredItem;
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
