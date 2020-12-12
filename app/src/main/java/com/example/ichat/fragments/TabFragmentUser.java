package com.example.ichat.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.ichat.R;
import com.example.ichat.Tab.tab_all;
import com.example.ichat.Tab.tab_friend;
import com.example.ichat.Tab.tab_friend_request;
import com.example.ichat.adapter.TabAdapter;
import com.google.android.material.tabs.TabLayout;


public class TabFragmentUser extends Fragment {
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usertab, container, false);
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tabs);
        adapter = new TabAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new tab_friend_request(), "Request");
        adapter.addFragment(new tab_friend(), "Friends");
        adapter.addFragment(new UsersFragment(), "All");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return view;

    }
}
