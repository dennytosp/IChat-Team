package com.example.ichat.HauNguyen.SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.example.ichat.HauNguyen.DAO.UserDAO;
import com.example.ichat.HauNguyen.Login.LoginActivity;
import com.example.ichat.HauNguyen.Model.User_Profile;
import com.example.ichat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fragment4_GetPassword extends Fragment {
    private static final String TAG = "Fragment4_GetPassword";
    public EditText edtNewPass, edtConfirmPass;
    String email, gender, birthday, username, fullName, password;
    //Firebase
    String userID;
    private Button btnFm5;
    private ProgressBar progressBar_fm5;
    private FirebaseAuth mAuth;
    private FirebaseFirestore fStore;
    private DatabaseReference mDatabase;
    UserDAO userDAO;
    User_Profile user_profile;

    public static boolean isValidPassword(String password) {
        //begin
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
        //end
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment4_password, container, false);
        innitView(view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            email = bundle.getString("EMAIL_FM3");
            gender = bundle.getString("GENDER_FM3");
            birthday = bundle.getString("BIRTHDAY_FM3");
            username = bundle.getString("USER_NAME_FM3");
            fullName = bundle.getString("Full_name_FM3");
            Log.i(TAG, "Show DATA ==/ " + bundle);
            Toast.makeText(getActivity(),
                    email + "\n" +
                            gender + "\n" +
                            birthday + "\n" +
                            username + "\n" +
                            fullName, Toast.LENGTH_SHORT).show();
        }

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //loading dialog


        btnFm5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showProgress();
                    String newPass = edtNewPass.getText().toString().trim();
                    String confirmPass = edtConfirmPass.getText().toString().trim();
                    if (newPass.length() < 6 || !isValidPassword(newPass) || !newPass.equals(confirmPass)) {
                        Toast.makeText(getActivity(), "Invalid or incorrect password, please try again!", Toast.LENGTH_SHORT).show();
                        hideProgress();
                        return;
                    } else {
                        password = confirmPass;
                        Toast.makeText(getActivity(), email + "\n" + gender + "\n" + birthday + "\n" + username + "\n" + fullName + "\n" + password, Toast.LENGTH_SHORT).show();
                        createUserAuthentication();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "It cannot be done!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error ==/ ");
                }
            }

        });

        return view;
    }

    private void createUserAuthentication() {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            mAuth.signOut();
                            user.sendEmailVerification()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(getActivity(), "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                            sendData();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "Email not sent.", Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                }
                            });
                            Toast.makeText(getActivity(), "User Created.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getActivity(), LoginActivity.class));
                        } else {
                            hideProgress();
                            Log.e(TAG, "reload", task.getException());
                            Toast.makeText(getActivity(), "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void addDataFireStore(String fullName, String email, String phone, String username, String birthday, String gender, String password) {
        userID = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);
        Map<String, Object> user = new HashMap<>();
        user.put("fName", fullName);
        user.put("email", email);
        user.put("phone", phone);
        user.put("usn", username);
        user.put("birthday", birthday);
        user.put("gender", gender);
        user.put("password", password);
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: user Profile is created for " + userID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });
    }


    private void sendData() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Emails_fm5", email);
        bundle.putString("password_fm5", password);
        bundle.putString("username_fm5", username);
        bundle.putString("fullname_fm5", fullName);
        bundle.putString("birthday_fm5", birthday);
        bundle.putString("gender_fm5", gender);
        intent.putExtras(bundle);
        Log.i(TAG, "sendData: "
                + email + "\n"
                + password + "\n"
                + gender + "\n"
                + birthday + "\n"
                + username + "\n"
                + fullName);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    private void showProgress() {
        progressBar_fm5.setVisibility(View.VISIBLE);
        btnFm5.setVisibility(View.INVISIBLE);
    }

    private void hideProgress() {
        progressBar_fm5.setVisibility(View.INVISIBLE);
        btnFm5.setVisibility(View.VISIBLE);
    }

    private void innitView(View view) {
        btnFm5 = view.findViewById(R.id.btnFm5);
        edtNewPass = view.findViewById(R.id.txtNewPass);
        edtConfirmPass = view.findViewById(R.id.txtConfirmPass);
        progressBar_fm5 = view.findViewById(R.id.progressBar_fm5);
    }

}