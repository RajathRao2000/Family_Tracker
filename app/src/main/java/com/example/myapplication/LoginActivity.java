package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    TextView registerTV;
    EditText usrLoginName,usrLoginPassword;
    FirebaseAuth fauth;
    FirebaseUser fuser;
    ProgressbarLoader loader;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);


        usrLoginName = (EditText)findViewById(R.id.ET_LoginName);
        usrLoginPassword = (EditText) findViewById(R.id.ET_LoginPassword);

        fauth = FirebaseAuth.getInstance();
        fuser = fauth.getCurrentUser();

        //initilize progressbar
        loader = new ProgressbarLoader(LoginActivity.this);


        registerTV = (TextView) findViewById(R.id.registertv);
        registerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

        loginBtn = (Button)findViewById(R.id.LoginBtn); 
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loader.showloader();
                loginOnClickAction();
            }
        });


    }

    private void loginOnClickAction() {

        String email = usrLoginName.getText().toString().trim();
        String password = usrLoginPassword.getText().toString().trim();
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "fill all fields", Toast.LENGTH_SHORT).show();
            loader.dismissloader();
        } else {
            fauth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                loader.dismissloader();
                                Toast.makeText(LoginActivity.this, "success..", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, ShowCircleCode.class);
                                startActivity(intent);
                            } else {
                                loader.dismissloader();
                                Toast.makeText(LoginActivity.this, "failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
    @Override
    protected void onStart() {
        if (fuser != null){
            startActivity(new Intent(LoginActivity.this, Home.class));
            finish();
        }
        super.onStart();
    }
}