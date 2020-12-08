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

    private String[] Names = {"Brick Stackers", "Better Tools", "Elite Stackers", "Cement Mixers", "Automation", "Advanced Logistics", "Futuristic Robots", "Alternate Timelines"};
    private String[] Description =  {"+100 bricks per second", "+20 bricks per click", "+1000 bricks per second", "+2000 bricks per click", "+10000 bricks per second", "+20000 bricks per click", "+1000000 bricks per second", "+2000000 bricks per click"};
    private int[] multipliers = {1,1,1,1,1,1,1,1};

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
        for(int i = 0; i < 8; i++)
        {
            if(multipliers[i] == 0)
            {
                multipliers[i] = 1;
            }
        }

        if(pointIncreaser == 0)
        {
            pointIncreaser = 1;
        }


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
            save();
            showShopFragments();
        }
    }

    private void showBrickFragments() {
        ViewGroup container = findViewById(R.id.container);
        container.removeAllViews();
        container.addView(getLayoutInflater().inflate(R.layout.activity_main,null));
    }

    private void brickClick() {
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
        if(cps < 100 && cps > 0)
        {
            points += 1;
        }
        points += cps/100;
        tvPoints.setText(Integer.toString(points));
        tvCps.setText(Integer.toString(cps) + " bps");
    }

    private void save() {
        SharedPreferences preferences = getSharedPreferences("GAME", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("bps", cps);
        editor.putInt("bricks", points);
        editor.putInt("mult0", multipliers[0]);
        editor.putInt("mult1", multipliers[1]);
        editor.putInt("mult2", multipliers[2]);
        editor.putInt("mult3", multipliers[3]);
        editor.putInt("mult4", multipliers[4]);
        editor.putInt("mult5", multipliers[5]);
        editor.putInt("mult6", multipliers[6]);
        editor.putInt("mult7", multipliers[7]);
        editor.putInt("increaser",pointIncreaser);
        editor.apply();
    }

    private void open() {
        SharedPreferences preferences = getSharedPreferences("GAME",0);
        cps = preferences.getInt("bps", 0);
        points = preferences.getInt("bricks",0);
        pointIncreaser = preferences.getInt("increaser",1);
        multipliers[0] = preferences.getInt("mult0",1);
        multipliers[1] = preferences.getInt("mult1",1);
        multipliers[2] = preferences.getInt("mult2",1);
        multipliers[3] = preferences.getInt("mult3",1);
        multipliers[4] = preferences.getInt("mult4",1);
        multipliers[5] = preferences.getInt("mult5",1);
        multipliers[6] = preferences.getInt("mult6",1);
        multipliers[7] = preferences.getInt("mult7",1);

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
            return i;
        }

        @Override
        public View getView(final int position, View cv, ViewGroup viewGroup) {
            final View convertView = getLayoutInflater().inflate(R.layout.itemlistview, null);
            ((TextView)convertView.findViewById(R.id.tvName)).setText(Names[position]);
            ((TextView)convertView.findViewById(R.id.tvDescription)).setText(Description[position]);
            ((TextView)convertView.findViewById(R.id.tvCost)).setText("Cost: " + (int)Math.pow(position + 2, multipliers[position])+(int)Math.pow(10,position));
            /*multipliers[0]=1;
            multipliers[1]=1;
            multipliers[2]=1;
            multipliers[3]=1;
            multipliers[4]=1;
            multipliers[5]=1;
            multipliers[6]=1;
            multipliers[7]=1;*/




            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getCount() == 8) {
                        if(points >= (int)Math.pow(position + 2, multipliers[position])+(int)Math.pow(10,position)) {
                            switch (position)
                            {
                                case 0: // code to be executed if n = 1;
                                    updateCps(100);
                                    updatePoints((int)Math.pow(position + 2, multipliers[position])+(int)Math.pow(10,position));
                                    break;
                                case 1: // code to be executed if n = 1;
                                    pointIncreaser += 20;
                                    updatePoints((int)Math.pow(position + 2, multipliers[position])+(int)Math.pow(10,position));
                                    break;
                                case 2: // code to be executed if n = 1;
                                    updateCps(1000);
                                    updatePoints((int)Math.pow(position + 2, multipliers[position])+(int)Math.pow(10,position));
                                    break;
                                case 3: // code to be executed if n = 1;
                                    pointIncreaser += 2000;
                                    updatePoints((int)Math.pow(position + 2, multipliers[position])+(int)Math.pow(10,position));
                                    break;
                                case 4: // code to be executed if n = 1;
                                    updateCps(10000);
                                    updatePoints((int)Math.pow(position + 2, multipliers[position])+(int)Math.pow(10,position));
                                    break;
                                case 5: // code to be executed if n = 1;
                                    pointIncreaser += 20000;
                                    updatePoints((int)Math.pow(position + 2, multipliers[position])+(int)Math.pow(10,position));
                                    break;
                                case 6: // code to be executed if n = 1;
                                    updateCps(1000000);
                                    updatePoints((int)Math.pow(position + 2, multipliers[position])+(int)Math.pow(10,position));
                                    break;
                                case 7: // code to be executed if n = 1;
                                    pointIncreaser += 2000000;
                                    updatePoints((int)Math.pow(position + 2, multipliers[position])+(int)Math.pow(10,position));
                                    break;
                                default: // code to be executed if n doesn't match any case
                            }
                            multipliers[position]++;
                            ((TextView)convertView.findViewById(R.id.tvCost)).setText("Cost: " + (int)Math.pow(position + 2, multipliers[position])+(int)Math.pow(10,position));
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