package com.oxca2.cyoat;

import com.badlogic.gdx.utils.Array;

public class SceneData {

	String[] startArray;
	
	// Background Stuff
	String bgPath;
	
	// Textbox stuff
	String tbPath;
	int tbX;
	int tbY;
	int tbW;
	int tbH;
	
	// Animated Text stuff 
	String[][] animatedText;
	String[][] animText;
	int atX;
	int atY;
	int lineLength;
	int maxPageLines;
	float speed;
	
	// Static Text stuff
	String[] staticText;
	String stFont;
	int stX;
	int stY;
	
	// Music stuff 
	String bgmPath;
	
	// Sound effect stuff 
	String sfxPath;
	
	// Trigger arrays
	String[][][] lineTriggers;
	String[][] timeTriggers;
	
	//
	int startLayers;
	
	// Menu Stuff 
	String[][] menuData;
	String[] menuItemNames;
	
	
	Array<Trigger> timeBasedTriggers;
}
