package com.example.myapplication.Options;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.OptionsActivity;
import com.example.myapplication.R;
import com.example.myapplication.adapter.membersadepter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OA_myCircle extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    //users cuser;
    ArrayList<String> namelist, idlist;

    DatabaseReference databaseReference, currentreference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    AlertDialog.Builder builder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_my_circle);

        recyclerView = findViewById(R.id.view_recyclerview);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String useruid = firebaseUser.getUid();

        namelist = new ArrayList<>();
        idlist = new ArrayList<>();

        layoutManager = new LinearLayoutManager(OA_myCircle.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://familytrack-66c23-default-rtdb.firebaseio.com/");
        currentreference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://familytrack-66c23-default-rtdb.firebaseio.com/").child(useruid).child("circle_members");

        currentreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                namelist.clear();
                idlist.clear();

                if (dataSnapshot.exists())
                {
                    for (DataSnapshot dss: dataSnapshot.getChildren())
                    {
                        String circleid = dss.child("circlememberid").getValue(String.class);

                        databaseReference.child(circleid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //users cuser = dataSnapshot.getValue(users.class);
                                String fetchname = dataSnapshot.child("firstname").getValue(String.class);
                                String fetchid = dataSnapshot.child("id").getValue(String.class);
                                namelist.add(fetchname);
                                idlist.add(fetchid);
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter = new membersadepter(namelist, idlist, OA_myCircle.this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed(){

        builder = new AlertDialog.Builder(OA_myCircle.this);
        builder.setMessage("Go Back?").setTitle(R.string.dialog_title)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(new Intent(OA_myCircle.this, OptionsActivity.class));
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