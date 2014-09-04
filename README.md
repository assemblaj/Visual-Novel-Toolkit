CYOA-Toolkit
============
Library for making Choose Your Own Adventure Book style games in libgdx. Unfinished. Purpose of
this is for me to learn libgdx and work on a large project. 

Takes triggers written in a jSon file and scripts out SceneSreens based on that, 
i.e.

TimeBasedTriggers:
[1, "addBackground", "2 bg1 background1.jpg"]
- 1 is the amount of seconds into the scene when this will be performed. 

- "addBackground" is the trigger, this specific trigger adds a background to the layer specified
  and sets it to be drawn to the screen

- The last string in the array is the arguments for the trigger, in this case:
   - 2 is the layer
   - bg 1 is the id 
   - Everything else is data needed for the specific trigger, in this case the name of
     the picture to be drawn. 

Assets are stored in Maps, so they can be easily accessed via ids and removed/disposed.
Assets to be drawn must specify a layer. Layers are used to easily group together 
elements based on how far back, or close, they should be drawn on the screen. This 
allows for easy positining and handling (adding/ removal, changing, etc) of overlapping
elements. Drawable elements are looped through each element of the draw() method of 
the SceneScreen class based on earliest layer to latest. 

Other Features:
Menu Class (clickable menus, add menu items, specific Menu Item rendering ) 
Fade out/ in transition
Animated text (*typewriter effect*) 

Features to come
-----------------------
Menu based decisions (along with Map to store choices made by user)
Class for Custom Triggers (note to me, just search both the custom class and the regular
  class (re: reflection)) 
Class for triggers to be performed on completion of another trigger 
An example game
Maybe a javadoc 


