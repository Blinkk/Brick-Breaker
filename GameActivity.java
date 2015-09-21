package edu.westwood.pongproject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;

public class GameActivity extends Activity 
{
    /** Called when the activity is first created. **/
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        //Set content view to an instance of Pong class
        //with this activity as the context parameter
        setContentView(new Pong(this));			

    }//end of main method

    @Override
	protected void onDestroy() 
    {
		//Log.d(TAG, "Destroying...");
		super.onDestroy();
	}

	@Override
	protected void onStop() 
	{
		//Log.d(TAG, "Stopping...");
		super.onStop();
	}
}