package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
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
    private BrickCounter brickCounter = new BrickCounter();
    private Random random;
    private TextView tvCps;

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

        tvPoints = findViewById(R.id.tvPoints);
        tvCps = findViewById(R.id.tvCps);
        random = new Random();
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
                points++;
                tvPoints.setText(Integer.toString(points));
                showToast(R.string.clicked);
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
        points++;
        tvPoints.setText(Integer.toString(points));
        showToast(R.string.clicked);
    }

    private void showToast(int stringID) {
        final Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER|Gravity.LEFT,random.nextInt(600)+100,random.nextInt(600)-300);
        toast.setDuration(toast.LENGTH_SHORT);
        TextView textView = new TextView(this);
        textView.setText(stringID);
        textView.setTextSize(40f);
        textView.setTextColor(Color.BLACK);
        toast.setView(textView);
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