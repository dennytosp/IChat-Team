package com.example.ichat.HauNguyen.DAO;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ichat.HauNguyen.FragmentHome;
import com.example.ichat.HauNguyen.Model.User_Profile;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import static com.example.ichat.HauNguyen.FragmentHome.adapterUser;
import static com.example.ichat.HauNguyen.FragmentHome.dataUser;


public class UserDAO {
    Context context;
    DatabaseReference mRef;
    String key;
    FragmentHome fragmentHome;

    public UserDAO(Context context) {
        this.context = context;
        this.mRef = FirebaseDatabase.getInstance().getReference("Users_Profile");
    }

    public UserDAO(Context context, FragmentHome fragmentHome) {
        this.context = context;
        this.fragmentHome = fragmentHome;
        this.mRef = FirebaseDatabase.getInstance().getReference("Users_Profile");
    }

    public ArrayList<User_Profile> getAll() {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    dataUser.clear();
                    Iterable<DataSnapshot> dataSnapshotIterable = snapshot.getChildren();
                    Iterator<DataSnapshot> iterator = dataSnapshotIterable.iterator();
                    while (iterator.hasNext()) {
                        DataSnapshot next = (DataSnapshot) iterator.next();
                        User_Profile theloaisach = next.getValue(User_Profile.class);
                        dataUser.add(theloaisach);
                    }
                    adapterUser.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return dataUser;
    }

    public void insert(User_Profile item) {
        // push cây theo mã tự tạo
        // string key lấy mã push
        key = mRef.push().getKey();
        //insert theo child mã key setvalue theo item
        mRef.child(key).setValue(item).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Insert Thành Công", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(context, "Insert Thất Bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean update(final User_Profile item) {
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("email").getValue(String.class).equalsIgnoreCase(item.getEmail())) {
                        key = dataSnapshot.getKey();
                        mRef.child(key).setValue(item);
                        Toast.makeText(context, "Update Thành Công", Toast.LENGTH_SHORT).show();
                        adapterUser.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapterUser.notifyDataSetChanged();
        return true;
    }

    public void delete(final User_Profile item) {
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.child("email").getValue(String.class).equalsIgnoreCase(item.getEmail())) {
                        key = dataSnapshot.getKey();
                        mRef.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Delete Thành Công", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Delete Thất Bại", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
                adapterUser.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
