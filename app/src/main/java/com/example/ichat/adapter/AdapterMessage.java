package com.example.ichat.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ichat.R;
import com.example.ichat.models.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterMessage extends RecyclerView.Adapter<AdapterMessage.MyHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    Context context;
    private List<Chat> mChat;
    String imageUrl;

    FirebaseUser fUser;

    public AdapterMessage(Context context, List<Chat> mChat, String imageUrl) {
        this.context = context;
        this.mChat = mChat;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (i == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, viewGroup, false);
            return new MyHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, viewGroup, false);
            return new MyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, final int i) {
        Chat chat = mChat.get(i);
        if (i == mChat.size() - 1) {
            if (chat.isIsseen()) {
                myHolder.tvSeen.setText("Đã xem");
            } else {
                myHolder.tvSeen.setText("Đã gửi");
            }
        } else {
            myHolder.tvSeen.setVisibility(View.GONE);
        }
        myHolder.tvMessage.setText(chat.getMessage());
        if (imageUrl.equals("default")) {
            myHolder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(imageUrl).into(myHolder.profile_image);
        }

        String message = mChat.get(i).getMessage();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
        Date resultdate = new Date(chat.getTime());
//        String type = mChat.get(i).getType();


//        if (type.equals("text")) {
//            //text message
//            myHolder.messageTv.setVisibility(View.VISIBLE);
//            myHolder.messageIv.setVisibility(View.GONE);
//
//            myHolder.messageTv.setText(message);
//        } else {
//            //image message
//            myHolder.messageTv.setVisibility(View.GONE);
//            myHolder.messageIv.setVisibility(View.VISIBLE);
//
//            Picasso.get().load(message).placeholder(R.drawable.ic_image_black).into(myHolder.messageIv);
//        }

        //set data
        myHolder.tvMessage.setText(message);
        myHolder.tvTime.setText(sdf.format(resultdate));
        try {
            Picasso.get().load(imageUrl).into(myHolder.profile_image);
        } catch (Exception e) {

        }

    }
    @Override
    public int getItemCount() {
        return mChat.size();
    }

    @Override
    public int getItemViewType(int position) {
        //get currently signed in user
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        //views
        private CircleImageView profile_image;
        private TextView tvMessage;
        private TextView tvSeen;
        private TextView tvTime;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            //init views
            profile_image = (CircleImageView) itemView.findViewById(R.id.profile_image);
            tvMessage = (TextView) itemView.findViewById(R.id.tvMessage);
            tvSeen = (TextView) itemView.findViewById(R.id.tvSeen);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
        }
    }
}
