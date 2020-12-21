package com.example.smartroom.common;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.smartroom.R;

public class MyCursorAdapter extends SimpleCursorAdapter {

    public MyCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    @Override
    public int getCount()
    {
        return super.getCount();
    }
    @Override
    public Object getItem(int position)
    {
        return super.getItem(position);
    }
    @Override
    public long getItemId(int position)
    {
        return super.getItemId(position);
    }

}
