package com.example.ichat.HauNguyen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ichat.HauNguyen.Adapter.AdapterUser;
import com.example.ichat.HauNguyen.DAO.UserDAO;
import com.example.ichat.HauNguyen.Model.User_Profile;
import com.example.ichat.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import static com.example.ichat.HauNguyen.FragmentHome.adapterUser;
import static com.example.ichat.HauNguyen.FragmentHome.rcvUser;

public class BottomSheetUpdateUser extends BottomSheetDialogFragment {
    EditText edt_email, edt_pass, edt_username, edt_fullname, edt_gender, edt_birthday;
    Button btnUpdate;
    ArrayList<User_Profile> dataloaisach;
    UserDAO daoLoaiSach;
    User_Profile theloaisach;
    int position;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_bottom_sheet_update_user, container, false);
        innitView(view);

        //getBundle
        final Bundle getdata = getArguments();
        if (getdata != null){
            String e = getdata.getString("Email_x");
            String psw = getdata.getString("Pass_x");
            String usn = getdata.getString("Username_x");
            String full = getdata.getString("Fullname_x");
            String p = getdata.getString("Phone_x");
            String g = getdata.getString("Gender_x");
            String b = getdata.getString("Birthday_x");
            edt_email.setText(e);
            edt_pass.setText(psw);
            edt_username.setText(usn);
            edt_fullname.setText(full);
            edt_gender.setText(g);
            edt_birthday.setText(b);
        }else {
            edt_email.setText("null");
            edt_pass.setText("null");
            edt_username.setText("null");
            edt_fullname.setText("null");
            edt_gender.setText("null");
            edt_birthday.setText("null");
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emails = edt_email.getText().toString();
                String pass = edt_pass.getText().toString();
                String username = edt_username.getText().toString();
                String fullname = edt_fullname.getText().toString();
                String gender = edt_gender.getText().toString();
                String birthday = edt_birthday.getText().toString();
                if (emails.isEmpty() || pass.isEmpty() || username.isEmpty() || fullname.isEmpty() || gender.isEmpty() || birthday.isEmpty()) {
                    Toast.makeText(getActivity(), "Vui lòng nhập đầy đủ các dữ liệu", Toast.LENGTH_SHORT).show();
                } else {
                    dataloaisach = new ArrayList<>();
                    daoLoaiSach = new UserDAO(getContext());
                    theloaisach = new User_Profile(emails, pass, username, fullname, gender, birthday);
                    daoLoaiSach.update(theloaisach);
                    dataloaisach = daoLoaiSach.getAll();
                    adapterUser = new AdapterUser(getActivity(), dataloaisach);
                    rcvUser.setAdapter(adapterUser);
                    adapterUser.notifyDataSetChanged();
                    dismiss();
                }
            }
        });
        return view;
    }

    private void innitView(View view){
        edt_email = view.findViewById(R.id.txtEmailes);
        edt_pass = view.findViewById(R.id.txtPasses);
        edt_username = view.findViewById(R.id.txtUsernamees);
        edt_fullname = view.findViewById(R.id.txtFullnamees);
        edt_gender = view.findViewById(R.id.txtGenderes);
        edt_birthday = view.findViewById(R.id.txtBirthdayes);
        btnUpdate = view.findViewById(R.id.btn_updatees);
    }
}