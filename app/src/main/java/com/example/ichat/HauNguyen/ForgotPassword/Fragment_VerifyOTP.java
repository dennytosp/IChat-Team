package com.example.ichat.HauNguyen.ForgotPassword;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chaos.view.PinView;
import com.example.ichat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken;
import static com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks;
import static com.google.firebase.auth.PhoneAuthProvider.getInstance;

public class Fragment_VerifyOTP extends Fragment {

    private static final String TAG = "Fragment_VerifyOTP";
    TextView textMobile;
    Button btnVerifyCode;
    FirebaseAuth mAuth;
    Bundle bundle;
    private PinView pinView;
    private String verificationId;
    private TextView resendOTP;
    private ProgressBar progressBar;
    private String phoneNumber;
    private ForceResendingToken mResendToken;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verify_otp_update, container, false);
        innitView(view);
        mAuth = FirebaseAuth.getInstance();
        bundle = getArguments();

        if (bundle != null) {
            phoneNumber = bundle.getString("PHONE_OTP");
            textMobile.setText(phoneNumber);
            sendVerificationCode(phoneNumber);
        }

        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendVerificationCode(phoneNumber, mResendToken);
            }
        });

        btnVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                btnVerifyCode.setVisibility(View.INVISIBLE);
                final String OTP = pinView.getText().toString();
                if (OTP.isEmpty() || OTP.length() < 6) {
                    pinView.setLineColor(Color.RED);
                    hindProgressAndShowButton();
                    return;
                } else if (OTP.length() == 6) {
                    pinView.setLineColor(getResources().getColor(R.color.colorPrimary));
                    verifyCode(pinView.getText().toString().trim());
                    hindProgressAndShowButton();
                }
            }
        });


        return view;
    }

    private void sendVerificationCode(String phoneNumbers) {

        getInstance().verifyPhoneNumber(
                phoneNumbers,
                60,
                TimeUnit.SECONDS,
                //(Activity) TaskExecutors.MAIN_THREAD,
                getActivity(),
                mCallBack
        );
    }

    private OnVerificationStateChangedCallbacks mCallBack =
            new OnVerificationStateChangedCallbacks() {

                @Override
                public void onCodeSent(@NonNull String s, @NonNull ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    verificationId = s;
                    mResendToken = forceResendingToken;
                    Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onCodeSent: " + verificationId);
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    hindProgressAndShowButton();
                    Log.d(TAG, "onVerificationCompleted: " + phoneAuthCredential.getSmsCode());
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Log.e(TAG, "onVerificationFailed: " + e.getMessage());
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        ForceResendingToken token) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)     // ForceResendingToken from callbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
        showProgressAndHideButton();

    }// [END resend_verification]

    private void verifyCode(String code) {
        if (verificationId != null) {
            showProgressAndHideButton();
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
            signInWithCredential(credential);
        }

    }


    private void signInWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Fragment_UpdatePassword fragmentUpdate = new Fragment_UpdatePassword();
                        if (task.isSuccessful()) {
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .setCustomAnimations(R.anim.anim_in_right, R.anim.anim_out_right, R.anim.anim_in_left, R.anim.anim_out_left)
                                    .replace(R.id.fr_layout_otp, fragmentUpdate, TAG)
                                    .addToBackStack(null)
                                    .commit();
                            hindProgressAndShowButton();
                        } else {

                            Log.e(TAG, "onComplete: " + task.getException().getMessage());
                            Toast.makeText(getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            hindProgressAndShowButton();
                        }
                    }
                });
    }

    private void hindProgressAndShowButton() {
        progressBar.setVisibility(View.INVISIBLE);
        btnVerifyCode.setVisibility(View.VISIBLE);
    }

    private void showProgressAndHideButton() {
        progressBar.setVisibility(View.VISIBLE);
        btnVerifyCode.setVisibility(View.INVISIBLE);
    }

    private void innitView(View view) {
        //Hooks
        btnVerifyCode = view.findViewById(R.id.btnVerifyOtpCode);
        textMobile = view.findViewById(R.id.textMobile);
        resendOTP = view.findViewById(R.id.resend_otp);
        pinView = view.findViewById(R.id.pinView);
        progressBar = view.findViewById(R.id.progressBar_verify);

    }
}