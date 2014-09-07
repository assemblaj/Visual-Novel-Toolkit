package com.oxca2.cyoat;

import java.util.Iterator;
import java.util.Observable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

public class AnimatedText extends Observable {
	final Main game;
	BitmapFont font;
	
	String text;
	
	Array<String> textArray;
	int lines = 1; // total lines
	int l; // line counter;
	final static int MAX_LINES = 4;
	final static float DEFAULT_SPEED = .04f;
	final int LINE_LENGTH = 600;
	int c;
	String preString = ""; // I'M GOING TO MAKE YOU A STRING BUILDER
	StringBuilder previous;
	int pages;
	String current;
	

	int lineLength, maxPageLines;
	float speed;
	float speedLimiter = 0;
	
	int dx, dy;
	
	public AnimatedText(
		Main game, String text, String font,
		int dx, int dy, 
		int lineLength, int maxLines, float speed)
	{
		this.game = game;
		this.text = text;
		this.dx = dx;
		this.dy = dy;
		this.lineLength = lineLength;
		this.maxPageLines = maxLines;
		this.speed = speed;
		
		this.font = game.fonts.get(font);
		
		
		textArray = new Array<String>();
		lines = wrap(text, textArray);
		previous = new StringBuilder();
		
		System.out.println("lines: " + lines);

		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean keyDown(int keycode){
				if (keycode == Keys.SPACE){
					processIntput();		
					setChanged();
					notifyObservers();
					return true;
				} else 
					return false;
						
			}
			
			@Override
			public boolean touchDown(
				int screenX, int screenY, 
				int pointer, int button)
			{	
				if (button == Buttons.LEFT) {		
					processIntput();
					setChanged();
					notifyObservers();
					return true;
				}else 
				   return false;
			}
			
		});
	}
	
	// cant i just use some checks for that
	// if time sense last loop < something, don't draw 	
	public void draw(SpriteBatch batch){
		Long before = TimeUtils.nanoTime();

		current = textArray.get(l);
		
		speedLimiter +=  Gdx.graphics.getDeltaTime();
		
		/*
		font.drawMultiLine(
			batch, 
			preString + current.substring(0, c), 
			dx, dy);		*/
		
		font.drawMultiLine(
				batch, 
				previous.toString() + current.substring(0, c), 
				dx, dy);		
		// If it's time to draw the next character
		// and wer're not on the last character of the line
		if (speedLimiter >= speed && c  < current.length() ){	
			c++;
			speedLimiter = 0;
			if (c > current.length()){
				System.out.println("HELOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
			}
		} 	
		// If the next character is the last character
		// of the current line, and the next line
		// isn't last line of the set.
		else if ((c + 1)  >= current.length() && (l + 1) < lines){		
			// If the next line isn't the last on the page
			// Go to the next line and restart the character counter.
			// Add the current line to the preString.
			if (!((l+1) % maxPageLines == 0)) {	
				l++;	
				c = 0;
				if (current.equals("\n")) 
					// Don't add newline behind a newline
					//preString += current;
					previous.append(current);
				else {
					//preString += current + "\n";
					previous.append(current);
					previous.append("\n");
				}
			}
		} 
		// If we're on the last line of the last page. 
		else if ((c + 1)  >= current.length() && (l + 1) > lines){
			// do nothing. 
		}	
		
		/*
		System.out.println("c: " + c);
		System.out.println("current.length(): " + current.length());
		System.out.println("current: " + current);
		*/
		setChanged();
		notifyObservers(l);
	}
	
	//Wraps the text so that it fits a certain
	// line width and then ends then returns the amount 
	// of lines.
	public int wrap(String str, Array<String> linesArray){
		StringBuilder line = new StringBuilder();
		String[] words = str.split(" ");
		int lines = 0;
		int total = words.length;
		// lines +=  " " + words[i];
				
		for(int i = 0; i < total; i++) {
			String test = line.toString(); // String for testing bounds
			if(words[i].equals("\n")){
				if (!test.equals("")) {
					linesArray.add(test);
					lines++;
				}
				
				linesArray.add("\n");
				line.replace(0, line.length(), "");
				lines++;
			}else if (font.getBounds(test + words[i] + " ").width <= LINE_LENGTH) {
				if (i == (total - 1)){
					line.append(words[i]);					
					linesArray.add(line.toString());
					lines++;
				}else 
					line.append(words[i] + " ");
			}else if (font.getBounds(test + words[i] + " ").width > LINE_LENGTH){			
				linesArray.add(test);
				line.replace(0, line.length(), words[i] + " ");
				lines++;
				
				// if wer're on the last word
				// therefore the loop would stop on the next 
				// test before it gets its own line.
				if(i == words.length - 1){
					linesArray.add(line.toString());
					lines++;
				}
			}
		}
		
		//System.out.println(linesArray);
		return lines;
	}	
	
	public void processIntput() {
		// If we're on the last line of any given page 
		// but the last page. 
		if (((l+1) % maxPageLines) == 0 && (l+1) < lines) {
			// Wait till the user presses space or clicks to move on 
			// to the next box. 
			//preString = "";
			previous.replace(0, previous.length(), "");
			l++;
			c = 0;
		}
		// If we're on the last page.
		// (the difference between the total lines and the 
		// line counter is so small that it's less than the
		// maximum lines per page) 
		
		// if we're on the last page but haven't hit the
		// last character yet. 
		else if ((lines - l <= maxPageLines)){			
			String loopLine;
			
			// Add all of the remaining lines from
			// the last page up to the very last,
			// to the prestring.
			for (int i = l; i < lines-1; i++){
				loopLine = textArray.get(i);
				if(loopLine.equals("\n")) 
					//preString += loopLine;
					previous.append(loopLine);
				else { 
					//preString += loopLine + "\n";
					previous.append(loopLine);
					previous.append("\n");
				}	
			}	
			
			// Add the last line to the preString,
			// set the line counter to the last line and the
			// current character to the last character. 
			current = textArray.get(lines-1);
			l= lines-1;
			c = current.length()-1;
		} else {
			int remainingLines;
			
			// If we're on the first page, 
			// the remaining lines are the maximum lines per page.		
			if (l < maxPageLines){
				remainingLines = maxPageLines;
			} else {
				remainingLines = (int)Math.ceil((double)(l+1)/maxPageLines) * maxPageLines;
			}
			
			// Add the remaining lines on the page to 
			// the prestring, up until the last line. 
			String loopLine;	
			for (int i = l; i < remainingLines-1; i++){
				loopLine = textArray.get(i);
				if(loopLine.equals("\n")){ 
					//preString += loopLine
					previous.append(loopLine);
				}
				else { 
					//preString += loopLine + "\n";
					previous.append(loopLine);
					previous.append("\n");
				}
			}	
			
		
			current = textArray.get(remainingLines-1);
			l = remainingLines - 1;
			c = current.length()-1;	
		}		
	}
	
	public boolean finished() {
		return l==lines-1 && c  == current.length()-1;
	}
	
	public static String join(String[] r, String d) {
		if (r.length == 0) return "";
		StringBuilder sb = new StringBuilder();
		int i;
		for(i = 0;i<r.length-1;i++)
			sb.append(r[i]+d);
		return sb.toString() + r[i];
	}	

}
