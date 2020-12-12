package com.example.ichat.HauNguyen.ForgotPassword;

import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hbb20.CountryCodePicker;

public class Fragment_SendOTP_PhoneNumber extends Fragment {
    private static final String TAG = "Fragment_SendOTP_PhoneNumber";
    private EditText edtPhone;
    private Button btn_resetPass, btnCancel;
    private ProgressBar progressBar;
    CountryCodePicker countryCodePicker;

    //firebase
    private FirebaseUser user;
    private FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_otp_phone, container, false);
        innitView(view);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        btn_resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = edtPhone.getText().toString().trim();
                if (!validatePhoneNumber(phoneNumber)){
                    return;
                }else {

                    sendData(new Fragment_VerifyOTP());

                }
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


    private void sendData(Fragment_VerifyOTP verifyOTP) {
        //Send phoneNumber to Fragment4
        String codePicker = countryCodePicker.getTextView_selectedCountry().getText().toString();
        String reverse = new StringBuffer(codePicker).reverse().toString();
        String cutString = reverse.substring(0, 4);
        String reverse2 = new StringBuffer(cutString).reverse().toString();
        reverse2 = reverse2.replace(" ", "");
        String _getUserEnteredPhoneNumber = edtPhone.getText().toString().trim();
        if (_getUserEnteredPhoneNumber.charAt(0) == '0') { // phương thức charAt(0) = '0' => lấy chuỗi con của một chuỗi vd: 0912345678 thì charAt(0) tức là số 0 đầu tiên của số điện thoại vừa nhập
            _getUserEnteredPhoneNumber = _getUserEnteredPhoneNumber.substring(1);  // substring(1) => tức lấy 912345678
        }
        final String _phoneNo = reverse2 + _getUserEnteredPhoneNumber;

        Bundle bundle = new Bundle();
        bundle.putString("PHONE_OTP", _phoneNo);
        verifyOTP.setArguments(bundle);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.anim_in_right, R.anim.anim_out_right, R.anim.anim_in_left, R.anim.anim_out_left)
                .replace(R.id.fr_layout_otp, verifyOTP, TAG)
                .addToBackStack(null)
                .commit();
    }


    private void sendPasswordResetEmail(String email){
        String phoneNumber = edtPhone.getText().toString().trim();
        if (!validatePhoneNumber(phoneNumber)){
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getActivity(), "Email sent.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        Fragment_SendOTP_PhoneNumber.this.getActivity().finish();
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }else {
                        Toast.makeText(getActivity(), "Email has not been sent.", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                }
            });
        }else {
            Toast.makeText(getActivity(), "Unsuccessfully!", Toast.LENGTH_SHORT).show();
        }

    }


    private boolean validatePhoneNumber(String phoneNumber) {
        String checkspaces = "\\A\\w{1,20}\\z";
        if (phoneNumber.isEmpty()) {
            edtPhone.setError("Enter valid phone number");
            edtPhone.requestFocus();
            return false;
        } else if (phoneNumber.length() > 10) {
            edtPhone.setError("Please enter the correct phone number!");
            edtPhone.requestFocus();
            return false;
        }else if (!phoneNumber.matches(checkspaces)){
            edtPhone.setError("No white spaces are allowed!");
            edtPhone.requestFocus();
            return false;
        } else {
            edtPhone.setError(null);
            return true;
        }
    }


    private void innitView(View v) {
        //Hooks
        edtPhone = v.findViewById(R.id.txt_phone_resetPass);
        btn_resetPass = v.findViewById(R.id.btn_phone_resetPass);
        btnCancel = v.findViewById(R.id.btnCancel);
        progressBar = v.findViewById(R.id.progress_reset_password);
        countryCodePicker = v.findViewById(R.id.codePickerCountry);
    }
}