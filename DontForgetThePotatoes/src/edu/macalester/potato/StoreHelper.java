package edu.macalester.potato;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Store Database Class
 * 
 * Class for the database containing store information.  Saves store name and address.
 * Has methods for updating and adding stores to the database as well as returning cursors over
 * items in the database.
 * 
 * @author Potato Team
 *
 */
public class StoreHelper extends SQLiteOpenHelper{
	private static final String DATABASE_NAME="StoreList.db";
	private static final int SCHEMA_VERSION=2;
	
	/**
	 * Constructor
	 * @param context setting in which constructor is called
	 */
	public StoreHelper(Context context){
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE stores (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"name TEXT, address TEXT);");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//no-op, since will not be called till 2nd ver
		
	}
	
	/**
	 * Updates database with new information of store
	 * @param id
	 * @param name
	 * @param address
	 */
	public void update(String id, String name, String address){
		ContentValues cv = new ContentValues();
		String[] args = {id};
		cv.put("name", name);
		cv.put("address", address);
		
		getWritableDatabase().update("stores", cv, "_ID=?", args);
		
	}
	
	/**
	 * Inserts a new store into the database.
	 * @param name
	 * @param address
	 */
	public void insert(String name, String address){
		ContentValues cv=new ContentValues();
		
		cv.put("name", name);
		cv.put("address", address);
		
		getWritableDatabase().insert("stores", "name", cv);
	}
	
	/**
	 * Deletes selected store from database
	 * @param id
	 */
	public void delete(String id){
		String[] args = {id};
		
		getWritableDatabase().delete("stores","_ID=?", args);
	}
	
	/**
	 * Returns a cursor over all items in the store database
	 * @return
	 */
	public Cursor getAll(){
		return getReadableDatabase().rawQuery("SELECT _id, name, address FROM stores ORDER BY _id", null);
	}
	
	/**
	 * Returns the name of the store
	 * @param c
	 * @return
	 */
	public String getName(Cursor c){
		return c.getString(1);
	}
	
	/**
	 * Returns the quantity of the address of the store
	 * @param c
	 * @return
	 */
	public String getAddress(Cursor c){
		return c.getString(2);
	}
	
	/**
	 * Returns a cursor over just item with given id.
	 * @param id
	 * @return
	 */
	public Cursor getById(String id){
		String[] args = {id};
		
		return getReadableDatabase().rawQuery("SELECT _id, name, address FROM stores WHERE _ID=?", args);
	}

}
