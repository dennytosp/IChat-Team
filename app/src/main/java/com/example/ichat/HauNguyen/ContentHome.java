package com.example.ichat.HauNguyen;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ichat.R;


public class ContentHome extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_home);
        //start first fragment (fragment1)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fr_layout_home, new FragmentHome()).commit();

        }
    }
}