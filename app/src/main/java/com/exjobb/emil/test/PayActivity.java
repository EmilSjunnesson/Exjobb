package com.exjobb.emil.test;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

import static android.view.View.OnDragListener;
import static android.view.View.OnTouchListener;


public class PayActivity extends Activity {

    View[] moneyViews = new View[6];
    TextView moneyInText;
    BigDecimal moneyInSquare = BigDecimal.ZERO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        ((Global)getApplication()).calculateSum();
        TextView toPayText = (TextView) findViewById(R.id.toPayView);
        toPayText.setText("Betala exakt: " + ((Global)getApplication()).getSumString());
        moneyInText = (TextView) findViewById(R.id.moneyInView);
        updateMoneyInText();
        findViewById(R.id.dropView).setOnDragListener(dropListener);
        setupMoneyViews();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pay, menu);
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

    OnDragListener dropListener = new OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int dragEvent = event.getAction();
            //HÄMTAS RÄTT IMAGEVIEW
            ImageView iv = (ImageView) event.getLocalState();
            RelativeLayout rl = (RelativeLayout) v;
            Money tag = (Money) iv.getTag();

            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if (tag.getExists()) {
                        iv.setVisibility(View.INVISIBLE);
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.i("Drag Event", "Entered");
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.i("Drag Event", "Exited");
                    break;
                case DragEvent.ACTION_DROP:
                    drawMoney(iv, rl, event.getX(), event.getY());

                    if(tag.getExists()) {
                        //Ta bort gammal view
                        rl.removeView(iv);
                    } else {
                        //Lägg till pengar
                        moneyInSquare = moneyInSquare.add(tag.getValue());
                        updateMoneyInText();
                        //Kolla om pengarna i rutan = summan
                        if(checkMoney()) {
                            //To win
                            showWinDialog();
                        }
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if (tag.getExists() && !event.getResult()) {
                        //Ta bort pengar
                        moneyInSquare = moneyInSquare.subtract(tag.getValue());
                        updateMoneyInText();
                        rl.removeView(iv);
                    }
                    break;
            }
            return true;
        }
    };

    OnTouchListener moneyListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int motionEvent = event.getAction();
            switch (motionEvent) {
                case MotionEvent.ACTION_DOWN:
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder dragShadow = new View.DragShadowBuilder(v);
                    v.startDrag(data, dragShadow, v, 0);
                    break;
            }
            return false;
        }
    };

    private void drawMoney(ImageView iv, RelativeLayout rl, float x, float y) {
        //RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int maxWidth = rl.getWidth();
        int maxHeight = rl.getHeight();
        Money tag = (Money) iv.getTag();
        int height = tag.getHeight();
        int width = tag.getWidth();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(height, width);
        // x och y - hälften av höjden och bredden
        //och om det blir mindre än noll gör det till noll
        params.leftMargin = (int) x - (width/2);
        params.leftMargin = (params.leftMargin < 0) ? 0 : params.leftMargin;
        params.leftMargin = (params.leftMargin > maxWidth - (width * 2)) ? maxWidth - (width * 2) : params.leftMargin;

        params.topMargin = (int) y - (height/2);
        params.topMargin = (params.topMargin < 0) ? 0 : params.topMargin;
        params.topMargin = (params.topMargin > maxHeight - height) ? maxHeight - height : params.topMargin;

        ImageView newView = new ImageView(getApplicationContext());
        newView.setTag(new Money(tag));
        newView.setImageDrawable(iv.getDrawable());
        newView.setOnTouchListener(moneyListener);
        rl.addView(newView, params);
    }

    private boolean checkMoney() {
        BigDecimal sum = ((Global)getApplication()).getSum().setScale(0, RoundingMode.HALF_UP);
        if (moneyInSquare.compareTo(sum) == 0) {
            return true;
        } else {
            return false;
        }
    }

    private void updateMoneyInText() {
        moneyInText.setText("Pengar i rutan: " + NumberFormat.getCurrencyInstance().format(moneyInSquare));
    }

    private void setupMoneyViews() {
        moneyViews[0] = findViewById(R.id.oneView);
        moneyViews[0].setTag(new Money(75, 75, 1));
        moneyViews[1] = findViewById(R.id.fiveView);
        moneyViews[1].setTag(new Money(81, 81, 5));
        moneyViews[2] = findViewById(R.id.tenView);
        moneyViews[2].setTag(new Money(58, 58, 10));
        moneyViews[3] = findViewById(R.id.twentyView);
        moneyViews[3].setTag(new Money(160, 90, 20));
        moneyViews[4] = findViewById(R.id.fiftyView);
        moneyViews[4].setTag(new Money(160,106, 50));
        moneyViews[5] = findViewById(R.id.hundredView);
        moneyViews[5].setTag(new Money(180, 95, 100));
        for (int i = 0; i < moneyViews.length; i++) {
            moneyViews[i].setOnTouchListener(moneyListener);
        }
    }

    private void showWinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Grattis, du klarade av spelet!")
               .setCancelable(false);
        builder.setPositiveButton("Till huvudmenyn", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finishAffinity();
            }
        });
        builder.setNegativeButton("Avsluta", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
                System.exit(0);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView messageView = (TextView)dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
        messageView.setTextSize(30);
    }

    private class Money {

        private int width;
        private int height;
        private BigDecimal value;
        private boolean exists;

        public Money(int height, int width, double value) {
            this.width = dpToPx(width);
            this.height = dpToPx(height);
            this.value = BigDecimal.valueOf(value);
            exists = false;
        }

        public Money(Money m) {
            this.width = m.getWidth();
            this.height = m.getHeight();
            this.value = m.getValue();
            exists = true;
        }

        public BigDecimal getValue() {
            return value;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public boolean getExists() {
            return exists;
        }

        private int dpToPx(int dp)
        {
            float density = getApplicationContext().getResources().getDisplayMetrics().density;
            return Math.round((float)dp * density);
        }
    }
}
