package com.oxca2.cyoat;

import java.util.Observable;
import java.util.Observer;


public class LineTriggerObserver implements Observer {
	final SceneScreen scene;
	String[][] triggers;
	
	public LineTriggerObserver(SceneScreen scene, String[][] triggers) {
		this.scene = scene;
		this.triggers = triggers;
		
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		for (int i = 0; i < triggers.length; i++){
			if (scene.animator.l == Integer.parseInt(triggers[i][0])){
				scene.runTrigger(triggers[i]);
			}
		}
	}

}
