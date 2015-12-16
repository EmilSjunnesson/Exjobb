package com.exjobb.emil.test;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;

import static android.view.View.*;


public class ShelfActivity extends Activity {

    View[] itemViews = new View[15];
    ListView shoppingList;
    ShoppingListAdapter adapter;
    ArrayList<Item> items;
    SoundPool sp;
    int soundIds[] = new int[10];
    private static Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelf);

        //Get items from Global
        Intent intent = getIntent();
        String category = intent.getStringExtra("category");
        items = ((Global)getApplication()).getItemsFromCategory(category);

        findViewById(R.id.cart).setOnDragListener(dropListener);

        setupItemViews();

        //Setup Shopping-list
        shoppingList = (ListView) findViewById(R.id.shoppingListView);
        adapter = new ShoppingListAdapter(this, ((Global)getApplication()).getShoppingList());
        shoppingList.setAdapter(adapter);

        //Change shoppinglist header type
        Typeface type = Typeface.createFromAsset(this.getAssets(), "fonts/ComingSoon.ttf");
        TextView shoppingListHeader = (TextView) findViewById(R.id.shoppingListHeader);
        shoppingListHeader.setTypeface(type);

        //Setup sound
        if((android.os.Build.VERSION.SDK_INT) >= 21) {
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(10);
            builder.setAudioAttributes(new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build());
            sp = builder.build();
        } else {
            sp = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        }
        soundIds[0] = sp.load(this, R.raw.metal, 1);
        soundIds[1] = sp.load(this, R.raw.error, 1);

        // Set shelf title
        String title = "";
        if(category.equals("meat")) {
            title = "Kött";
        } else if(category.equals("vegetables")) {
            title = "Frukt och Grönt";
        } else if(category.equals("dairy")) {
            title = "Mejeriprodukter";
        } else if(category.equals("candy")) {
            title = "Godis och Snacks";
        }
        TextView textView = (TextView) findViewById(R.id.shelfText);
        textView.setText(title);

        //Back button
        Button back = (Button) findViewById(R.id.backButton);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_shelf, menu);
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

    OnTouchListener itemListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int motionEvent = event.getAction();
            Log.i("tag", "touched");
            switch (motionEvent) {
                case MotionEvent.ACTION_DOWN:
                    ClipData data = ClipData.newPlainText("", "");
                    DragShadowBuilder dragShadow = new DragShadowBuilder(v);
                    Log.i("tag", "clicked");
                    v.startDrag(data, dragShadow, v.getTag(), 0);
                    break;
            }
            return false;
        }
    };

    OnDragListener dropListener = new OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent = event.getAction();

            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.i("Drag Event", "Entered");
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.i("Drag Event", "Exited");
                    break;
                case DragEvent.ACTION_DROP:
                    Item item = (Item) event.getLocalState();

                    //Add item to cart or display error
                    if (((Global)getApplication()).addToCart(item)) {
                        //Bra feedback
                        //Kryssa i shoppinglistan
                        adapter.notifyDataSetChanged();

                        //Toast.makeText(getApplicationContext(), item.getName() + " (La till i vagnen)", Toast.LENGTH_SHORT).show();
                        showToast(item.getName(), true);
                        //Spela ljud
                        sp.play(soundIds[0], 1, 1, 1, 0, 1);
                    } else {
                        //dålig feedback
                        //Toast.makeText(getApplicationContext(), item.getName() + " (La inte till i vagnen)", Toast.LENGTH_SHORT).show();
                        showToast(item.getName(), false);
                        //Spela ljud
                        sp.play(soundIds[1], 1, 1, 1, 0, 1);
                        //Test animation
                        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                        v.startAnimation(shake);
                    }
                    break;
            }
            return true;
        }
    };

    private void setupItemViews() {
        for (int i = 0; i < 15; i++) {
            int resId = getResources().getIdentifier("itemView" + (i+1), "id", getPackageName());
            itemViews[i] = findViewById(resId);
            TextView tv = (TextView) itemViews[i].findViewById(R.id.cellTextView);
            tv.setText(items.get(i).getName() + " " + items.get(i).getPriceString());
            ImageView iv = (ImageView) itemViews[i].findViewById(R.id.cellImageView);
            iv.setOnTouchListener(itemListener);
            iv.setTag(items.get(i));
            iv.setMinimumHeight(100);
            iv.setContentDescription(items.get(i).getName());
            iv.setImageResource(items.get(i).getPicId());
            //Göm blanka
            if(items.get(i).getPrice().compareTo(BigDecimal.ZERO) == 0) {
                itemViews[i].setVisibility(INVISIBLE);
            }
        }
    }

    private void showToast(String name, boolean positive) {
        if(toast != null) {
            toast.cancel();
        }
        LayoutInflater inflater = getLayoutInflater();
        View layout;
        if(positive) {
            layout = inflater.inflate(R.layout.toast_positive,
                    (ViewGroup) findViewById(R.id.toast_layout_positive));
        } else {
            layout = inflater.inflate(R.layout.toast_negative,
                    (ViewGroup) findViewById(R.id.toast_layout_negative));
        }
        TextView text = (TextView) layout.findViewById(R.id.text2);
        text.setText(name);

        toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, -120, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

}
