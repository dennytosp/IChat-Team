package com.example.ichat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.ichat.adapter.AdapterMessage;
import com.example.ichat.models.Chat;
import com.example.ichat.models.User;
import com.example.ichat.notifications.Data;
import com.example.ichat.notifications.Sender;
import com.example.ichat.notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private CircleImageView profileImage;
    private TextView username;
    private RecyclerView recycleviewMess;
    private EditText edtMessage;
    private ImageButton btnSendMess;


    FirebaseUser mUser;
    DatabaseReference reference;

    Intent intent;
    String userId;
    private RequestQueue requestQueue;
    AdapterMessage messageAdapter;
    List<Chat> mChat;

    ValueEventListener seenListener;


    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = findViewById(R.id.toolbar);
        profileImage = (CircleImageView) findViewById(R.id.profile_image);
        username = (TextView) findViewById(R.id.username);
        recycleviewMess = (RecyclerView) findViewById(R.id.recycleview_mess);
        edtMessage = (EditText) findViewById(R.id.edtMessage);
        btnSendMess = (ImageButton) findViewById(R.id.btnSendMess);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // and this
//                startActivity(new Intent(ChatActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });


        requestQueue = Volley.newRequestQueue(getApplicationContext());

        recycleviewMess.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recycleviewMess.setLayoutManager(linearLayoutManager);
        // user đang login
        mUser = FirebaseAuth.getInstance().getCurrentUser();


        //id người nhận
        Bundle bundle = getIntent().getExtras();
        intent = getIntent();
        userId = bundle.getString("hisUid");
        Log.d("TAG", userId);

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getName());
                if (user.getImage().equals("default")) {
                    profileImage.setImageResource(R.mipmap.ic_launcher);
                } else {

                    //change
                    Glide.with(getApplicationContext()).load(user.getImage()).into(profileImage);
                }
                //        id user đang login/ id người nhận/ ảnh người nhận
                readMessage(mUser.getUid(), userId, user.getImage());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        seenMessage(userId);

    }

    private void seenMessage(final String userId) {
        reference = FirebaseDatabase.getInstance().getReference("Chats");

        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(mUser.getUid()) && chat.getSender().equals(userId)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //button gửi tin nhắn
    public void sendMess(View view) {
        notify = true;
        String message = edtMessage.getText().toString();
        if (message.isEmpty()) {
            Toast.makeText(this, "Không thể gửi tin nhắn trống", Toast.LENGTH_SHORT).show();
        } else {

            sendMessage(mUser.getUid(), userId, message);
        }
        edtMessage.setText("");

    }

    private void sendMessage(String sender, final String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        long millis = System.currentTimeMillis();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);
        hashMap.put("time", millis);

        reference.child("Chats").push().setValue(hashMap);


        //thêm user vào màn hình ChatFragments
        //lấy mảng id người nhận theo id user đang login cho vào dữ liệu
        final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(mUser.getUid())
                .child(userId);

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatRef.child("id").setValue(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(userId)
                .child(mUser.getUid());
        chatRefReceiver.child("id").setValue(mUser.getUid());

        final String msg = message;

        reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (notify) {
                    sendNotifiaction(receiver, user.getName(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ChatActivity.this, "Lỗi" + databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void sendNotifiaction(String receiver, final String username, final String message) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(mUser.getUid(), R.mipmap.ic_launcher, username + ": " + message, "New Message",
                            userId);
                    Sender sender = new Sender(data, token.getToken());
                    try {
                        JSONObject senderJsonObj = new JSONObject(new Gson().toJson(sender));
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", senderJsonObj,
                                new com.android.volley.Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        //response of the request
                                        Log.d("JSON_RESPONSE", "onResponse: " + response.toString());

                                    }
                                }, new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("JSON_RESPONSE", "onResponse: " + error.toString());
                            }
                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                //put params
                                Map<String, String> headers = new HashMap<>();
                                headers.put("Content-Type", "application/json");
                                headers.put("Authorization", "key=AAAAHqX9MAE:APA91bFH5xRBoo9sFD-Mll0gAeahMQ22O_c-kcvpxF6RBJ15_SJ_JvYSpEiyqYthEeODmAdQzPHmxzI0hPABrilnNw8L6KMCSFkSP3N0222ogJAHjcyf8wpqxCAfjsHhSU1xSxvZbBAe");


                                return headers;
                            }
                        };

                        //add this request to queue
                        requestQueue.add(jsonObjectRequest);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    apiService.sendNotification(sender)
//                            .enqueue(new Callback<MyResponse>() {
//                                @Override
//                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
//                                    if (response.code() == 200){
//                                        if (response.body().success != 1){
//                                            Toast.makeText(ChatActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<MyResponse> call, Throwable t) {
//
//                                }
//                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessage(final String senderId, final String receiverId, final String imageUrl) {
        mChat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);

                    if (chat.getReceiver().equals(senderId) && chat.getSender().equals(receiverId)
                            || chat.getReceiver().equals(receiverId) && chat.getSender().equals(senderId)) {
                        mChat.add(chat);
                    }

                }
                messageAdapter = new AdapterMessage(ChatActivity.this, mChat, imageUrl);
                recycleviewMess.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void status(String status) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("onlineStatus", status);

        reference.updateChildren(hashMap);
    }

    //user người nhận
    private void currentUser(String userid) {
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
        currentUser(userId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListener);
        status("offline");
        currentUser("none");
    }

}
