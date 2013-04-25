package edu.macalester.potato;







import android.app.Activity;
/**
 * 
 * @author Awankel
 * This is the main menu of the app.  From here the user is given options to read instructions, add items, view items, and enter shopping mode.
 * this mode allows users to check off items from the store they are currently in.  There is also the gps button, which when enabled will allow the user
 * to access certain gps only functions.
 *
 */

import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.widget.ToggleButton;
/**
 * 
 * @author Awankel
 *
 */
public class DontForgetThePotatoes extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        View aboutButton = findViewById(R.id.instructions);
        aboutButton.setOnClickListener(this);
        View enterButton = findViewById(R.id.enter);
        enterButton.setOnClickListener(this);
        View listButton = findViewById(R.id.list);
        listButton.setOnClickListener(this);
        View addButton = findViewById(R.id.add);
        addButton.setOnClickListener(this);
        /**
         * these create the clickable buttons on the main screen.
         */
        
    
       
    
        	/**
        	 * Creates the toggle button for the gps, on click the text "GPS enabled" will be displayed.  Displays the text "GPS Disabled when toggled off.
        	 */
          
        
    }
    

	public void onClick(View v) {
		switch (v.getId()) {
	      case R.id.instructions:
	         Intent i = new Intent(this, Instructions.class);
	         startActivity(i);
	         break;
	      case R.id.list:
		         Intent j = new Intent(this, SearchList.class);
		         startActivity(j);
		         break;
	      case R.id.enter:
		         Intent k = new Intent(this, EnterStore.class);
		         startActivity(k);
		         break;
	      case R.id.add:
		         Intent l = new Intent(this, AddItem.class);
		         startActivity(l);
		         break;
		
		}
	}
	@Override
	   public boolean onCreateOptionsMenu(Menu menu) {
	      super.onCreateOptionsMenu(menu);
	      MenuInflater inflater = getMenuInflater();
	      inflater.inflate(R.menu.menu, menu);
	      return true;
	      /**
	       * Creates the option menu, allowing access to the settings and about screens.
	       */
	   }
	

@Override
public boolean onOptionsItemSelected(MenuItem item) {
   switch (item.getItemId()) {
   case R.id.settings:
      startActivity(new Intent(this, Prefs.class));
      
      return true;
      /**
       * Settings button in the menu
       */
   case R.id.aboutus:
	      startActivity(new Intent(this, About.class));
	      return true;
	      /**
	       * About button in the menu.
	       */
   }
   return false;
}
}
