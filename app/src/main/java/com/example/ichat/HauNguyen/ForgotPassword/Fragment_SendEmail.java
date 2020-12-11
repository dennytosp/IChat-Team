package com.example.ichat.HauNguyen.ForgotPassword;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ichat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;

public class Fragment_SendEmail extends Fragment {
    private static final String TAG = "Fragment_SendOTP";
    private EditText edtEmail;
    private Button btn_resetPass, btnCancel;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    FirebaseUser user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_send_email, container, false);
        innitView(view);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        edtEmail.addTextChangedListener(passwordWatcher);

        btn_resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                btn_resetPass.setVisibility(View.INVISIBLE);
                //resetPasswordWithEmail(edtEmail.getText().toString());
                sendPasswordResetEmail(edtEmail.getText().toString().trim());
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
    

    private void sendPasswordResetEmail(String email){
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getActivity(), "Email sent.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    btn_resetPass.setVisibility(View.VISIBLE);
                    getActivity().finish();
                    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }else {
                    Toast.makeText(getActivity(), "Email has not been sent.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    btn_resetPass.setVisibility(View.VISIBLE);
                }

            }
        });
    }


private final TextWatcher passwordWatcher = new TextWatcher() {
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        edtEmail.setError(null);
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String getText = edtEmail.getText().toString();

        if (Patterns.EMAIL_ADDRESS.matcher(getText).matches()) {
            edtEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mail_outline_24px, 0, R.drawable.ic_done_24px, 0);
        } else {
            edtEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mail_outline_24px, 0, 0, 0);
        }
    }
};

    private void innitView(View v) {
        //Hooks
        edtEmail = v.findViewById(R.id.txt_Email_resetpass);
        btn_resetPass = v.findViewById(R.id.btn_resetPass);
        btnCancel = v.findViewById(R.id.btnCancel);
        progressBar = v.findViewById(R.id.progress_reset_password);
    }
}