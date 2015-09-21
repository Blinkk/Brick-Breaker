package edu.westwood.pongproject;

import android.view.View.OnClickListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;

public class MainActivity extends Activity 
{
	 Button next;
				
    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       	
        //Instantiate button in code and call getButton();
	    next = (Button) findViewById(R.id.next);
        getButton();
    }
    
    public void getButton()
    {
    	 next.setOnClickListener(new View.OnClickListener() 
    	 {
             public void onClick(View view) 
             {
             	Intent myIntent = new Intent(view.getContext(), GameActivity.class);
                startActivityForResult(myIntent, 0);
             }
         });	         	
    }
}
