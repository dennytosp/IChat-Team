package com.example.ichat.HauNguyen.SignUp;

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

import com.example.ichat.R;


public class Fragment3_GetUsername_FullName extends Fragment {
    private static final String TAG = "Fragment3_GetUsername_FullName";
    Content_FrameLayout_Register_Activity content_Register;
    String email, birthday, gender, username, fullName;
    private Button btnFm4;
    private ProgressBar progress_bar_fm4;
    private EditText edt_username, edt_fullname;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment3_username_fullname, container, false);
        //innit view
        innitView(view);
        Bundle bundle = getArguments();
        if (bundle != null) {
            email = bundle.getString("EMAIL_FM2");
            gender = bundle.getString("GENDER_FM2");
            birthday = bundle.getString("BIRTHDAY_FM2");
            Toast.makeText(getActivity(), email + "\n" + gender + "\n" + birthday, Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onCreateView: " + bundle);
        }

        btnFm4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    showProgress();
                    if (!validateUserName() || !validateFullname()) {
                        hideProgress();
                        return;
                    } else {
                        username = edt_username.getText().toString();
                        fullName = edt_fullname.getText().toString();
                        Fragment4_GetPassword fm4 = new Fragment4_GetPassword();
                        sendData(fm4, email, birthday, gender);
                        startFragment(fm4);
                    }
                } catch (Exception e) {
                    hideProgress();
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onClick: " + e.getMessage());
                }

            }
        });

        return view;
    }


    private void startFragment(Fragment4_GetPassword fm3) {
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.anim_in_right, R.anim.anim_out_right, R.anim.anim_in_left, R.anim.anim_out_left)
                .replace(R.id.fr_layout_register, fm3, TAG)
                .addToBackStack(null)
                .commit();
    }

    private void sendData(Fragment4_GetPassword fragment3, String email, String birthday, String gender) {
        //Send phoneNumber to Fragment4
        Bundle bundle = new Bundle();
        bundle.putString("EMAIL_FM3", email);
        bundle.putString("GENDER_FM3", gender);
        bundle.putString("BIRTHDAY_FM3", birthday);
        bundle.putString("USER_NAME_FM3", edt_username.getText().toString());
        bundle.putString("Full_name_FM3", edt_fullname.getText().toString());
        fragment3.setArguments(bundle);
    }


    private boolean validateUserName() {
        String val = edt_username.getText().toString().trim();
        String checkSpace = "\\A\\w{1,20}\\z";
        if (val.isEmpty()) {
            edt_username.setError("Field can't be empty!");
            return false;
        } else if (val.length() > 20) {
            edt_username.setError("Username is too large!");
            return false;
        } else if (!val.matches(checkSpace)) {
            edt_username.setError("No white spaces are allowed!");
            return false;
        } else {
            edt_username.setError(null);
            return true;
        }
    }

    private boolean validateFullname() {
        String val = edt_fullname.getText().toString().trim();
        if (val.isEmpty()) {
            edt_fullname.setError("Field can't be empty!");
            return false;
        } else {
            edt_fullname.setError(null);
            return true;
        }
    }

    private void showProgress() {
        progress_bar_fm4.setVisibility(View.VISIBLE);
        btnFm4.setVisibility(View.INVISIBLE);
    }

    private void hideProgress() {
        progress_bar_fm4.setVisibility(View.INVISIBLE);
        btnFm4.setVisibility(View.VISIBLE);
    }

    private void innitView(View view) {
        edt_username = view.findViewById(R.id.txt_username);
        edt_fullname = view.findViewById(R.id.txt_fullName);
        btnFm4 = view.findViewById(R.id.btnFm4);
        progress_bar_fm4 = view.findViewById(R.id.progress_bar_fm4);
    }

}