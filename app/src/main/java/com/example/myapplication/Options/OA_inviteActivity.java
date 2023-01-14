package com.example.myapplication.Options;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.OptionsActivity;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OA_inviteActivity extends AppCompatActivity {
    TextView codetxt;
    Button sharebtn;

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    AlertDialog.Builder builder;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_invite);

        codetxt = findViewById(R.id.code_textview);
        sharebtn = findViewById(R.id.sharebutton);
        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharebuttonlistener();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String currentuid = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://familytrack-66c23-default-rtdb.firebaseio.com/").child(currentuid);

        displaycodelistener();
    }

    private void sharebuttonlistener() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String sharebody = "circle code: "+code;
        String sharesub = "gps helper invitation code";
        intent.putExtra(Intent.EXTRA_SUBJECT,sharesub);
        intent.putExtra(Intent.EXTRA_TEXT,sharebody);
        startActivity(Intent.createChooser(intent, "sharing code"));
    }

    private void displaycodelistener() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                code = dataSnapshot.child("circle_id").getValue(String.class);

                codetxt.setText(code);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed(){

        builder = new AlertDialog.Builder(OA_inviteActivity.this);
        builder.setMessage("Go Back?").setTitle(R.string.dialog_title)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(new Intent(OA_inviteActivity.this, OptionsActivity.class));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Exit");
        alert.show();

    }
}