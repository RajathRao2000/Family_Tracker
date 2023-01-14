package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.modal.users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {
    Button registerBtn,goToLogin;

    EditText name,email,phone,password;
    DatabaseReference databaseReference;
    ProgressbarLoader loader;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);

        name = (EditText)findViewById(R.id.ET_name);
        email = (EditText)findViewById(R.id.ET_email);
        phone = (EditText)findViewById(R.id.ET_phone);
        password = (EditText)findViewById(R.id.ET_password);
        loader = new ProgressbarLoader(RegisterActivity.this);

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://familytrack-66c23-default-rtdb.firebaseio.com/");
        auth = FirebaseAuth.getInstance();

        registerBtn = (Button) findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterBtnOnClickAction();
            }
        });

        goToLogin = (Button) findViewById(R.id.goToLoginPageBtn);
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

    }
    private String generatecode() {
        Random r = new Random();
        int intcode = 100000 + r.nextInt(900000);
        String code = String.valueOf(intcode);
        return code;
    }

    private void RegisterBtnOnClickAction(){
        try {

            final String getName = name.getText().toString().trim();
            final String getEmail = email.getText().toString().trim();
            final String getPhone = phone.getText().toString().trim();
            final String getPassword = password.getText().toString().trim();

            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            final String strdate = dateFormat.format(date);

//            if (TextUtils.isEmpty(getName) || TextUtils.isEmpty(getEmail) || TextUtils.isEmpty(getPhone) || TextUtils.isEmpty(getPassword)) {
//                Toast.makeText(RegisterActivity.this, "fill the all field", Toast.LENGTH_SHORT).show();
//                loader.dismissloader();
//            }
            if (getName.isEmpty()) {
                name.setError("Empty field!");
                name.requestFocus();
                return;
            }
            else if (getEmail.isEmpty()) {
                email.setError("Empty field!");
                email.requestFocus();
                return;
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(getEmail).matches()) {
                email.setError("Not a valid email");
                email.requestFocus();
                return;
            }
            else if (getPassword.isEmpty()) {
                password.setError("Empty field!");
                password.requestFocus();
                return;
            }
            else if (getPassword.length() < 6) {
                password.setError("Password should be greater than 6 digits");
                password.requestFocus();
                return;
            }
            else if (getPhone.length() < 10) {
                phone.setError("Phone number should be 10 digits");
                phone.requestFocus();
                return;
            }

            else{
                loader.showloader();
                auth.createUserWithEmailAndPassword(getEmail, getPassword)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    String userid = auth.getCurrentUser().getUid();
                                    users usrinfo = new users(userid, getName, generatecode(), getEmail, getPassword, strdate, 0, 0, "null");
                                    FirebaseDatabase.getInstance().getReferenceFromUrl("https://familytrack-66c23-default-rtdb.firebaseio.com/")
                                            .child(userid)
                                            .setValue(usrinfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(RegisterActivity.this, "submited..", Toast.LENGTH_SHORT).show();
                                                    loader.dismissloader();
                                                    Intent in = new Intent(RegisterActivity.this, LoginActivity.class);
                                                    startActivity(in);
                                                }
                                            });

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(RegisterActivity.this, "database response error", Toast.LENGTH_SHORT).show();
                                    loader.dismissloader();
                                }
                            }
                        });
            }
        } catch (NullPointerException e) {
            Toast.makeText(RegisterActivity.this, "error:" + e, Toast.LENGTH_SHORT).show();
        }
    }
}