package com.oxca2.cyoat;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Menu extends InputAdapter {
	final Main game;
	Array<MenuItem> menuItems;
	int menuX, menuY;
	int space;
	int itemHeight, itemWidth;
	int paddingV, paddingH;
	float offset;
	Vector3 mousePos;
	Rectangle menuBounds;
	
	// int has a default value of 0, which interferes with the touchDown() method
	int hoveredItem = -1; 
	
	MenuItemRenderer renderer;
	
	public Menu(Main main, int space, 
			int menuX, int menuY,
			int itemHeight, int itemWidth,
			int paddingV, int paddingH,
			float offset){
		this.game = main;
		this.space = space;
		
		this.menuX = menuX;
		this.menuY = menuY;
		
		this.itemHeight = itemHeight;
		this.itemWidth = itemWidth;
		
		this.paddingV = paddingV;
		this.paddingH = paddingH;
		
		this.offset = offset;
		
		
		menuItems = new Array<MenuItem>();
		mousePos = new Vector3();
		menuBounds = new Rectangle();
		menuBounds.x = menuX;
		menuBounds.y = menuY += offset + space;
		menuBounds.width = itemWidth;
		menuBounds.height = itemHeight;

		System.out.println("menuBoudsn.height: " + menuBounds.height);
		
		// Setting the default renderer. 
		renderer = new MenuItemRenderer() {
			public void drawHighlighted(Menu menu, MenuItem item, SpriteBatch batch){
				game.fonts.get("vs_f5").setColor(Color.RED);
				game.fonts.get("vs_f5").draw(batch, item.getName(), 
						item.bounds.x, item.bounds.y + menu.offset);						
			}
			
			public void drawDefault(Menu menu, MenuItem item, SpriteBatch batch){
				game.fonts.get("vs_f5").setColor(Color.WHITE);;
				game.fonts.get("vs_f5").draw(batch, item.getName(), 
						item.bounds.x, item.bounds.y + menu.offset);							
			}
		};
	}
	
	
	public void draw(SpriteBatch batch) {
		int counter = 0;
		MenuItem currentItem;
		Iterator<MenuItem> items = menuItems.iterator();
		
		while (items.hasNext()){
			currentItem = items.next();
			mousePos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			game.camera.unproject(mousePos);		

			if (currentItem.getBounds().contains(mousePos.x, mousePos.y)){		
				hoveredItem = counter;
				renderer.drawHighlighted(this, currentItem, batch);
			} else {
				renderer.drawDefault(this, currentItem, batch);
			}
			counter++;
			
			if (!menuBounds.contains(mousePos.x, mousePos.y)){
				hoveredItem = -1;
			}

		}
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {			
		Iterator<MenuItem> items = menuItems.iterator();
		MenuItem currentItem;
		
		int counter = 0;
		
		System.out.println("hoveredItem: " + hoveredItem);
		while(items.hasNext()){
			currentItem = items.next();
			if (counter == hoveredItem){
				System.out.println("COUNTER IN touchDown: " + counter);
				currentItem.runCommand();
			}	
			counter++;
		}
		return false;
	}
	
	public void add(MenuItem newItem) {
		menuItems.add(newItem);
	}
	
	public void setRenderer(MenuItemRenderer renderer){
		this.renderer = renderer;
	}
	
	/* Calculate the drawing position of the last menu item
	 * the decrement downward to the first. 
	 * Done because openGL  has Y axis going upward 
	 * but most menus are arranged in descending order.
	 * So this code starts from the top and goes downward for
	 * each item.
	 */		
	public void layoutMenu() {
		Iterator<MenuItem> items = menuItems.iterator();
		int lastPos =  menuY + ( menuItems.size * itemHeight) + (menuItems.size * space);
		
		while (items.hasNext()){
			layoutItem(items.next(), lastPos);
			lastPos -= space;
			lastPos -= itemHeight;
		}			
		
		// Make the height of the bounds the height of the total amount
		// of items, including spaces.
		// Make the y value of the mounds the position that the
		// last menu item was drawn at.
		menuBounds.height = ( menuItems.size * itemHeight) + (menuItems.size * space);
		/*
		menuY = lastPos;
		System.out.println("MenuY in layoutMenu: " + menuY);*/
	}
	
	public void layoutItem(MenuItem item, int pos) {
		item.getBounds().x = menuX; 
		item.getBounds().y = pos;
		item.getBounds().width = itemWidth;
		item.getBounds().height = itemHeight;
	}
	
	public abstract class MenuItem {
		String name;
		Rectangle bounds = new Rectangle();
		
		abstract void runCommand();
		
		public Rectangle getBounds(){
			return bounds;
		}
		
		public String getName() {
			return name;
		}
		
		public void setName(String name){
			this.name = name;
		}
		
		public MenuItem initialize(String name) {
			this.name = name;
			return this;
		}
	}
	
	public abstract class MenuItemRenderer {	
		abstract void drawHighlighted(Menu menu, MenuItem item, SpriteBatch batch);
		abstract void drawDefault(Menu menu, MenuItem item, SpriteBatch batch);
	}
}


