package com.example.ichat.HauNguyen.ForgotPassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.ichat.HauNguyen.Login.LoginActivity;
import com.example.ichat.R;

import java.util.Objects;

public class Fragment_UpdatePassword extends Fragment {
    private static final String TAG = "Fragment_UpdatePassword";
    private EditText txtNewPass, txtConfirmPass;
    private Button btnFinish;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_password, container, false);
        innitView(view);

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                Objects.requireNonNull(getActivity()).startActivity(intent);
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.anim_fadein, R.anim.anim_fadeout);
            }
        });

        return view;
    }

    private void innitView(View view) {
        txtNewPass = view.findViewById(R.id.txtNewPass_register);
        txtConfirmPass = view.findViewById(R.id.txtConfirmPass_register);
        btnFinish = view.findViewById(R.id.btnUpdatePassword_register);
    }
}