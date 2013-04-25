package edu.macalester.potato;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.Cursor;
import android.content.ContentValues;

/**
 * Item Database Class
 * 
 * Class that defines the database that will hold items in the list.  Provides methods for manipulating the database
 * 
 * @author Potato Team
 *
 */
class ItemHelper extends SQLiteOpenHelper{
	private static final String DATABASE_NAME="ItemList.db";
	private static final int SCHEMA_VERSION=1;
	
	/**
	 * Constructor
	 * @param context where program is called from
	 */
	public ItemHelper(Context context){
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE items (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"name TEXT, quantity TEXT, store TEXT);");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//no-op, since will not be called till 2nd version
		
	}
	
	/**
	 *  Updates the database with new information for a particular item
	 * @param id ID of item in db
	 * @param name Name of item to be saved in db
	 * @param quantity Quantity of item to be saved in db.
	 * @param store Store/location of item to be saved in db
	 */
	public void update(String id, String name, String quantity, String store){
		ContentValues cv = new ContentValues();
		String[] args = {id};
		cv.put("name", name);
		cv.put("quantity", quantity);
		cv.put("store", store);
		
		getWritableDatabase().update("items", cv, "_ID=?", args);
		
	}
	
	/**
	 *  Inserts a new item into the database
	 * @param name Name of item to be saved
	 * @param quantity Quantity of item to be saved
	 * @param store Store/location of item to be saved
	 */
	public void insert(String name, String quantity, String store){
		ContentValues cv=new ContentValues();
		
		cv.put("name", name);
		cv.put("quantity", quantity);
		cv.put("store", store);
		
		getWritableDatabase().insert("items", "name", cv);
	}
	
	/**
	 *  Deletes an item with the corresponding Id from the database.
	 * @param id ID of item in db.
	 */
	public void delete(String id){
		String[] args = {id};
		
		getWritableDatabase().delete("items","_ID=?", args);
	}
	
	/**
	 *  Returns a cursor over all the items in the database
	 * @return
	 */
	public Cursor getAll(){
		return getReadableDatabase().rawQuery("SELECT _id, name, quantity, store FROM items ORDER BY name", null);
	}
	
	/**
	 * Returns a cursor over all items at desired store
	 * 
	 * @param storeName store to be queried
	 * @return cursor over items at storeName
	 */
	public Cursor getFromStore(String storeName){
		String[] args = {storeName};
		return getReadableDatabase().rawQuery("SELECT _id, name, quantity, store FROM items WHERE store like ? ORDER BY name", args);
	}
	
	/**
	 *  Returns name of item
	 * @param c Cursor over items
	 * @return item name
	 */
	public String getName(Cursor c){
		return c.getString(1);
	}
	
	/**
	 * Returns quantity ofbocovichbocovich item
	 * @param c Cursor pointing to item
	 * @return item quantity
	 */
	public String getQuantity(Cursor c){
		return c.getString(2);
	}
	
	/**
	 * Returns store/location of item
	 * @param c Cursor pointing to item
	 * @return item store/location
	 */
	public String getStore(Cursor c){
		return c.getString(3);
	}
	
	/**
	 *  Returns a cursor over item with given Id.
	 *  
	 * @param id
	 * @return
	 */
	public Cursor getById(String id){
		String[] args = {id};
		
		return getReadableDatabase().rawQuery("SELECT _id, name, quantity, store FROM items WHERE _ID=?", args);
	}

}
