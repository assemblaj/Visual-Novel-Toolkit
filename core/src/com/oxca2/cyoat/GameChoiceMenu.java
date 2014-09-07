package com.oxca2.cyoat;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameChoiceMenu extends Menu {
	String prompt;
	String font;
	
	public GameChoiceMenu(Main main, int space, int menuX, int menuY,
			int itemHeight, int itemWidth, int paddingV, int paddingH,
			float offset, String prompt, String font) {
		super(main, space, menuX, menuY, itemHeight, itemWidth, paddingV, paddingH,
				offset);
		this.prompt = prompt;
		this.font = font;
	}
	
	public void draw(SpriteBatch batch) {
		game.fonts.get(font).setColor(Color.WHITE);
		game.fonts.get(font).drawMultiLine(batch, prompt, 
				menuItems.get(0).bounds.x, menuItems.get(0).bounds.y + this.offset*3);	
		super.draw(batch);
	}
	
	public class GameChoice extends MenuItem {
		String[][] triggers;
		SceneScreen scene;
		
		public GameChoice(String name, String[][] triggers, SceneScreen scene){
			this.name = name;
			this.triggers = triggers;
			this.scene = scene;
		}
		
		@Override
		void runCommand() {
			for (String[] trigger : triggers)
				scene.runTrigger(trigger);
		}
	}
}
