package com.example.ichat.HauNguyen.ForgotPassword;

import android.os.Bundle;
import android.transition.Fade;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ichat.R;


public class Content_ForgotPassWord_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content__o_t_p_);
        animationLogo();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fr_layout_otp, new Fragment_SendEmail()).commit();
        }
    }

    private void animationLogo(){
        Fade fade = new Fade();
        View decor = getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
        fade.excludeTarget(android.R.id.statusBarBackground, true);
        fade.excludeTarget(android.R.id.navigationBarBackground, true);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}