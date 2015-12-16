package com.exjobb.emil.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;

import static android.view.View.*;


public class StoreActivity extends Activity {

    int[] ids = {R.id.dairyButton, R.id.meatButton, R.id.vegetablesButton, R.id.candyButton};
    String[] tags = {"dairy", "meat", "vegetables", "candy"};
    ListView shoppingList;
    ShoppingListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        View[] shelves = new View[4];
        for (int i = 0; i < shelves.length; i++) {
            shelves[i] = findViewById(ids[i]);
            shelves[i].setOnClickListener(shelfListen);
            shelves[i].setTag(tags[i]);
        }

        //Setup Shopping-list
        shoppingList = (ListView) findViewById(R.id.shoppingListView);
        adapter = new ShoppingListAdapter(this, ((Global)getApplication()).getShoppingList());
        shoppingList.setAdapter(adapter);

        //Change shoppinglist header type
        Typeface type = Typeface.createFromAsset(this.getAssets(), "fonts/ComingSoon.ttf");
        TextView shoppingListHeader = (TextView) findViewById(R.id.shoppingListHeader);
        shoppingListHeader.setTypeface(type);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_store, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    OnClickListener shelfListen = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getTag().toString().equals("dairy") || v.getTag().toString().equals("vegetables")) {
                Intent i = new Intent(v.getContext(), ShelfActivity.class);
                i.putExtra("category", v.getTag().toString());
                startActivity(i);
            }
        }
    };

    public void toCheckout(View view) {
        if ((((Global)getApplication()).getCartSize()) == (((Global)getApplication()).getShoppingList().size())) {
            startActivity(new Intent(this, CheckoutActivity.class));
            finishAffinity();
        } else {
            showToast();
        }
    }

    private void showToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_negative,
                    (ViewGroup) findViewById(R.id.toast_layout_negative));
        TextView text = (TextView) layout.findViewById(R.id.text1);
        text.setText("Handla färdigt innan du går till kassan");

        TextView toRemove = (TextView) layout.findViewById(R.id.text2);
        ((LinearLayout)toRemove.getParent()).removeView(toRemove);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

}
