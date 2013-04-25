package edu.macalester.potato;

import java.util.ArrayList;
import java.util.List;

import edu.macalester.potato.SearchList.ItemHolder;

import android.app.Activity; 
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle; 
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * AddItem
 * 
 * This class allows the user to add items to the item database.  Item name, quantity, and store location
 * can be entered and are saved to the database.
 * 
 * @author Potato Team
 *
 */
public class AddItem extends Activity{
	Cursor model = null;
	EditText name = null;
	EditText quantity=null;
	String store =null;
	Spinner spinner=null;
	ItemHelper helper = null;
	StoreHelper helper2 = null;
	String itemID = null;
	ArrayList<String> stores = new ArrayList<String>();
	AlertDialog.Builder builder = null;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        
        helper = new ItemHelper(this);//opens access to database
        helper2 = new StoreHelper(this);
        name = (EditText)findViewById(R.id.name);
        quantity=(EditText)findViewById(R.id.quantity);
        
        //Retrieve stores from database and populate an arraylist.
        stores.add("NONE");
        model = helper2.getAll();
        startManagingCursor(model);
        model.moveToFirst();
        while(!model.isAfterLast()){
        	stores.add(model.getString(1));
        	model.moveToNext();
        }
        
        //Set up spinner menu for store selection.
        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
        
        //Set up buttons and onClickListeners.
        Button save = (Button)findViewById(R.id.save);
        Button newStore = (Button)findViewById(R.id.newStore);
        Button checkOffItem = (Button)findViewById(R.id.cancel);
        
        save.setOnClickListener(onSave);
        newStore.setOnClickListener(onNewStore);
        checkOffItem.setOnClickListener(onCancel);
        
        //Make alert dialog for when user tries to save empty item
        builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Please enter both a name and quantity.");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener(){
        	public void onClick(DialogInterface dialog, int which){
        		dialog.dismiss();
        	}
        });

    }
    
    /*
     * (non-Javadoc)
     * @see android.app.Activity#onDestroy()
     */
    public void onDestroy(){
    	super.onDestroy();
    	helper.close();
    	helper2.close();
    }
    
    /*
     * Method for when the "save" button is clicked.  Sends information to the database. Information
     * can be new and/or updated.
     */
    View.OnClickListener onSave=new View.OnClickListener() {
		
		public void onClick(View v) {
			
			if(!name.getText().toString().equals("") && !quantity.getText().toString().equals("")){
			
			if(itemID==null){
				helper.insert(name.getText().toString(), 
						quantity.getText().toString(), store);
			}
			else{
				helper.update(itemID, name.getText().toString(),
						quantity.getText().toString(), store);
			}
			finish();
			}
			
			else{
				AlertDialog alert = builder.create();
				alert.show();
			}
			
		}
	};
	
	/*
	 * Method for what happens when "new store" button is clicked.  Goes to the "AddStore" class.
	 */
    View.OnClickListener onNewStore=new View.OnClickListener() {
		
		public void onClick(View v) {
			
			Intent i = new Intent(AddItem.this, AddStore.class);
			startActivityForResult(i,1);

		}
	};
	
	/**Called when subactivity AddStore is finished and re-populates spinner.**/
	protected void onActivityResult (int requestCode, int resultCode, Intent data){
		stores.clear();
			model = helper2.getAll();
	        startManagingCursor(model);
	        model.moveToFirst();
	        stores.add("NONE");
	        while(!model.isAfterLast()){
	        	stores.add(model.getString(1));
	        	model.moveToNext();
	        	
	            
	        }
	        
	}
	
	/*
	 * Method for collecting information from Spinner on which store was selected.
	 */
	public class MyOnItemSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent,
	        View view, int pos, long id) {
	          store = parent.getItemAtPosition(pos).toString();
	    }

	    public void onNothingSelected(AdapterView parent) {
	      // Do nothing.
	    }
	}
	
	/*
	 * Method for when the "Cancel" button is clicked.  Activity simply ends and user returns to previous screen.
	 */
    View.OnClickListener onCancel=new View.OnClickListener() {
		
		public void onClick(View v) {

			finish();
		}
	};
    
}
