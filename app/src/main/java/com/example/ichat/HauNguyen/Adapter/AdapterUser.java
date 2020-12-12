package com.example.ichat.HauNguyen.Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ichat.HauNguyen.BottomSheetUpdateUser;
import com.example.ichat.HauNguyen.DAO.UserDAO;
import com.example.ichat.HauNguyen.Model.User_Profile;
import com.example.ichat.R;

import java.util.ArrayList;

import static com.example.ichat.HauNguyen.FragmentHome.adapterUser;
import static com.example.ichat.HauNguyen.FragmentHome.rcvUser;


public class AdapterUser extends RecyclerView.Adapter<AdapterUser.ViewHolder> {
    Activity context;
    ArrayList<User_Profile> dataUser;

    public AdapterUser(Activity context, ArrayList<User_Profile> dataUser) {
        this.context = context;
        this.dataUser = dataUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_user, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvUser.setText(
                "Email : " + dataUser.get(position).getEmail() + "\n" +
                        "Password : " + dataUser.get(position).getPassword() + "\n" +
                        "Username : " + dataUser.get(position).getUsername() + "\n" +
                        "Fullname : " + dataUser.get(position).getFullname() + "\n" +
                        "Gender : " + dataUser.get(position).getGender() + "\n" +
                        "Birthday : " + dataUser.get(position).getBirthday());

        holder.tvUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("Email_x", dataUser.get(position).getEmail() + "");
                args.putString("Pass_x", dataUser.get(position).getPassword() + "");
                args.putString("Username_x", dataUser.get(position).getUsername() + "");
                args.putString("Fullname_x", dataUser.get(position).getFullname() + "");
                args.putString("Gender_x", dataUser.get(position).getGender() + "");
                args.putString("Birthday_x", dataUser.get(position).getBirthday() + "");
                BottomSheetUpdateUser bottom_sheft_update_theLoaiSach = new BottomSheetUpdateUser();
                bottom_sheft_update_theLoaiSach.show(((FragmentActivity) context).getSupportFragmentManager(), "TAG-Adapter");
                bottom_sheft_update_theLoaiSach.setArguments(args);

            }
        });

        holder.tvUser.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Thông Báo");
                builder.setMessage("Bạn có chắc muốn xóa không");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String email = dataUser.get(position).getEmail();
                        String pass = dataUser.get(position).getPassword();
                        String username = dataUser.get(position).getUsername();
                        String fullname = dataUser.get(position).getFullname();
                        String gender = dataUser.get(position).getGender();
                        String birthday = dataUser.get(position).getBirthday();
                        dataUser = new ArrayList<>();
                        UserDAO userDAO = new UserDAO(context);
                        User_Profile theloaisach = new User_Profile(email, pass, username, fullname, gender, birthday);
                        userDAO.delete(theloaisach);
                        dataUser = userDAO.getAll();
                        adapterUser = new AdapterUser(context, dataUser);
                        rcvUser.setAdapter(adapterUser);

                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataUser = new ArrayList<>();
                        UserDAO userDAO = new UserDAO(context);
                        dataUser = userDAO.getAll();
                        AdapterUser adapterUser = new AdapterUser(context, dataUser);
                        rcvUser.setAdapter(adapterUser);
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUser = itemView.findViewById(R.id.tvUser);

        }
    }
}
