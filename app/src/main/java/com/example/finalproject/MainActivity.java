package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvPoints;
    private int points;
    private int cps;
    private int pointIncreaser = 1;
    private BrickCounter brickCounter = new BrickCounter();
    private Random random;
    private Random randocol;
    private TextView tvCps;
    //private ImageView b;

    private String[] Names = {"Clicker"};
    private String[] Description =  {"+100 bricks per second"};

    private SensorManager sm;
    private float acelVal;
    private float acelLast;
    private float shake;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(sensorListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;

        //b = (ImageView)findViewById(R.id.brick);

        tvPoints = findViewById(R.id.tvPoints);
        tvCps = findViewById(R.id.tvCps);
        random = new Random();
        randocol = new Random();
        open();

    }

    private final SensorEventListener sensorListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            acelLast = acelVal;
            acelVal = (float) Math.sqrt((double) (x*x + y*y + z*z));

            if(acelVal > acelLast + 0.01 || acelVal < acelLast - 0.01)
            {
                brickClick();
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            showBrickFragments();
            tvPoints = findViewById(R.id.tvPoints);
            tvCps = findViewById(R.id.tvCps);
            random = new Random();
            open();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onPause() {
        super.onPause();
        save();
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.brick)
        {



            Animation a = AnimationUtils.loadAnimation(this, R.anim.brick_animation);
            a.setAnimationListener(new SimpleAnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation) {
                    brickClick();
                }
            });
            v.startAnimation(a);

        } else if(v.getId() == R.id.btnShop) {
            showShopFragments();
            save();
        }
    }

    private void showBrickFragments() {
        ViewGroup container = findViewById(R.id.container);
        container.removeAllViews();
        container.addView(getLayoutInflater().inflate(R.layout.activity_main,null));
    }

    private void brickClick() {
        //b.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);
        //Drawable myDrawable = ContextCompat.getDrawable(this,R.drawable.brick);
        //myDrawable.setTint(ContextCompat.getColor(this, R.color.colorPrimary));
        ImageView changeCol = (ImageView) findViewById(R.id.brick);
        int a = randocol.nextInt(12);
        int col = 0;

        switch (a)
        {
            case 0: // code to be executed if n = 1;
                break;
            case 1: // code to be executed if n = 2;
                col = R.color.lightblue;
                break;
            case 2: // code to be executed if n = 1;
                col = R.color.red;
                break;
            case 3: // code to be executed if n = 2;
                col = R.color.orange;
                break;
            case 4: // code to be executed if n = 1;
                col = R.color.yellow;
                break;
            case 5: // code to be executed if n = 2;
                col = R.color.green;
                break;
            case 6: // code to be executed if n = 1;
                col = R.color.darkgreen;
                break;
            case 7: // code to be executed if n = 2;
                col = R.color.blue;
                break;
            case 8: // code to be executed if n = 1;
                col = R.color.purple;
                break;
            case 9: // code to be executed if n = 2;
                col = R.color.black;
                break;
            case 10: // code to be executed if n = 1;
                col = R.color.brown;
                break;
            case 11: // code to be executed if n = 2;
                col = R.color.tan;
                break;
            case 12: // code to be executed if n = 2;
                col = R.color.white;
                break;
            default: // code to be executed if n doesn't match any case
        }

        changeCol.setColorFilter(null);
        if(a != 0)
        {
            changeCol.setColorFilter(col,  PorterDuff.Mode.SRC_ATOP);
            changeCol.setImageResource(R.drawable.brickb);
        }



        points += pointIncreaser;
        tvPoints.setText(Integer.toString(points));
        showToast("+" + pointIncreaser);
    }

    private void showToast(String stringID) {
        int x = random.nextInt(600)+100;
        int y = random.nextInt(600)-300;




        final Toast toast = new Toast(this);

        LinearLayout toastLayout = new LinearLayout(this);
        toastLayout.setOrientation(LinearLayout.VERTICAL);

        ImageView image = new ImageView(this);
        image.setImageResource(R.drawable.brickbrickbri);
        //image.setColorFilter(Color.RED, PorterDuff.Mode.LIGHTEN);




        toast.setGravity(Gravity.CENTER|Gravity.LEFT,x,y);
        toast.setDuration(toast.LENGTH_SHORT);
        TextView textView = new TextView(this);
        textView.setText(stringID);
        textView.setTextSize(40f);
        textView.setTextColor(Color.BLACK);
        toastLayout.addView(image);
        toastLayout.addView(textView);

        toast.setView(toastLayout);
        CountDownTimer toastCountDown;
        toastCountDown = new CountDownTimer(500,100) {
            @Override
            public void onTick(long millisUntilFinished) {
                toast.show();

            }

            @Override
            public void onFinish() {
                toast.cancel();

            }
        };
        toast.show();
        toastCountDown.start();
    }

    private void update() {
        points += cps/100;
        tvPoints.setText(Integer.toString(points));
        tvCps.setText(Integer.toString(cps) + " bps");
    }

    private void save() {
        SharedPreferences preferences = getSharedPreferences("GAME", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("bps", cps);
        editor.putInt("bricks", points);
        editor.commit();
    }

    private void open() {
        SharedPreferences preferences = getSharedPreferences("GAME",0);
        cps = preferences.getInt("bps",0);
        points = preferences.getInt("bricks",0);
    }

    private void showShopFragments() {
        ViewGroup container = findViewById(R.id.container);
        ShopAdapter shopAdapter = new ShopAdapter();
        container.removeAllViews();
        container.addView(getLayoutInflater().inflate(R.layout.shopactivity, null));
        ((ListView)findViewById(R.id.listShop)).setAdapter(shopAdapter);
    }

    private void updateCps(int i)
    {
        cps += i;
    }

    private void updatePoints(int i)
    {
        points -= i;
    }

    private class BrickCounter {
        private Timer timer;

        public BrickCounter() {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            update();
                        }
                    });
                }
            }, 1000, 10);
        }
    }

    public class ShopAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Names.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            convertView = getLayoutInflater().inflate(R.layout.itemlistview, null);
            ((TextView)convertView.findViewById(R.id.tvName)).setText(Names[position]);
            ((TextView)convertView.findViewById(R.id.tvDescription)).setText(Description[position]);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getCount() == 1) {
                        if(points >= 100) {
                            updateCps(100);
                            updatePoints(100);
                            save();
                        } else {
                            (new AlertDialog.Builder(MainActivity.this)).setMessage("Do not have enough bricks")
                                    .show();
                        }
                    }
                }
            });

            return convertView;
        }
    }

}