CYOA-Toolkit
============
Library for making Choose Your Own Adventure Book style games in libgdx. Unfinished. Purpose of
this is for me to learn libgdx and work on a large project. 

Originally, triggers were written in string arrays in jSon files. Since that was a bad 
idea (and became completely unmaintainable when implementing menus), I've moved over
to an object system where one writes out the properties directly  in the file. 
i.e 

		{
			"class" : "com.oxca2.cyoat.AddNewBackground",
			"layer": 0,
			"id" : "bg0",
			"bgPath" : "ai1.jpg",
			"time" : 0
		}

AddNewBackground is a Trigger that adds a background to be drawn to the screen. It creates
a DrawingCommand object, which has an execute method which draws to the screen every
frame. 

Assets are stored inside the DrawingCommand and disposed when the command is 
removed from the command array. Each DrawingCommand is added to a layer.
Layers are used to easily group together elements based on how far back, or close,
they should be drawn on the screen. This allows for easy positining and handling 
(adding/ removal, changing, etc) of overlapping elements. 

Other Features
-----------------------
- Menu Class (clickable menus, add menu items, specific Menu Item rendering ) 
- Fade out/ in transition
- Animated text (*typewriter effect*)
   - Has it's own class for performing triggers when a specific line of text
     is reach. 

Features to come
-----------------------
- Menu based decisions (along with Map to store choices made by user)
- Class for Custom Triggers 
- Class for triggers to be performed on completion of another trigger 
- An example game
- Maybe a javadoc 


