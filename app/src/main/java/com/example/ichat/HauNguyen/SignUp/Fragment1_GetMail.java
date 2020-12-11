package com.example.ichat.HauNguyen.SignUp;

import android.annotation.SuppressLint;
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
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.example.ichat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.Objects;


public class Fragment1_GetMail extends Fragment {
    private static final String TAG = "Fragment_GetMail";
    FirebaseAuth mAuth;
    FirebaseUser user;

    private ProgressBar progressBar;
    private Button btnCheckEmail;
    private EditText txt_Email;

    @SuppressLint("ResourceAsColor")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment1__get_mail, container, false);
        //innit view
        innitView(view);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        btnCheckEmail.setText("Continue");
        txt_Email.addTextChangedListener(passwordWatcher);


        btnCheckEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                btnCheckEmail.setVisibility(View.INVISIBLE);
                final String email = txt_Email.getText().toString().trim();
                //validate
                if (email.isEmpty()) {
                    txt_Email.setError("Field can't be empty!");
                    Toast.makeText(getActivity(), "Enter email address", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    btnCheckEmail.setVisibility(View.VISIBLE);
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    //set error and focuss to email edittext
                    txt_Email.setError("Invalid Email");
                    txt_Email.setFocusable(true);
                    progressBar.setVisibility(View.INVISIBLE);
                    btnCheckEmail.setVisibility(View.VISIBLE);
                    return;
                } else {
                    //check email user
                    mAuth.fetchSignInMethodsForEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                    boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                                    if (isNewUser) { // là người dùng mới
                                        //xacThucEmail();
                                        progressBar.setVisibility(View.INVISIBLE);
                                        btnCheckEmail.setVisibility(View.VISIBLE);
                                        final Fragment2_GetGender_BirthDay fm2 = new Fragment2_GetGender_BirthDay();
                                        sendData(fm2, email);
                                        startFragment(fm2);
                                    } else { // người dùng đã tồn tại trong firebase
                                        progressBar.setVisibility(View.INVISIBLE);
                                        btnCheckEmail.setVisibility(View.VISIBLE);
                                        Toast.makeText(getActivity(), "This email already exists!", Toast.LENGTH_SHORT).show();
                                        Log.e("TAG", "Is Old User!");
                                        return;
                                    }

                                }
                            });
                }
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ViewCompat.setTransitionName(btnCheckEmail, "transition_button1");
    }

    private final TextWatcher passwordWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            txt_Email.setError(null);

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String getText = txt_Email.getText().toString();

            if (Patterns.EMAIL_ADDRESS.matcher(getText).matches()) {
                txt_Email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mail_outline_24px, 0, R.drawable.ic_done_24px, 0);
            } else {
                txt_Email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mail_outline_24px, 0, 0, 0);
            }
        }
    };


    private void startFragment(Fragment2_GetGender_BirthDay fm2) {
        //start fragment
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.anim_in_right, R.anim.anim_out_right, R.anim.anim_in_left, R.anim.anim_out_left)
                .addSharedElement(btnCheckEmail, "transition_button1")
                .replace(R.id.fr_layout_register, fm2, TAG)
                .addToBackStack(null)
                .commit();
    }

    private void sendData(Fragment2_GetGender_BirthDay fm2, String email) {
        //Send phoneNumber to Fragment4
        Bundle bundle = new Bundle();
        bundle.putString("EMAIL_FM1", email);
        fm2.setArguments(bundle);
    }


    private void innitView(View view) {
        progressBar = view.findViewById(R.id.progress_fm1);
        txt_Email = view.findViewById(R.id.txt_Email);
        btnCheckEmail = view.findViewById(R.id.btn_fm1);
    }

}