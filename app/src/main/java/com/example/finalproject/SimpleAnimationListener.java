package com.example.finalproject;

import android.content.res.Resources;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class SimpleAnimationListener extends AppCompatActivity implements Animation.AnimationListener {
    int colorFrom = R.color.red;
    int colorTo = R.color.blue;


    //final ImageView iv = (ImageView) R.id.brick;
    //final ImageView iv = (ImageView) findViewById(R.id.brick);
      //  iv.setColorFilter(null);
    //final int newColor = res.getColor(R.color.tan);
      //  iv.setColorFilter(ContextCompat.getColor(this,R.color.blue));

    @Override
    public void onAnimationStart(Animation animation) {

        //PorterDuff.Mode.LIGHTEN
        //image.setColorFilter(newColor, PorterDuff.Mode.LIGHTEN);

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
