/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.petsdb;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.petsdb.data.PetsContract;
import com.example.android.petsdb.data.PetsCursorAdapter;
import com.example.android.petsdb.data.PetsDBHelper;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final int PET_LOADER = 0;
    PetsCursorAdapter mCursorAdapter;
    private PetsDBHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView petListView = (ListView) findViewById(R.id.list);
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);

        mCursorAdapter = new PetsCursorAdapter(this,null);
        petListView.setAdapter(mCursorAdapter);

        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                Uri currentPetUri = ContentUris.withAppendedId(PetsContract.PetsEntry.CONTETNT_URI, id);
                intent.setData(currentPetUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(PET_LOADER, null, this);

//        displayDatabaseInfo();

    }

    /**
     * First attempt to read data from database without using content provider
     */

    private void displayDatabaseInfo() {
        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        //PetsDBHelper mDbHelper = new PetsDBHelper(this);

        // Create and/or open a database to read from it
    //    SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                PetsContract.PetsEntry._ID,
                PetsContract.PetsEntry.COLUMN_NAME,
                PetsContract.PetsEntry.COLUMN_BREED,
                PetsContract.PetsEntry.COLUMN_GENDER,
                PetsContract.PetsEntry.COLUMN_WEIGHT
        };

//        Cursor cursor = db.query(
//                PetsContract.PetsEntry.TABLE_NAME,
//                projection,
//                null,
//                null,
//                null,
//                null,
//                null);

        Cursor cursor = getContentResolver().query(
                PetsContract.PetsEntry.CONTETNT_URI,
                projection,
                null,
                null,
                null);

        // Find ListView to populate
        ListView lvItems = (ListView) findViewById(R.id.list);
// Setup cursor adapter using cursor from last step
        PetsCursorAdapter petsCursorAdapter = new PetsCursorAdapter(this, cursor);
// Attach cursor adapter to the ListView
        lvItems.setAdapter(petsCursorAdapter);

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
    //    Cursor cursor = db.rawQuery("SELECT * FROM " + PetsContract.PetsEntry.TABLE_NAME, null);

      //  TextView displayView = (TextView) findViewById(R.id.text_view_pet);

//        try {
//            // Display the number of rows in the Cursor (which reflects the number of rows in the
//            // pets table in the database).
//
//            displayView.setText("The pets table contains "+ cursor.getCount() + " pets \n\n");
//            displayView.append(PetsContract.PetsEntry._ID +
//                    " - " + PetsContract.PetsEntry.COLUMN_NAME +
//                    " - " + PetsContract.PetsEntry.COLUMN_BREED +
//                    " - " + PetsContract.PetsEntry.COLUMN_GENDER +
//                    " - " + PetsContract.PetsEntry.COLUMN_WEIGHT + "\n");
//
//            int idColumnIndex = cursor.getColumnIndex(PetsContract.PetsEntry._ID);
//            int nameColumnIndex = cursor.getColumnIndex(PetsContract.PetsEntry.COLUMN_NAME);
//            int breedColumnIndex = cursor.getColumnIndex(PetsContract.PetsEntry.COLUMN_BREED);
//            int genderColumnIndex = cursor.getColumnIndex(PetsContract.PetsEntry.COLUMN_GENDER);
//            int weightColumnIndex = cursor.getColumnIndex(PetsContract.PetsEntry.COLUMN_WEIGHT);
//
//            while (cursor.moveToNext()) {
//
//                int currentID = cursor.getInt(idColumnIndex);
//                String currentName = cursor.getString(nameColumnIndex);
//                String breedName = cursor.getString(breedColumnIndex);
//                String genderName = cursor.getString(genderColumnIndex);
//                int weight = cursor.getInt(weightColumnIndex);
//
//                displayView.append(("\n" + currentID + " - " + currentName + " - " + breedName + " - " + genderName + " - " + weight));
//
//            }
//          //  displayView.setText("Number of rows in pets database table: " + cursor.getCount());
//        } finally {
//            // Always close the cursor when you're done reading from it. This releases all its
//            // resources and makes it invalid.
//            cursor.close();
//        }
    }

    private void insertPet() {

        mDbHelper = new PetsDBHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PetsContract.PetsEntry.COLUMN_NAME, "Toto");
        values.put(PetsContract.PetsEntry.COLUMN_BREED, "Terrier");
        values.put(PetsContract.PetsEntry.COLUMN_GENDER, PetsContract.PetsEntry.GENDER_MALE);
        values.put(PetsContract.PetsEntry.COLUMN_WEIGHT, 7);

//        long newRowID = db.insert(PetsContract.PetsEntry.TABLE_NAME, null, values);
//        Log.v("new Row ID: ", " "+newRowID);
        Uri newUri = getContentResolver().insert(PetsContract.PetsEntry.CONTETNT_URI, values);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
            //    displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                deleteAllPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                PetsContract.PetsEntry._ID,
                PetsContract.PetsEntry.COLUMN_NAME,
                PetsContract.PetsEntry.COLUMN_BREED,
        };

        return new CursorLoader(this,
                PetsContract.PetsEntry.CONTETNT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader loader) {
        mCursorAdapter.swapCursor(null);
    }

    private void deleteAllPets() {
        int rowsDeleted = getContentResolver().delete(PetsContract.PetsEntry.CONTETNT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
    }
}
