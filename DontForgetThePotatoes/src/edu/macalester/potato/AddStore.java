package edu.macalester.potato;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * AddStore class
 * 
 * Allows User to add a new store to the store database and delete stores from the database.
 * 
 * @author Potato Team
 */
public class AddStore extends Activity{
	EditText name = null;
	EditText address=null;
	StoreHelper helper =null;
	Cursor model = null;
	String storeID = null;
	AlertDialog.Builder builder = null;
	AlertDialog.Builder builder2 = null;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addstore);
        
        Button save = (Button)findViewById(R.id.save);
        save.setOnClickListener(onSave);
        
        Button delete = (Button)findViewById(R.id.delete);
        delete.setOnClickListener(onDelete);
        
        Button cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(onCancel);
        
        helper = new StoreHelper(this);//opens access to database
        name = (EditText)findViewById(R.id.name);
        address=(EditText)findViewById(R.id.address);
        
        builder = new AlertDialog.Builder(this);
        builder2 = new AlertDialog.Builder(this);
        
    }
    
    /*
     * (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    public void onDestroy(){
    	super.onDestroy();
    	helper.close();
    }
    
    /*
     * Method for what happens when the "Save" button is clicked.  Inserts store information into the database.
     */
    View.OnClickListener onSave=new View.OnClickListener() {
		
		public void onClick(View v) {

			helper.insert(name.getText().toString(), 
						address.getText().toString());
			setResult(RESULT_OK);
			
			finish();
		}
	};
	
	/*
	 * Method for what happens when the "Cancel button is clicked.  Simply ends activity.
	 */
	View.OnClickListener onCancel=new View.OnClickListener() {
			
			public void onClick(View v) {
				
				finish();
			}
		};
	
	/*
	 * Method for what happens when "Delete Store" button is pressed.  Brings up a list of stores
	 * and prompts user whether they want to delete them.
	 */
	View.OnClickListener onDelete=new View.OnClickListener() {
		
		public void onClick(View v) {
			model = helper.getAll();
			
			/*Sets up a dialog that shows current stores. */
			builder.setInverseBackgroundForced(true);
			builder.setCursor(model, new DialogInterface.OnClickListener(){
        	public void onClick(DialogInterface dialog, final int which){
        			builder2.setMessage("Do you wish to delete this store?");
        			//Sets up a way to delete stores from the database
        			builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
        	        	public void onClick(DialogInterface dialog2, int which2){
        	        		model.moveToPosition(which);
        	        		String itemId = model.getString(0);
        	        		helper.delete(itemId);
        	        		
        	        		dialog2.dismiss();
        	        	}
        	        });
        			builder2.setNegativeButton("No", new DialogInterface.OnClickListener(){
        	public void onClick(DialogInterface dialog, int which){
        		dialog.dismiss();
        	}
        });
        			AlertDialog delete = builder2.create();
        			delete.show();
        	}
        }, model.getColumnName(1));
			
			builder.setNeutralButton("Okay", new DialogInterface.OnClickListener(){
        	public void onClick(DialogInterface dialog, int which){
        		dialog.dismiss();
        	}
        });
			builder.setTitle("Saved Stores");
			
			AlertDialog show = builder.create();
			show.show();
		}
	};

}
