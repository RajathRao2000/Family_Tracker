package com.example.myapplication.Options;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.OptionsActivity;
import com.example.myapplication.ProgressbarLoader;
import com.example.myapplication.R;
import com.example.myapplication.modal.circlejoin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class OA_joinCircle extends AppCompatActivity {

    EditText joinedit;
    Button joinbtn;
    DatabaseReference reference, circlerefernce;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    ProgressbarLoader loader;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_join_circle);

        joinedit = findViewById(R.id.join_edittext);
        joinbtn = findViewById(R.id.join_button);

        loader = new ProgressbarLoader(OA_joinCircle.this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        try {
            //String currentuid = firebaseUser.getUid();
            reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://familytrack-66c23-default-rtdb.firebaseio.com/");
            //currentuser = FirebaseDatabase.getInstance().getReference("users").child(currentuid);
        } catch (NullPointerException e) {
            Toast.makeText(OA_joinCircle.this, "error:" + e, Toast.LENGTH_SHORT).show();
        }

        joinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinbtnlistener();
            }
        });
    }

    private void joinbtnlistener() {
        loader.showloader();
        String no = joinedit.getText().toString().trim();
        Query query = reference.orderByChild("circle_id").equalTo(no);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //users userc;
                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                            /*userc = dss.getValue(users.class);
                            joinuid = userc.getid();*/

                        String joinid = dss.child("id").getValue(String.class);

                        circlerefernce = FirebaseDatabase.getInstance().getReferenceFromUrl("https://familytrack-66c23-default-rtdb.firebaseio.com/").child(joinid).child("circle_members");
                        //Toast.makeText(getActivity(), "response:"+joinid, Toast.LENGTH_SHORT).show();

                        circlejoin join = new circlejoin(firebaseUser.getUid());
                        //circlejoin join1 = new circlejoin(joinuid);

                        circlerefernce.child(firebaseUser.getUid()).setValue(join)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(OA_joinCircle.this, "joined success", Toast.LENGTH_SHORT).show();
                                            loader.dismissloader();
                                        }
                                    }
                                });
                    }
                } else {
                    Toast.makeText(OA_joinCircle.this, "this code is not available", Toast.LENGTH_SHORT).show();
                    loader.dismissloader();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed(){

        builder = new AlertDialog.Builder(OA_joinCircle.this);
        builder.setMessage("Go Back?").setTitle(R.string.dialog_title)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        startActivity(new Intent(OA_joinCircle.this, OptionsActivity.class));
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