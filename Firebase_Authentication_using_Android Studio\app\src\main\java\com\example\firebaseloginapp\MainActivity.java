package com.example.firebaseloginapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    EditText txtSignInEmail,txtSignInPassword;
    TextView txtSignUp;
    Button btnSignIn;
    ProgressBar progressSignIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Sign In");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        txtSignInEmail = findViewById(R.id.txtSignInEmail);
        txtSignInPassword =findViewById(R.id.txtSignInPassword);
        txtSignUp = findViewById(R.id.txtSignUp);
        btnSignIn = findViewById(R.id.btnSignIn);
        progressSignIn = findViewById(R.id.progressSignIn);

        txtSignUp.setOnClickListener(this);
        btnSignIn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnSignIn:
                 userLogin();
                break;

            case R.id.txtSignUp:
                Intent intent = new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void userLogin() {

        String email = txtSignInEmail.getText().toString().trim();
        String password = txtSignInPassword.getText().toString().trim();

        if(email.isEmpty()){
            txtSignInEmail.setError("Please,enter email address");
            txtSignInEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtSignInEmail.setError("Please,enter valid email address");
            txtSignInEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            txtSignInPassword.setError("Please,enter password");
            txtSignInPassword.requestFocus();
            return;
        }
        if(password.length()<5){
            txtSignInPassword.setError("Minimum length of password shuold be 5");
            txtSignInPassword.requestFocus();
            return;
        }
        progressSignIn.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressSignIn.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            finish();
                            Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
