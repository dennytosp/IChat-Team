package com.example.ichat.HauNguyen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ichat.HauNguyen.Adapter.AdapterUser;
import com.example.ichat.HauNguyen.DAO.UserDAO;
import com.example.ichat.HauNguyen.Model.User_Profile;
import com.example.ichat.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FragmentHome extends Fragment {
    private static final String TAG = "Home";
    public static RecyclerView rcvUser;
    public static DatabaseReference ref;
    public static AdapterUser adapterUser;
    public static ArrayList<User_Profile> dataUser;

    private UserDAO userDAO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);
        innitView(view);
        ref = FirebaseDatabase.getInstance().getReference().child("Users_Profile");
        dataUser = new ArrayList<com.example.ichat.HauNguyen.Model.User_Profile>();
        userDAO = new UserDAO(getActivity());
        dataUser = userDAO.getAll();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcvUser.setHasFixedSize(true);
        rcvUser.setLayoutManager(layoutManager);
        adapterUser = new AdapterUser(getActivity(), dataUser);
        rcvUser.setAdapter(adapterUser);

        return view;
    }


    @Override
    public void onResume() {
        adapterUser.notifyDataSetChanged();
        super.onResume();
    }

    private void innitView(View view) {
        rcvUser = view.findViewById(R.id.rcvUser);
    }

}