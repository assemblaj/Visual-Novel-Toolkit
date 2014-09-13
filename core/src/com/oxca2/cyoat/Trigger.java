package com.oxca2.cyoat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Timer.Task;
import com.oxca2.cyoat.GameChoiceMenu.GameChoice;

// dataID
// TriggerID

/*
 * There is a difference between dataID and triggerID
 * 
 * triggerID would be the ID for that specific trigger.
 * This is used to get triggers out of the triggerMap,
 * map triggers to menu items, etc
 * 
 * dataID is used to identify the data that the trigger is working 
 * with, i.e what will be drawn to the screen in a DrawingComand, etc.
 * Multiple triggers can work with the same data, for 
 * example, multiple triggers can work with the same Sprite/Texture.
 * Specifically, dataIDs are used to specify which command either 
 * already works with the data, or the id of the command which 
 * will work with the data.  
 */
public abstract class Trigger {
	int type;
	
	String dataID; 
	String triggerID; //if it's a menu item, it has a unique ID for itself as a menu item. 
	String name;
	int layer;
	int time;
	int line;
	
	
	SceneScreen scene;
	Main game;
	
	abstract void execute();
	
	public void setScene(SceneScreen scene){
		this.scene = scene;
	}
	
	public void setGame(Main game){
		this.game = game;
	}
}

/* The IDs for the Commands are the dataIDs
 * for the triggers
 */
abstract class DrawingCommand {
	SpriteBatch batch;
	Sprite sprite;
	Texture texture;
	String id;
	int layer;
	
	private int x, y;
	private int width, height;
	
	abstract void draw(SpriteBatch batch);
	
	public void setBounds(int x, int y, int width, int height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
}


abstract class MultiTriggerSequence extends Trigger{
	Array<Trigger> triggers = new Array<Trigger>();	
	String[] triggerIDs;
	

	protected void mapIDsToTriggers() {
		ObjectMap<String, Trigger> map = scene.getTriggers();
		
		for (String id  : triggerIDs)
			triggers.add(map.get(id));		
	}
}
	
class StartTimeBasedSequence extends MultiTriggerSequence {
	
	@Override
	void execute() {
		mapIDsToTriggers();
		TimeTriggerHandler.scheduleTriggers(triggers);
	}
	
}

class RunMultipleTriggers extends MultiTriggerSequence {

	
	@Override
	void execute() {
		mapIDsToTriggers();
		for (Trigger trigger: triggers)
			trigger.execute();
	}
		
}

class AddNewBackground extends Trigger {
	String bgPath;
	
	@Override
	void execute() {
		scene.addCommandToLayer(new DrawBackground(layer, dataID, bgPath));
	}
}

class SetBackground extends Trigger {
	String bgPath;
	
	@Override
	void execute() {
		scene.setBackground(layer, dataID, new Texture(Gdx.files.internal(bgPath)));
	}	
}


class RemoveBackground extends Trigger {
	@Override
	void execute() {
		scene.removeBackground(this);
	}
}

class DrawBackground extends DrawingCommand {
	
	public DrawBackground(int layer, String id, String bgPath) {
		this.layer = layer;
		this.id = id;
		texture = new Texture(Gdx.files.internal(bgPath));
		sprite  = new Sprite(texture);
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(sprite, 0, 0, Main.WIDTH, Main.HEIGHT);
	}
}

class AddTextbox extends Trigger {
	private String bgPath;
	private int x, y;
	private int width, height;
	
	@Override
	void execute() {
		scene.addCommandToLayer(
			new DrawTextbox(layer, dataID, bgPath,
			x, y, width, height));
	}
}

class SetTextbox extends Trigger {
	private String bgPath;
	private int x, y;
	private int width, height;
	
	@Override
	void execute() {
		scene.setTextbox(
			layer, dataID, new Texture(Gdx.files.internal(bgPath)), 
			x, y, width, height);
	}
	
}

class RemoveTextbox extends Trigger {

	@Override
	void execute() {
		scene.removeTextbox(this);
	}
	
}

class DrawTextbox extends DrawingCommand {
	private int x, y;
	private int width, height;
	
	public DrawTextbox(
		int layer, String id, String bgPath, 
		int x, int y, int width, int height) 
	{
		this.layer = layer;
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		texture = new Texture(Gdx.files.internal(bgPath));
		sprite  = new Sprite(texture);		
	}
	
	@Override
	void draw(SpriteBatch batch) {
		batch.draw(sprite, x, y, width, height);
	}
}

class AddAnimatedText extends Trigger {
	String[] textArray;
	Trigger[] lineTriggers;
	String font;
	
	int x,  y; 
	int lineLength; 
	int maxLines;
	float speed;

	@Override
	void execute() {
		scene.addCommandToLayer(
				new DrawAnimatedText(layer, dataID, game,
				AnimatedText.join(textArray, " "), lineTriggers,
				font, x, y, lineLength, maxLines, speed));		
	}
}

class RemoveAnimatedText extends Trigger {
	@Override
	void execute() {
		scene.removeAnimatedText(this);
	}
}

class DrawAnimatedText extends DrawingCommand {
	AnimatedText animText;
	LineTriggerObserver observer;
	
	public DrawAnimatedText(
		int layer, String id, Main game,
		String text, Trigger[] lineTriggers,
		String font, int x, int y, int lineLength,
		int maxLine, float speed)
	{
		this.layer = layer;
		this.id = id;
		
		animText = new AnimatedText(
			game, text, font, 
			x, y, lineLength, 
			maxLine, speed			
		);
		
		if (lineTriggers != null){
			observer = new LineTriggerObserver(lineTriggers);
			animText.addObserver(observer);
		}
	}
	
	@Override
	void draw(SpriteBatch batch) {
		animText.draw(batch);
	}
	
	public void clearObservers(){
		animText.deleteObservers();
	}
}

class AddStaticText extends Trigger {
	String text;
	int x, y;
	String font;
	
	@Override
	void execute() {
		scene.addCommandToLayer(
			new DrawStaticText(
			game, layer, dataID, text, x, y, font));
	}
	
}

class RemoveStaticText extends Trigger {

	@Override
	void execute() {
		scene.removeStaticText(this);
	}
	
}

class DrawStaticText extends DrawingCommand {
	String text;
	int x, y;
	String font;
	BitmapFont bFont;
	
	public DrawStaticText(
		Main game,  int layer, String id,
		String text, int x, int y, String font)
	{
		this.layer = layer;
		this.id = id;
		
		this.text = text;
		this.x = x;
		this.y = y;
		this.font = font;
		
		bFont = game.fonts.get(font);
	}
	
	
	@Override
	void draw(SpriteBatch batch) {
		// TODO Auto-generated method stub
		bFont.draw(batch, text, x, y);
	}
	
}

class AddGameChoiceMenu extends Trigger {
	int space,  menuX,  menuY;
	int itemHeight, itemWidth, paddingV,  paddingH;
	float offset;
	String prompt; 
	String font; 
	String[] itemIDs;
	
	@Override
	void execute() {
		offset = game.fonts.get(font).getLineHeight();
		
		scene.addCommandToLayer(
				new DrawGameChoiceMenu(layer, dataID, game, space, menuX, menuY,
				    itemHeight, itemWidth,  paddingV, paddingH,
				    offset, prompt, font, itemIDs, scene));				
	}
	
	
}

class RemoveGameChoiceMenu extends Trigger {

	@Override
	void execute() {
		
		
	}
	
}

class DrawGameChoiceMenu extends DrawingCommand {
	GameChoiceMenu menu;
	Array<Trigger> menuItems;
	ObjectMap<String, Trigger> map;
	
	public DrawGameChoiceMenu(int layer, String id, Main main,
			int space, int menuX, int menuY,
			int itemHeight, int itemWidth, 
			int paddingV, int paddingH,
			float offset, String prompt, String font, 
			String[] itemIDs, SceneScreen scene)
	{
		this.layer = layer;
		this.id = id;
		menuItems = new Array<Trigger>();
		map = scene.getTriggers();
		
		for (int i = 0; i < itemIDs.length; i++){
			menuItems.add(map.get(itemIDs[i]));
		}
		
		menu = new GameChoiceMenu(main, space, 
			menuX, menuY, itemHeight, itemWidth,
			paddingV, paddingH, offset, prompt, font);
		
		for (Trigger item: menuItems)
			menu.add(menu.new GameChoice("test", item, scene) );
		
		menu.layoutMenu();
		Gdx.input.setInputProcessor(menu);
	}
	
	@Override
	void draw(SpriteBatch batch) {
		menu.draw(batch);		
	}
	
}


