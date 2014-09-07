package com.oxca2.cyoat;

import java.util.Observable;
import java.util.Observer;


public class LineTriggerObserver implements Observer {
	//final SceneScreen scene;
	//String[][] triggers;
	Trigger[] triggers;
	
	public LineTriggerObserver(Trigger[] triggers) {
		//this.scene = scene;
		this.triggers = triggers;
		
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		for (int i = 0; i < triggers.length; i++){
			int l = (Integer)arg1; 
			if (l == triggers[i].line){
				triggers[i].execute();
			}
		}
	}

}
