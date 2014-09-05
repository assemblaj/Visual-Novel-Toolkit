package com.oxca2.cyoat;

import com.badlogic.gdx.graphics.Color;

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
	
	public void draw() {
		game.fonts.get(font).setColor(Color.WHITE);
		game.fonts.get(font).drawMultiLine(game.batch, prompt, 
				menuItems.get(0).bounds.x, menuItems.get(0).bounds.y + this.offset*3);	
		super.draw();
	}
}
