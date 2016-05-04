package com.shopsy.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

/**
 * @author prithiviraj
 *
 */

public class DBHelper extends SQLiteOpenHelper 
{
	// Static Final Variable database meta information

	public static final String DATABASE_NAME = "shopsy1.db";
	public static final int VERSION = 1;
	public static final String TABLE = "country_list";

	public static final String C_ID = "c_id";
	public static final String C_NAME = "name";
	public static final String C_id = "id";
	
	Context ctx;
	
	// Override constructor
	public DBHelper(Context context) 
	{
		super(context, DATABASE_NAME, null, VERSION);
		context=this.ctx;

	}

	// Override onCreate method
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL("CREATE TABLE " + TABLE + " ( " + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + C_id + " text, " + C_NAME + " text )");
		
		try
		{
			copyDataBase();
		} 
		catch (IOException e)
		{
			Log.d("IO_Exception",""+e);
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{

		// Drop old version table
		db.execSQL("Drop table " + TABLE);

		// Create New Version table
		onCreate(db);
	}

	@SuppressLint("SdCardPath") 
	public void copyDataBase() throws IOException 
	{
		try 
		{
	        InputStream myInput = new FileInputStream("/data/data/com.justlock.app/databases/"+DATABASE_NAME);

	        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/"+DATABASE_NAME);
	        if (!file.exists())
	        {
	            try 
	            {
	                file.createNewFile();
	            }
	            catch (IOException e) 
	            {
	                Log.d("File creation failed for ","" + file);
	            }
	        }

	        OutputStream myOutput = new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/"+DATABASE_NAME);

	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = myInput.read(buffer))>0)
	        {
	            myOutput.write(buffer, 0, length);
	        }
	        //Close the streams
	        myOutput.flush();
	        myOutput.close();
	        myInput.close();
	        Log.i("FO","copied");

	    } catch (Exception e) 
	    {
	        Log.d("exception=",""+e);
	    }
    }
}