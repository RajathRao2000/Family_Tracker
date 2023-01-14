package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.Options.MyCurrentLoc;
import com.example.myapplication.Options.OA_inviteActivity;
import com.example.myapplication.Options.OA_joinCircle;
import com.example.myapplication.Options.OA_myCircle;
import com.example.myapplication.Options.OA_profile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OptionsActivity extends AppCompatActivity {

    Button profileButton,joinCircleButton,inviteButton,myCircleButton,homeButton,logoutButton,currentlocationbutton;
    FirebaseAuth fauth;
    FirebaseUser fuser;
    DatabaseReference databaseReference;
    String current_uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        profileButton = findViewById(R.id.profileBtn);
        joinCircleButton = findViewById(R.id.joinCircleBtn);
        inviteButton = findViewById(R.id.InviteBtn);
        myCircleButton = findViewById(R.id.myCircleBtn);
        homeButton = findViewById(R.id.homeBtn);
        logoutButton = findViewById(R.id.logoutBtn);
        currentlocationbutton = findViewById(R.id.MyCurLocBtn);

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://familytrack-66c23-default-rtdb.firebaseio.com/");
        fauth = FirebaseAuth.getInstance();
        fuser = fauth.getCurrentUser();
        current_uid = fuser.getUid();
        
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfileActivity();
            }
        });
        
        joinCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToJoinCircleActivity();
            }
        });

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToInviteActivity();
            }
        });

        myCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMyCircleActivity();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHomeActivity();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLogoutActivity();
            }
        });

        currentlocationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCurrentLocActivity();
            }
        });
        
        

    }

    private void goToCurrentLocActivity() {
        startActivity(new Intent(OptionsActivity.this, MyCurrentLoc.class));
        finish();

    }

    private void goToInviteActivity() {
        startActivity(new Intent(OptionsActivity.this, OA_inviteActivity.class));
        finish();

    }

    private void goToMyCircleActivity() {
        startActivity(new Intent(OptionsActivity.this, OA_myCircle.class));
        finish();
    }

    private void goToHomeActivity() {
        startActivity(new Intent(OptionsActivity.this, Home.class));
        finish();
    }

    private void goToLogoutActivity() {
        fuser = fauth.getCurrentUser();
        if (fuser != null)
        {
            fauth.signOut();
            finish();
            startActivity(new Intent(OptionsActivity.this, LoginActivity.class));
        }
    }

    private void goToJoinCircleActivity() {
        startActivity(new Intent(OptionsActivity.this, OA_joinCircle.class));
        finish();
    }

    private void goToProfileActivity() {
        startActivity(new Intent(OptionsActivity.this, OA_profile.class));
        finish();
    }
}