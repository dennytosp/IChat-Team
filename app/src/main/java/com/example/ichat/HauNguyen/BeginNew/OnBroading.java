package com.example.ichat.HauNguyen.BeginNew;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.ichat.HauNguyen.Adapter.SliderAdapter;
import com.example.ichat.HauNguyen.Login.LoginActivity;
import com.example.ichat.R;
import com.rd.PageIndicatorView;

public class OnBroading extends AppCompatActivity {
    ViewPager viewPager;
//    LinearLayout dotsLayout;
    SliderAdapter sliderAdapter;
    TextView[] dots;
    Button btn_getStarted;
    Animation animation;
    int currentPosition;
    SharedPreferences onBroadingScreen;
    PageIndicatorView pageIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_broading);
        innitView();

        //Adapter
        sliderAdapter = new SliderAdapter(this);

        //set Adapter
        viewPager.setAdapter(sliderAdapter);

        //addDots(0);
//        addDotsIndicatorView(0);
        viewPager.addOnPageChangeListener(changeListener);

    }

//    private void addDots(int position) {
//        dots = new TextView[4];
//        dotsLayout.removeAllViews();
//        for (int i = 0; i < dots.length; i++) {
//            dots[i] = new TextView(this);
//            dots[i].setText(Html.fromHtml("&#8226;"));
//            dots[i].setTextColor(getResources().getColor(R.color.colorDivider));
//            dots[i].setTextSize(35);
//            dotsLayout.addView(dots[i]);
//        }
//
//        if (dots.length > 0) {
//            dots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
//        }
//    }



    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
//            addDots(position);
//            addDotsIndicatorView(position);
            currentPosition = position;

            if (position == 0) {
                btn_getStarted.setVisibility(View.INVISIBLE);
                pageIndicatorView.setVisibility(View.VISIBLE);
            } else if (position == 1) {
                btn_getStarted.setVisibility(View.INVISIBLE);
                pageIndicatorView.setVisibility(View.VISIBLE);
            } else if (position == 2) {
                btn_getStarted.setVisibility(View.INVISIBLE);
                pageIndicatorView.setVisibility(View.VISIBLE);
            } else {
                animation = AnimationUtils.loadAnimation(OnBroading.this, R.anim.bottom_anim);
                btn_getStarted.setAnimation(animation);
                btn_getStarted.setVisibility(View.VISIBLE);
                pageIndicatorView.setVisibility(View.GONE);
            }
            pageIndicatorView.setSelection(currentPosition);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };


    public void started(View view) {
        savePrefsData();
        Intent intent = new Intent(OnBroading.this, LoginActivity.class);
        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(
                getApplicationContext(), android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
        startActivity(intent, bundle);
        finish();
    }



    private void savePrefsData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isOnlyOne_OnBroading", true);
        editor.commit();

    }

    private void innitView(){
        //Hooks
        viewPager = findViewById(R.id.slider);
//        dotsLayout = findViewById(R.id.dotsLayout);
        btn_getStarted = findViewById(R.id.btn_get_started);
        pageIndicatorView = findViewById(R.id.pageIndicatorView);
    }

    public void skip(View view) {
        viewPager.setCurrentItem(currentPosition + 3);
        animation = AnimationUtils.loadAnimation(OnBroading.this, R.anim.bottom_anim);
        btn_getStarted.setAnimation(animation);
        btn_getStarted.setVisibility(View.VISIBLE);
        pageIndicatorView.setVisibility(View.GONE);
        pageIndicatorView.setSelection(currentPosition);
    }

    public void next(View view) {
        viewPager.setCurrentItem(currentPosition + 1);

    }
}