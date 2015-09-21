package edu.westwood.pongproject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class Brick
{
	public int brickX;				// X position of brick
	public int brickY;				// Y position of brick
	private int brickW;				// Width of brick
	private int brickH;				// Height of brick
	private boolean isVisible;		// Visibility flag
	private Rect bRect;				// Rectangle for each brick
	Bitmap brick;					// Image for brick
	
	public Brick(int posX, int posY, int width, int height, Bitmap brickImg)
	{
		// Initialize variables with passed
		// position, dimension and image
		brickX = posX;
		brickY = posY;
		brickW = width;
		brickH = height;
		brick = brickImg;
		
		//Create new rect for each brick
		final Rect bRect = new Rect(brickX, brickY, brickX + brickW,
				brickY + brickH);
		
		isVisible = true;			// Bricks are automatically set to								
	}								// be visible when they are created
	
	public void Draw(Canvas c)
	{
		if(isVisible)				// If visible, draw brick
			c.drawBitmap(brick, brickX, brickY, null);
	}
	
	public boolean CheckXCollision(int ballX, int ballY, Bitmap ballImg,
			boolean ballRight, boolean ballLeft)
	{			
		// Note: The +-2 is only relevant for Android 2.2 and will
		// not work correctly on devices with a higher pixel count
		
		// If ball hits the left side of a brick
		if(ballY + ballImg.getHeight() >= brickY 
				&& ballY <= brickY + brickH 
				&& ballX + ballImg.getWidth() >= brickX - 2 
				&& ballX + ballImg.getWidth() <= brickX + 2
				&& ballRight)
		{
			this.isVisible = false;
			return true;
		}
		
		// If ball hits the right side of a brick
		else if(ballY + ballImg.getHeight() >= brickY 
				&& ballY <= brickY + brickH 
				&& ballX <= (brickX + brickW) + 2 
				&& ballX >= brickX + brickW - 2
				&& ballLeft)
		{
			this.isVisible = false;	
			return true;
		}
		
		return false;				
	}
	
	public boolean CheckYCollision(int ballX, int ballY, Bitmap ballImg,
			boolean ballUp, boolean ballDown)
	{	
		// Note: The +-4 is only relevant for Android 2.2 and will
		// not work correctly on devices with a higher pixel count
		
		// If ball hits the top of a brick
		if (ballX + ballImg.getWidth() >= brickX 
				&& ballX <= brickX + brickW
				&& ballY + ballImg.getHeight() >= brickY - 4 
				&& ballY + ballImg.getHeight() <= brickY + 4
				&& ballDown)
		{
			this.isVisible = false;
			return true;
		}
					
		// If ball hits the bottom of a brick
		else if (ballX + ballImg.getWidth() >= brickX 
				&& ballX <= brickX + brickW
				&& ballY <= (brickY + brickH) + 4
				&& ballY >= brickY + brickH - 4
				&& ballUp)
		{
			this.isVisible = false;
			return true;
		}
		
		return false;
	}
}
