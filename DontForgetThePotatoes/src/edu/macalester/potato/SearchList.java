package edu.macalester.potato;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.app.TabActivity;
import android.widget.AdapterView;

/**
 * SearchList
 * 
 * Displays information to user including a list of items.  Gives the user the option to add items to the list.
 * 
 * @author Potato Team
 */
public class SearchList extends ListActivity{
	Cursor model = null;
	ItemAdapter adapter=null;
	ItemHelper helper=null;
	AlertDialog.Builder builder = null;
	long currentId;
	int currentPosition;
	public final static String ID_EXTRA="edu.macalester.potato._ID";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        
        Button addAnItem=(Button)findViewById(R.id.addAnItem);
        addAnItem.setOnClickListener(onAdd);
        
        helper = new ItemHelper(this);//opens access to database
        //Populates ItemAdapter
        model=helper.getAll();
        startManagingCursor(model);
        adapter=new ItemAdapter(model);
        setListAdapter(adapter);
        
        
       //creates a dialog that pops up when a list item is selected and
        //allows the user to either edit or delete item.
        builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        //builder.setTitle("Please enter both a name and quantity.");
        builder.setInverseBackgroundForced(true);
        builder.setNeutralButton("Edit Item", new DialogInterface.OnClickListener(){
        	public void onClick(DialogInterface dialog, int which){
        		Intent i = new Intent(SearchList.this, EditItem.class);
        		i.putExtra(ID_EXTRA, String.valueOf(currentId));
        		startActivity(i);
        	}
        });
        builder.setNegativeButton("Delete Item", new DialogInterface.OnClickListener(){
        	public void onClick(DialogInterface dialog, int which){
        		model.moveToPosition(currentPosition);
        		String itemid = model.getString(0);
        		helper.delete(itemid);
        		model.requery();//Refresh activity

        	}
        });

    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	helper.close();//close access to the database.
    }
    
    /*
     * Method for when "Add Item" button is clicked.
     *
     */
    View.OnClickListener onAdd=new View.OnClickListener() {
		
		public void onClick(View v) {
			Intent i = new Intent(SearchList.this, AddItem.class);
			startActivity(i);
		}
	};
    
	/* Method for when the item is selected.  Creates an alert window that asks the user what they would like to do.
	 * (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	public void onListItemClick(ListView list, View view,
			int position, long id){
		AlertDialog alert = builder.create();
		alert.show();
		currentId = id;
		currentPosition = position;
	}
	
	//For saving item information in an array
	class ItemAdapter extends CursorAdapter{
		ItemAdapter(Cursor c){
			super(SearchList.this, c);
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
	
	//Simplifies code, for loading array into view
	static class ItemHolder{
		private TextView name = null;
		private TextView quantity=null;
		private TextView location=null;
		private ImageView icon=null;
		
		ItemHolder(View row){
			name=(TextView)row.findViewById(R.id.title);
			quantity=(TextView)row.findViewById(R.id.quantity);
			location=(TextView)row.findViewById(R.id.location);
			icon=(ImageView)row.findViewById(R.id.icon);
		}
		
		void populateFrom(Cursor c, ItemHelper helper){
			name.setText(helper.getName(c));
			quantity.setText(helper.getQuantity(c));
			location.setText(helper.getStore(c));
			icon.setImageResource(R.drawable.bag);
		}
	}
}
