package edu.westwood.pongproject;

import android.graphics.Canvas;

public class GameThread extends Thread 
{
   static final long FPS = 30;
   private boolean running = false;
   private Pong view;				//This is the pong object
   									//that controls everything 
   									//in this thread
   public GameThread(Pong view) 
   {
	   this.view = view;
   }
 
   public void setRunning(boolean run) 
   {
	   running = run;
   }
 
   @Override
   public void run() 
   {
	   long ticksPS = 1000 / FPS;
	   long startTime;
	   long sleepTime;
	   while (running) 
	   {
		   Canvas c = null;
           startTime = System.currentTimeMillis();
           try 
           {
        	   c = view.getHolder().lockCanvas();
               synchronized (view.getHolder()) 
               {
            	   view.onDraw(c);
               }
           } 
           finally 
           {
        	   if (c != null) 
        	   {
        		   view.getHolder().unlockCanvasAndPost(c);
        	   }
           }
               
           sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
           
           try 
           {
        	   if (sleepTime > 0)
        		   sleep(sleepTime);
        	   else
        		   sleep(10);
           } 
           catch (Exception e) {}
	   }
   }
}