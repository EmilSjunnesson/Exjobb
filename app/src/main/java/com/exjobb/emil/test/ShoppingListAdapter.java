package com.exjobb.emil.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Emil on 2015-04-08.
 */
public class ShoppingListAdapter extends ArrayAdapter {
    ArrayList<Item> items = new ArrayList<Item>();
    Context context;
    Typeface type;

    public ShoppingListAdapter(Context context, ArrayList<Item> resource) {
        super(context, R.layout.row, resource);
        this.context = context;
        this.items = resource;
        type = Typeface.createFromAsset(context.getAssets(), "fonts/ComingSoon.ttf");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.row, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.textView1);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
        name.setTypeface(type);
        name.setText(items.get(position).getName());
        cb.setChecked(items.get(position).isBought());
        return convertView;
    }
}