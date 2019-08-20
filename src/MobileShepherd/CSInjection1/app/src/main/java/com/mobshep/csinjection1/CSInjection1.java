package com.mobshep.csinjection1;

import java.io.File;
import java.io.IOException;

import net.sqlcipher.database.SQLiteDatabase;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

/**
 * This file is part of the Security Shepherd Project.
 * 
 * The Security Shepherd project is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.<br/>
 * 
 * The Security Shepherd project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.<br/>
 * 
 * You should have received a copy of the GNU General Public License
 * along with the Security Shepherd project.  If not, see <http://www.gnu.org/licenses/>. 
 * 
 * @author Sean Duggan
 */

public class CSInjection1 extends Activity implements OnClickListener {

	TabHost th;
	Button Login;
	EditText username;
	EditText password;
	EditText key;
	String dbPassword = "P93Eid3D33DE0ZanbffGpo101Sirjw2";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.csi);
		th = (TabHost) findViewById(R.id.tabhost);
		populateTable(this, dbPassword);
		referenceXML();
		th.setup();
		generateKey(this, dbPassword);

		TabSpec specs = th.newTabSpec("tag1");
		specs.setContent(R.id.tab1);
		specs.setIndicator("Login");
		th.addTab(specs);

		specs = th.newTabSpec("tag2");
		specs.setContent(R.id.tab2);
		specs.setIndicator("Key");
		th.addTab(specs);
	}

	private void referenceXML() {
		// TODO Auto-generated method stub
		Login = (Button) findViewById(R.id.bLogin);
		// Login.setFilterTouchesWhenObscured(true);
		username = (EditText) findViewById(R.id.etName);
		password = (EditText) findViewById(R.id.etPass);
		key = (EditText) findViewById(R.id.etKey);
		Login.setOnClickListener(this);

	}

	public void onClick(View arg0) {
		switch (arg0.getId()) {

		case (R.id.bLogin):

			String unsanitizeName = username.getText().toString();
			String unsanitizePass = password.getText().toString();

			String sanitizeName = unsanitizeName.replace("OR", " ");
			sanitizeName = sanitizeName.replace("or", " ");
			sanitizeName = sanitizeName.replace("SELECT", " ");
			sanitizeName = sanitizeName.replace("AND", " ");
			sanitizeName = sanitizeName.replace("UPDATE", " ");
			sanitizeName = sanitizeName.replace("DROP", " ");
			sanitizeName = sanitizeName.replace("1=1", " ");
			sanitizeName = sanitizeName.replace("1 = 1", " ");

			String sanitizePass = unsanitizePass.replace("OR", "  ");
			sanitizePass = sanitizePass.replace("or", "  ");
			sanitizePass = sanitizePass.replace("SELECT", "  ");
			sanitizePass = sanitizePass.replace("AND", "  ");
			sanitizePass = sanitizePass.replace("UPDATE", "  ");
			sanitizePass = sanitizePass.replace("DROP", "  ");
			sanitizePass = sanitizePass.replace("1=1", "  ");
			sanitizePass = sanitizePass.replace("1 = 1", "  ");

			try {
				if (login(sanitizeName, sanitizePass) == true) {
					outputKey(this, dbPassword);
					                    Toast loggedin = Toast.makeText(CSInjection1.this,
                            "Logged in!", Toast.LENGTH_LONG);
                    loggedin.show();

				}
			} catch (IOException e1) {
				Toast error = Toast.makeText(CSInjection1.this,
						"An error occurred!", Toast.LENGTH_LONG);
				error.show();
			}

			try {
				if (login(sanitizeName, sanitizePass) == false) {
					Toast invalid = Toast.makeText(CSInjection1.this,
							"Invalid Credentials, " + sanitizeName,
							Toast.LENGTH_LONG);
					invalid.show();

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (sanitizeName.contentEquals("")
					|| sanitizePass.contentEquals("")) {
				Toast blank = Toast.makeText(CSInjection1.this,
						"Empty Fields Detected.", Toast.LENGTH_SHORT);
				blank.show();

			}

		}

	}

	private boolean login(String username, String password) throws IOException {
try{
		try {
			String dbPath = this.getDatabasePath("Users.db").getPath();

			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath,
					dbPassword, null);

			String query = ("SELECT * FROM Users WHERE memName='" + username
					+ "' AND memPass = '" + password + "';");

			Cursor cursor = db.rawQuery(query, null);
			if (cursor != null) {
				if (cursor.getCount() <= 0) {
					return false;
				}
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast error = Toast.makeText(CSInjection1.this,
					"An error occurred.", Toast.LENGTH_LONG);
			error.show();
			key.getText().clear();
			key.setHint("The key is only shown to authenticated users.");
			return false;
		}

		} catch (SQLiteException e) {
			Toast error = Toast.makeText(CSInjection1.this,
					"An database error occurred.", Toast.LENGTH_LONG);
			error.show();
		}

		return true;

	}

	public void populateTable(Context context, String dbpassword) {
		try {

			try {
				SQLiteDatabase.loadLibs(context);

				String dbPath = context.getDatabasePath("Users.db").getPath();

				File dbPathFile = new File(dbPath);
				if (!dbPathFile.exists())
					dbPathFile.getParentFile().mkdirs();

				SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath,
						dbpassword, null);

				db.execSQL("DROP TABLE IF EXISTS Users");
				db.execSQL("CREATE TABLE Users(memID INTEGER PRIMARY KEY AUTOINCREMENT, memName TEXT, memAge INTEGER, memPass VARCHAR)");

				db.execSQL("INSERT INTO Users VALUES( 1,'Admin',20,'49c3a17ad8d8ccd93885e6a28661480d')");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Toast error = Toast.makeText(CSInjection1.this,
						"An error occurred.", Toast.LENGTH_LONG);
				error.show();

			}

		} catch (SQLiteException e) {
			Toast error = Toast.makeText(CSInjection1.this,
					"An database error occurred.", Toast.LENGTH_LONG);
			error.show();
		}
	}
	
	public void outputKey(Context context, String password) {
		SQLiteDatabase.loadLibs(context);

		String dbPath = context.getDatabasePath("key.db").getPath();

		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath, dbPassword,
				null);

		String query = ("SELECT * FROM key;");

		Cursor cursor = db.rawQuery(query, null);

		if (cursor != null) {

			try {
				if (cursor.moveToFirst())
					key.setText(cursor.getString(0));
			} finally {
				cursor.close();

			}
		}
	}
	
	public void generateKey(Context context, String password) {
		try {
			try {
				SQLiteDatabase.loadLibs(context);

				String dbPath = context.getDatabasePath("key.db").getPath();

				File dbPathFile = new File(dbPath);
				if (!dbPathFile.exists())
					dbPathFile.getParentFile().mkdirs();

				SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbPath,
						dbPassword, null);

				db.execSQL("DROP TABLE IF EXISTS key");
				db.execSQL("CREATE TABLE key(key VARCHAR)");

				db.execSQL("INSERT INTO key VALUES('The Key is SourHatsAndAngryCats.')");

			} catch (Exception e) {
				// TODO Auto-generated catch block
				Toast error = Toast.makeText(CSInjection1.this,
						"An error occurred.", Toast.LENGTH_LONG);
				error.show();

			}

		} catch (SQLiteException e) {
			Toast error = Toast.makeText(CSInjection1.this,
					"An database error occurred.", Toast.LENGTH_LONG);
			error.show();
		}
	}
}
