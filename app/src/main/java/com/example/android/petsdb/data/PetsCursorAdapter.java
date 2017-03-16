package com.example.android.petsdb.data;

/**
 * Created by shuvo on 3/15/17.
 */

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.petsdb.R;

public class PetsCursorAdapter extends CursorAdapter {

    public PetsCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
       // return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find fields to populate in inflated template
        TextView petName = (TextView) view.findViewById(R.id.name);
        TextView petBreed = (TextView) view.findViewById(R.id.summary);
        // Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow(PetsContract.PetsEntry.COLUMN_NAME));
        String breed = cursor.getString(cursor.getColumnIndexOrThrow(PetsContract.PetsEntry.COLUMN_BREED));
        // Populate fields with extracted properties
        petName.setText(name);

        if (TextUtils.isEmpty(breed)) {
            petBreed.setText("Unknown breed");
        }
        else {
            petBreed.setText(breed);
        }
    }
}