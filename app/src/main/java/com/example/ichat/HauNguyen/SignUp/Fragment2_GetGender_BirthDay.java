package com.example.ichat.HauNguyen.SignUp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ichat.R;

import java.util.Calendar;

public class Fragment2_GetGender_BirthDay extends Fragment {
    private static final String TAG = "Fragment2_GetGender_BirthDay";
    private RadioGroup radioGroup;
    private RadioButton male, female, other;
    private DatePicker datePicker;
    private Button btnFm3;
    private ProgressBar progress_bar_fm3;
    String email, gender, birthday;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_fragment2_gender_brthday, container, false);
        innitView(view);
        Bundle bundle = getArguments();
        if (bundle != null){
            Log.i(TAG, "onCreateView: " + bundle);
            email = bundle.getString("EMAIL_FM1");
            Toast.makeText(getActivity(), email, Toast.LENGTH_SHORT).show();
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                doGender();
            }
        });


        btnFm3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                if (!validateAge() | !validateGender()) {
                    hideProgress();
                    return;
                } else {
                    try {
                        int day = datePicker.getDayOfMonth();
                        int month = datePicker.getMonth() + 1;
                        int year = datePicker.getYear();
                        //get birthday
                        birthday = day + "/" + month + "/" + year;
                        //get data from class Fragment_AuthenticationPhone
                        gender = doGender();
                        Fragment3_GetUsername_FullName fm3 = new Fragment3_GetUsername_FullName();
                        sendData(fm3, email, birthday, gender);
                        startFragment(fm3);
                    } catch (Exception e) {
                        hideProgress();
                        Log.e(TAG, "Error ==/ " + e.getMessage());
                        Toast.makeText(getActivity(), "Cannot be completed at this time!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return view;
    }

    public void hideProgress(){
        progress_bar_fm3.setVisibility(View.GONE);
        btnFm3.setVisibility(View.VISIBLE);
    }

    public void showProgress(){
        progress_bar_fm3.setVisibility(View.VISIBLE);
        btnFm3.setVisibility(View.GONE);
    }

    private void startFragment(Fragment3_GetUsername_FullName fm3){
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.anim_in_right, R.anim.anim_out_right, R.anim.anim_in_left, R.anim.anim_out_left)
                .replace(R.id.fr_layout_register, fm3, TAG)
                .addToBackStack(null)
                .commit();
    }

    private void sendData(Fragment3_GetUsername_FullName fragment3, String email, String age, String gender) {
        //Send phoneNumber to Fragment4
        Bundle bundle = new Bundle();
        bundle.putString("EMAIL_FM2", email);
        bundle.putString("GENDER_FM2", gender);
        bundle.putString("BIRTHDAY_FM2", age);
        fragment3.setArguments(bundle);
    }



    public String doGender() {
        String gen = null;
        if (male.isChecked()){
            gen = male.getText().toString();
        }else if (female.isChecked()){
            gen = female.getText().toString();
        }else if (other.isChecked()){
            gen = other.getText().toString();
        }
        return gen;
    }


    private boolean validateGender() {
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(getActivity(), "Please select gender", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateAge() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int userAge = datePicker.getYear();
        int isAgeValid = currentYear - userAge;
        if (isAgeValid < 14) {
            Toast.makeText(getActivity(), "You are not eligible to apply!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void innitView(View view){
        //innit view
        radioGroup = view.findViewById(R.id.radio_group);
        datePicker = view.findViewById(R.id.dp_age_picker);
        male = view.findViewById(R.id.rdo_male);
        female = view.findViewById(R.id.rdo_female);
        other = view.findViewById(R.id.rdo_others);
        btnFm3 = view.findViewById(R.id.btnFm3);
        progress_bar_fm3 = view.findViewById(R.id.progress_bar_fm3);
    }

}