package edu.macalester.potato;
import android.app.Activity; 
import android.os.Bundle; 
/**
 * 
 * @author Awankel
 * Opens the instuctions xml file when the Instructions icon is pressed from the main menu.
 *
 */

public class Instructions extends Activity { 
@Override 
	protected void onCreate(Bundle savedInstanceState) { 
	super.onCreate(savedInstanceState); 
	/**
	 * Pulls up the instructions xml file
	 */
	setContentView(R.layout.instructions); 
	} 
} 