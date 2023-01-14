package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowCircleCode extends AppCompatActivity {

    TextView showid;
    DatabaseReference databaseReference;
    FirebaseAuth fauth;
    FirebaseUser fuser;
    Button send,done;
    String currentcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_circle_code);
        showid = findViewById(R.id.code_textview);
        done = findViewById(R.id.doneBtn);
        send = findViewById(R.id.sharebutton);
        fauth = FirebaseAuth.getInstance();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        String current = fuser.getUid();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOnClickAction();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://familytrack-66c23-default-rtdb.firebaseio.com/").child(current);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentcode = dataSnapshot.child("circle_id").getValue(String.class);
                showid.setText(currentcode);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneOnClickAction();
            }
        });
    }

    private void doneOnClickAction() {
        startActivity(new Intent(ShowCircleCode.this, Home.class));
        finish();
    }

    private void sendOnClickAction() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String sharebody = "Circle Code: "+currentcode;
        String sharesub = "GPS track invitation code";
        intent.putExtra(Intent.EXTRA_SUBJECT,sharesub);
        intent.putExtra(Intent.EXTRA_TEXT,sharebody);
        startActivity(Intent.createChooser(intent, "Sharing Code"));
    }
}