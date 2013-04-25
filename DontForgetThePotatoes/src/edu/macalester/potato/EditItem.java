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
 * EditItem
 *  
 * Class for editing items already in the list.  When an item from the list is selected, this class is called and the information
 * for the item is pre-filled into the page.  User can change item name, quantity, and store.
 *  
 * @author Potato Team
 */
public class EditItem extends Activity{
	Cursor model = null;
	EditText name = null;
	EditText quantity=null;
	String store =null;
	Spinner spinner=null;
	ItemHelper helper = null;
	StoreHelper helper2=null;
	String itemID = null;
	AlertDialog.Builder builder = null;
	ArrayList<String> stores = new ArrayList<String>();
	public final static String ID_EXTRA="edu.macalester.potato._ID";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        
        helper = new ItemHelper(this);//opens access to database
        helper2 = new StoreHelper(this);
        name = (EditText)findViewById(R.id.name);
        quantity=(EditText)findViewById(R.id.quantity);
        
        stores.add("NONE");
        model = helper2.getAll();
        startManagingCursor(model);
        model.moveToFirst();
        while(!model.isAfterLast()){
        	stores.add(model.getString(1));
        	model.moveToNext();
        }
        
        //set up spinner that displays choice of current stores.
        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
        
        //set up display buttons
        Button save = (Button)findViewById(R.id.save2);
        Button newStore = (Button)findViewById(R.id.newStore2);
        
        save.setOnClickListener(onSave);
        newStore.setOnClickListener(onNewStore);
        
        //set up alert dialog that keeps user from entering empty items
        builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Please enter both a name and quantity.");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener(){
        	public void onClick(DialogInterface dialog, int which){
        		dialog.dismiss();
        	}
        });
        
        
        itemID = getIntent().getStringExtra(SearchList.ID_EXTRA);

        if(itemID !=null){
        	load();
    }
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
    
    /**
     * Method for the "Save" button.  Information on the edited item will be saved when this is clicked
     * and then the activity will finish.
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
	
	/**
	 * Method for when the user selects the "New Store" button.  The AddStore activity will be called.
	 * 
	 */
    View.OnClickListener onNewStore=new View.OnClickListener() {
		
		public void onClick(View v) {
			
			Intent i = new Intent(EditItem.this, AddStore.class);
			startActivityForResult(i,1);
			
			model.requery();
		}
	};
	
	/**Called when subactivity AddStore is finished and re-populates spinner.**/
	protected void onActivityResult (int requestCode, int resultCode, Intent data){
		stores.clear();
		stores.add("NONE");
			model = helper2.getAll();
	        startManagingCursor(model);
	        model.moveToFirst();
	        while(!model.isAfterLast()){
	        	stores.add(model.getString(1));
	        	model.moveToNext();
	        }
	}
	
	/*
	 * Method for retrieving information on which store was selected.
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
		
	/**
	 * Loads information of item to be edited into the display
	 */
	private void load(){
		Cursor c=helper.getById(itemID);
		c.moveToFirst();
		name.setText(helper.getName(c));
		quantity.setText(helper.getQuantity(c));
		store = helper.getStore(c);
		int pos = stores.indexOf(store);
		spinner.setSelection(pos);
		c.close();
	}
	
	/**
	 * Inner class to populate display
	 * @author Potato Team
	 *
	 */
	class StoreAdapter extends CursorAdapter{
		StoreAdapter(Cursor c){
			super(EditItem.this, c);
		}
		
		
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			ItemHolder holder = (ItemHolder)view.getTag();
			holder.populateFrom(cursor,helper);
			
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			LayoutInflater inflater = getLayoutInflater();
			View row=inflater.inflate(R.layout.row, parent, false);
			ItemHolder holder = new ItemHolder(row);
			
			row.setTag(holder);
			
			return row;
		}	
	}
    
}
