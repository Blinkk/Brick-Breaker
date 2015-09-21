/************************************************
 *		Programmer: Austin Ivicevic				*
 *		Brick Breaker Game for SGPG250			*
 ************************************************/

/*Note: This project was designed for an old version of android with a very small amount of pixels. Many position values
have been hard-coded in and will not port correctly to all devices. This is currently being worked on. Thank you for your 
understanding.*/


package edu.westwood.pongproject;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class Pong extends SurfaceView 
{
	//All global class attributes
	private SurfaceHolder holder;			// SurfaceHolder for all assets
	private GameThread gameLoopThread;		// Game thread for main activity
	private int lives = 3;					// Set player lives to 3
	private String strLives =
			Integer.toString(lives);		// String to hold lives and draw to screen
	private int x = 0; 						// X coordinate of your finger 
	private int y = 0;						// Y coordinate of your finger
	private int pongX;						// X coordinate of ball (set in setImages() method)
	private int pongY;						// Y coordinate of ball (set in setImages() method)
	private Rect pRect;						// Rectangle to hold ball
	private int paddleX;					// X coordinate of paddle (set in setImages() method)
	private int paddleY;					// Y coordinate of paddle (set in setImages() method)
	private int w = 0;						// Width of screen
	private int h = 0;						// Height of screen
	private int xSpeed = 6;					// Speed of horizontal movements of ball
	private int ySpeed = 6;					// Speed of vertical movements of ball
	private boolean pongUp = false;			// Check if ball going up
	private boolean pongDown = false;		// Check if ball going down
	private boolean pongRight = false;		// Check if ball going right
	private boolean pongLeft = false;		// Check if ball going left
 	private boolean goRight = false;		// Check if paddle going right (see onTouchEvent())
 	private boolean goLeft = false;			// Check if paddle going left (see onTouchEvent())
 	Bitmap pong;							// Ball image
 	Bitmap paddle;							// Paddle image
 	Bitmap brick;							// Brick image
 	Paint paint;							// Paint [brush] 
 	ArrayList<Brick> brickListOne = 
 			new ArrayList<Brick>();			// List of bricks to hold row 1
 	ArrayList<Brick> brickListTwo = 
 			new ArrayList<Brick>();			// List of bricks to hold row 2
 	ArrayList<Brick> brickListThree = 
 			new ArrayList<Brick>();			// List of bricks to hold row 3
       
 	public Pong(Context context) 
 	{
 		super(context);
	   	paint = new Paint();
	   	DisplayMetrics dm = getResources().getDisplayMetrics();		
	   	h = dm.heightPixels;				// Set screen height
	   	w = dm.widthPixels;					// Set screen width
	   	setImages(); 
	   	InitializeBricks();
	   	
	   	gameLoopThread = new GameThread(this);
	   	holder = getHolder();
	   	holder.addCallback(new SurfaceHolder.Callback() 
	   	{
		   	@Override
		   	public void surfaceDestroyed(SurfaceHolder holder) 
		   	{
		   		boolean retry = true;
		   		gameLoopThread.setRunning(false);
		   		while (retry) 
		   		{
		   			try 
		   			{
		   				gameLoopThread.join();
		   				retry = false;
		   			} 
		   			catch (InterruptedException e) {}
		   		}
		   	}
 
		   	@Override
		   	public void surfaceCreated(SurfaceHolder holder) 
		   	{
			   	gameLoopThread.setRunning(true);
			   	gameLoopThread.start();
		   	}
 
		   	@Override
		   	public void surfaceChanged(SurfaceHolder holder, int format,
		   			int width, int height)
		   	{
		   	}
	   	});     
 	}
 	
    // This is the magic method. It is called 
 	// every time the images are re-drawn on screen
 	@Override
 	protected void onDraw(Canvas canvas) 
 	{			
 		paint.setAntiAlias(false);
 		canvas.drawColor(Color.BLACK);
 		canvas.drawBitmap(pong, pongX, pongY, null);
 		canvas.drawBitmap(paddle, paddleX, paddleY, null);
 		
 		// Draw each brick that is
 		// visible to the screen
 		for (int i = 0; i < 3; i++)
 		{
 			switch(i)
 			{
 			case 1:
 				for (Brick b1 : brickListOne)
 					b1.Draw(canvas);
 			case 2:
 				for (Brick b2 : brickListTwo)
 					b2.Draw(canvas);
 			case 3:
 				for (Brick b3 : brickListThree)
 					b3.Draw(canvas);
 			}
 		}

 		// Each time onDraw() is called, check whether or not the
 		// onTouchEvent() returned a value for goLeft or goRight,
 		// if so move paddle, if not do nothing and continue
 		if (goLeft && paddleX > 0)
 			paddleX -= 8;
 		if (goRight && paddleX < 
 				w - paddle.getWidth())	// 'w' is width of screen in pixels
 			paddleX += 8;
 		checkPaddleCollision();			// Check paddle collision
 		updateBall();					// Move ball
 		checkBrickCollision();			// Check collision for bricks
 	
 		// Set up paint (declared in constructor)
 		// and draw text to screen using the paint
 		paint.setColor(Color.WHITE);
	   	paint.setTextSize(20); 
	   	canvas.drawText("EXIT" , w - 100, h - 100, paint); 
	   	canvas.drawText("Restart" , 50, h - 100, paint);
	   	canvas.drawText("Lives: " + strLives, w - 265, h -100, paint);
 	}
       
 	public void setImages()
 	{ 
 		pong = BitmapFactory.decodeResource(getContext().getResources(), 
 				R.drawable.pong);
 		paddle = BitmapFactory.decodeResource(getContext().getResources(), 
 				R.drawable.paddle);
 		brick = BitmapFactory.decodeResource(getContext().getResources(),
 				R.drawable.brick);
 		
 		// Figure out how to change pos for different phones
 		// Set ball position immediately after creating
 		pongX = w / 2;
 		pongY = (h / 2) - 200;
 		
 		//Pong rect
 		pRect = new Rect(pongX, pongY, pongX + pong.getWidth(),
 				pongY + pong.getHeight());
 		
 		// Figure out how to change pos for different phones
 		// Set paddle position immediately after creating
 		paddleX = ((w / 2) - (paddle.getWidth() / 2));
	   	paddleY = h - 150;
 	}
 	
 	// Initialize bricks on screen and populate lists
 	public void InitializeBricks()
 	{
 		int tempPosX = 15;
 		int tempPosY = 0;
 		
 		// Row 1
 		for (int i = 0; i < 6; ++i)
 		{
 			// Pass position, dimensions and image to each new brick
 			brickListOne.add(new Brick(tempPosX, tempPosY, brick.getWidth(), 
 					brick.getHeight(), brick));
 			// Increment tempPos
 			tempPosX += brick.getWidth();
 		}
 		
 		// Reset/Increment positions
 		tempPosX = 15;
 		tempPosY += brick.getHeight();
 		
 		// Row 2
 		for (int i = 0; i < 6; ++i)
 		{
 			// Pass position, dimensions and image to each new brick
 			brickListTwo.add(new Brick(tempPosX, tempPosY, brick.getWidth(), 
 					brick.getHeight(), brick));
 			// Increment tempPos
 			tempPosX += brick.getWidth();
 		}
 		
 		// Reset/Increment positions
 	 	tempPosX = 15;
 	 	tempPosY += brick.getHeight();
 		
 		// Row 3
 		for (int i = 0; i < 6; ++i)
 		{
 			// Pass position, dimensions and image to each new brick
 			brickListThree.add(new Brick(tempPosX, tempPosY, brick.getWidth(), 
 					brick.getHeight(), brick));
 			// Increment tempPos
 			tempPosX += brick.getWidth();
 		}
 	}

 	// Called every time onDraw() is called in thread loop
 	// to move ball according to speedX and speedY
 	public void updateBall()
 	{
 		pongX += xSpeed;
 		pongY += ySpeed;
 		if (pongX > w-20)
 			xSpeed = -6;
 		if (pongX < 0)
 			xSpeed = 6;
 		if (pongY > h - 40) 
 		{
 			lives--;
 			if (lives > 0)
 				Reset(lives);
 			else doLose();
 		}
 		if (pongY < 0)
		   	ySpeed = 6;

 		// Reset directional flags for ball
 		if (ySpeed > 0)
 		{
 			pongDown = true;
 			pongUp = false;
 		}
 		else
 		{
 			pongUp = true;
 			pongDown = false;
 		}
 		
 		if (xSpeed > 0)
 		{
 			pongRight = true;
 			pongLeft = false;
 		}
 		else
 		{
 			pongLeft = true;
 			pongRight = false;
 		}   
 	}

 	// Check collision with paddle
 	public void checkPaddleCollision()
 	{
 		// Y-axis has a 20 pixel slack
 		if (pongX + pong.getWidth() > paddleX 
 				&& pongX < paddleX + paddle.getWidth() 
 				&& pongY + pong.getHeight() >= paddleY 
 				&& pongY + pong.getHeight() - 10 < paddleY + 10)
 		{
 			// If collision, reverse ySpeed
 			ySpeed = -6;
 			
 			// If ball moving right and center hits
 			// very left half of paddle, reverse xSpeed
 			if (pongX + pong.getWidth() / 2 < 
 					paddleX + paddle.getWidth() / 2 - 1
 					&& pongRight)
 				xSpeed *= -1;
 			
 			// If ball moving right and center hits 
 			// very right half paddle, reverse xSpeed
 			else if (pongX + pong.getWidth() / 2 > 
 					paddleX + paddle.getWidth() / 2 + 1
 					&& pongLeft)
 				xSpeed *= -1;
 		}
 	}
 	
 	public void checkBrickCollision()
 	{
 		// Row 1
 		// Only checks this collision if the first two rows are missing
 		// elements (meaning the player has hit those elements)
 		// and if this list is not empty
 		if (brickListThree.size() < 6 
 				&& brickListTwo.size() < 6)
 		{
 			if (!brickListOne.isEmpty())
 			{
 				for (Brick b1 : brickListOne)
 				{
 					if (b1.CheckYCollision(pongX, pongY, pong, pongUp, pongDown))
 					{
 						brickListOne.remove(b1);
 						ySpeed *= -1;
 						break;
 					}
 				
 					if (b1.CheckXCollision(pongX, pongY, pong, pongRight, pongLeft))
 					{
 						brickListOne.remove(b1);
 						xSpeed *= -1;
 						break;
 					}
 				}
 			}
 		}
 		
 		// Row 2
 		// Only checks this collision if elements have been 
 		// hit in row 3 and list is not empty
 		if (brickListThree.size() < 6)
 		{
 			if (!brickListTwo.isEmpty())
 			{
 				for (Brick b2 : brickListTwo)
 				{
 					if (b2.CheckYCollision(pongX, pongY, pong, pongUp, pongDown))
 					{
 						brickListTwo.remove(b2);
 						ySpeed *= -1;
 						break;
 					}
 			
 					if (b2.CheckXCollision(pongX, pongY, pong, pongRight, pongLeft))
 					{
 						brickListTwo.remove(b2);
 						xSpeed *= -1;
 						break;
 					}
 				}
 			}
 		}
 		
 		// Row 3
 		// This collision is always checked as
 		// as long as list is not empty
 		for (Brick b3 : brickListThree)
 		{
 			if (!brickListThree.isEmpty())
 			{
 				if (b3.CheckYCollision(pongX, pongY, pong, pongUp, pongDown))
 				{
 					brickListThree.remove(b3);
 					ySpeed *= -1;
 					break;
 				}
 			
 				if (b3.CheckXCollision(pongX, pongY, pong, pongRight, pongLeft))
 				{
 					brickListThree.remove(b3);
 					xSpeed *= -1;
 					break;
 				}
 			}
 		}
 	}
      
 	@Override
 	public boolean onTouchEvent(MotionEvent me)
 	{
 		int action = me.getAction();
 		x = (int) me.getX();
 		y = (int) me.getY();
	        
 		if (action == MotionEvent.ACTION_DOWN) 
 		{
 			if(x < getWidth() / 2)
 				goLeft = true;
 			if(x > getWidth() / 2)
 				goRight = true;
	        
 			// paddleX=x;
	        	       
 			// This will not work on other phones with hard code
 			if(x > w-100 && x < w && y > h-130 && y < h)
 				doLose(); //exit
	        	

 			if(x > 0 && x < 120 && y > h-130 && y < h)
 			{
 				goLeft = false;
 				goRight = false;
 				Restart();
 			}
	   	}

	   	if (action == MotionEvent.ACTION_MOVE) 
	   	{
		   	// eventually use this to move paddle by dragging
	   	}

	   	if (action == MotionEvent.ACTION_UP) 
	   	{
		   	// do something
		   	goLeft = false;
		   	goRight = false;
	   	}
	        
	   	return true;
 	}
 	
 	public void Restart()
 	{
 		// Stop the ball from moving
 		xSpeed = 0;
 		ySpeed = 0;
 		
 		// Sleep for 2 seconds
 		try 
 		{ 
 			Thread.sleep(2000);
 		}
 		catch (InterruptedException ex)
 		{
 			Thread.currentThread().interrupt();
 		}
 		
 		lives = 3;
 		Reset(lives);
 	}
 	
 	public void Reset(int lives)
 	{
 		// Update Lives string
		strLives = Integer.toString(lives);
		
 		// Reset ball position
 		pongX = w / 2;
 		pongY = (h / 2) - 200;
 		
 		// Reset ball direction (velocity)
 		xSpeed = 6;
 		ySpeed = 6;
 		
 		// Pause program momentarily
 		try 
 		{
 		    Thread.sleep(1000);
 		} 
 		catch(InterruptedException ex) 
 		{
 		    Thread.currentThread().interrupt();
 		}
 	}
	   
 	public void doLose() 
 	{
 		((GameActivity) getContext()).finish();          
 	}  
}