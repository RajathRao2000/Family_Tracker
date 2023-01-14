package com.example.myapplication.Options;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class OA_profile extends AppCompatActivity {

    TextView txtfirst, txtemail, txtpassword;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_profile);

        txtfirst = findViewById(R.id.firstname_textview);
        //txtlast = findViewById(R.id.lastname_textview);
        txtemail = findViewById(R.id.email_textview);
        txtpassword = findViewById(R.id.password_textview);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        final String getuid = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://familytrack-66c23-default-rtdb.firebaseio.com/").child(getuid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String firstname = dataSnapshot.child("firstname").getValue(String.class);
                //String lastname = dataSnapshot.child("lastname").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String password = dataSnapshot.child("password").getValue(String.class);

                txtfirst.setText(firstname);
                //txtlast.setText(lastname);
                txtemail.setText(email);
                txtpassword.setText(password);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onBackPressed(){

        builder = new AlertDialog.Builder(OA_profile.this);
        builder.setMessage("Go Back?").setTitle(R.string.dialog_title)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(new Intent(OA_profile.this, OptionsActivity.class));
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