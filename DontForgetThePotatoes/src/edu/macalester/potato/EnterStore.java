package edu.macalester.potato;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import edu.macalester.potato.AddItem.MyOnItemSelectedListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.app.TabActivity;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * EnterStore class
 * 
 * When user enters a store, displays items in that store and provides a way to check off
 * items.
 * 
 * @author Potato team
 *
 */
public class EnterStore extends ListActivity{
	Cursor model = null;
	String store =null;
	public static Vibrator earthquake;
	Cursor model2 = null;
	Cursor model3 = null;
	ItemAdapter adapter=null;
	ItemAdapter adapter3=null;
	ItemHelper helper=null;
	StoreHelper helper2 = null;
	AlertDialog.Builder builder = null;
	AlertDialog.Builder builder2 = null;
	AlertDialog.Builder builder3 = null;
	String currentStore = null;
	Spinner spinner = null;
	String current = null;
	Timer timer;
	long currentId;
	
	int currentPosition;
	ArrayList<String> stores = new ArrayList<String>();
	ArrayList<String> items = new ArrayList<String>();
	
	//All variables for the GPS
	LocationManager mlocManager;
	LocationListener mlocListener;
	Location lastKnown;
	double savedLat;
	double savedLong;
	String provider;
	
	//Keeps track of whether or not user has returned to initial spot.
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter);
        earthquake = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        helper = new ItemHelper(this);//opens access to database
        helper2 = new StoreHelper(this);
        
        //Populates Store Adapter
        
        lastKnown = null;
        stores.add("NONE");
        
        model = helper2.getAll();
        startManagingCursor(model);
       
        model.moveToFirst();
        while(!model.isAfterLast()){
        	stores.add(model.getString(1));
        	model.moveToNext();
        	
        }
       
        
        
        
        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stores);
        
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);
        spinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
        //Shows an alert dialog at the start of the activity which allows the user
        //to specify to store being entered.
        
        builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
       
        builder.setInverseBackgroundForced(true);
        
        builder.setNegativeButton("Check Off", new DialogInterface.OnClickListener(){
        	public void onClick(DialogInterface dialog, int which){
        		model2.moveToPosition(currentPosition);
        		
        		String itemid = model2.getString(0);
        		String itemName= model2.getString(1);
        		helper.delete(itemid);
        		items.remove(itemName);
        		
        		//Refresh list
        		model2.requery();
        	}
        });
        
        //set up dialog for when gps senses user exits store.
        builder2 = new AlertDialog.Builder(this);
        builder2.setCancelable(true);
       
        builder2.setInverseBackgroundForced(true);
        builder2.setTitle("You are leaving the store!  Did you forget any items?");
        
        builder2.setPositiveButton("Okay", new DialogInterface.OnClickListener(){
        	public void onClick(DialogInterface dialog, int which){
        		dialog.dismiss();

        	}
        });
        builder3 = new AlertDialog.Builder(this);
        builder3.setCancelable(true);
        builder3.setInverseBackgroundForced(true);
        
     
        builder3.setPositiveButton("There are still items to buy!", new DialogInterface.OnClickListener(){
        	public void onClick(DialogInterface dialog, int which){
        		
        		earthquake.cancel();
        		dialog.dismiss();
        	}
        	
    });
        /* Use the LocationManager class to obtain GPS locations */
        mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        
		provider = LocationManager.NETWORK_PROVIDER;
        
		//Set methods for Location Listener
        mlocListener = new LocationListener() {
            public void onLocationChanged(Location loc) {
            	
              // Called when a new location is found by the network location provider.
    			lastKnown = loc;
    			float results[] = new float[1];
    			Location.distanceBetween(savedLat, savedLong, lastKnown.getLatitude(), lastKnown.getLongitude(),results);
    			long [] pattern = {0, 500, 500};
    			if((results[0] <= 10f)&&items.isEmpty()==false){
    					earthquake.vibrate(pattern, 0);
            			AlertDialog alert = builder3.create();
            			alert.show();
            			
            			
            			
            		
    			}
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {
            	Toast.makeText( getApplicationContext(),"Gps Enabled",Toast.LENGTH_SHORT).show();
            }

            public void onProviderDisabled(String provider) {
            	Toast.makeText( getApplicationContext(),"GPS Disabled",Toast.LENGTH_SHORT ).show();
            }
        };
        mlocManager.requestLocationUpdates(provider, 1000, 0, mlocListener);
          
        getLocation();
        
   
    }
    
    @Override
    public void onDestroy(){
    	mlocManager.removeUpdates(mlocListener);
    	lastKnown.reset();
 
    	super.onDestroy();
    	helper.close();//close access to the database.
    	helper2.close();
    	//mlocManager.removeUpdates(mlocListener);
    }
 
    
    
    private void getLocation(){
    	provider = LocationManager.NETWORK_PROVIDER;
    	Toast.makeText(this, "Finding location...", Toast.LENGTH_SHORT).show();
    	if (lastKnown != null)
        {
        	savedLat = lastKnown.getLatitude();
        	savedLong = lastKnown.getLongitude();
        	Toast.makeText(this, "Latitude: "+savedLat+"Longitude: "+savedLong, Toast.LENGTH_SHORT).show();
        }
    	else if ((lastKnown=mlocManager.getLastKnownLocation(provider))!=null){
    		savedLat = lastKnown.getLatitude();
        	savedLong = lastKnown.getLongitude();
        	Toast.makeText(this, "Latitude: "+savedLat+"Longitude: "+savedLong, Toast.LENGTH_SHORT).show();
    	}
        else
        	Toast.makeText(this, "No location available", Toast.LENGTH_SHORT).show();
    	
    }
    
    public void onListItemClick(ListView list, View view,
			int position, long id){
		AlertDialog alert = builder.create();
		alert.show();
		currentId = id;
		currentPosition = position;
		
	}
	
	/**
	 *  Inner class that populates list
	 */
	class ItemAdapter extends CursorAdapter{
		ItemAdapter(Cursor c){
			super(EnterStore.this, c);
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
	public class MyOnItemSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent,
	        View view, int pos, long id) {
	    	items.clear();
        	
        	currentStore = parent.getItemAtPosition(pos).toString();
        		
            model2 = helper.getFromStore(currentStore);//Cursor over items in desired store.
            startManagingCursor(model2);
                
            adapter=new ItemAdapter(model2);
            setListAdapter(adapter);
            
            model2.moveToFirst();
            while(!model2.isAfterLast()){
            	items.add(model2.getString(1));
            	model2.moveToNext();            
            }
            
	    }

	    public void onNothingSelected(AdapterView parent) {
	      // Do nothing.
	    }
	   
	}
	
	/**
	 * 
	 * @author Potato Team
	 * 
	 * Class to handle gps functionality.  Collects information on changes in location and alerts user when leaving
	 * the vicinity of the store.
	 *
	 */
	
}
