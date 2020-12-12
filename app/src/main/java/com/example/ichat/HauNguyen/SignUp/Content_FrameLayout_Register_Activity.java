package com.example.ichat.HauNguyen.SignUp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ichat.R;


public class Content_FrameLayout_Register_Activity extends AppCompatActivity {
    private static final String TAG = "Content_Layout_Controller";
    private ImageView img1;
    private Button tvSignIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_fragment_register);
        innitView();

        //start first fragment (fragment1)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fr_layout_register, new Fragment1_GetMail()).commit();
        }

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Content_FrameLayout_Register_Activity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }


    private void innitView() {
        img1 = findViewById(R.id.img1);
        tvSignIn = findViewById(R.id.tvSignIn);


    }
}
