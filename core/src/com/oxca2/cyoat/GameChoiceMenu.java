package com.oxca2.cyoat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameChoiceMenu extends Menu {
	String prompt;
	String font;
	int promptSpace;
	
	public GameChoiceMenu(Main main, int space, int menuX, int menuY,
			int itemHeight, int itemWidth, int paddingV, int paddingH,
			float offset, String prompt, int promptSpace, String font) {
		super(main, space, menuX, menuY, itemHeight, itemWidth, paddingV, paddingH,
				offset);
		this.prompt = prompt;
		this.font = font;
		this.promptSpace = promptSpace;
	}
	
	public void draw(SpriteBatch batch) {
		game.fonts.get(font).setColor(Color.WHITE);
		game.fonts.get(font).drawMultiLine(batch, prompt, 
				menuItems.get(0).bounds.x, menuItems.get(0).bounds.y + promptSpace);	
		super.draw(batch);
	}
	
	
	public class GameChoice extends MenuItem {
		//String[][] triggers;
		SceneScreen scene;
		Trigger trigger;
		
		public GameChoice(String name, Trigger trigger, SceneScreen scene){
			this.name = name;
			this.trigger = trigger;
			this.scene = scene;
		}
		
		@Override
		void runCommand() {
					/*
		for (String[] trigger : triggers)
			scene.runTrigger(trigger);*/
			trigger.execute();
		}
	}
}

