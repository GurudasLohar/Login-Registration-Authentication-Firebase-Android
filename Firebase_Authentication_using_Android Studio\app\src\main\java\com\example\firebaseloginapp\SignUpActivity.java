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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private static final Pattern PASSWORD_Pattern =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +             // 1 number digit
                    "(?=.*[A-Z])" +             // 1 uppercase
                    "(?=.*[a-z])" +             // 1 lowercase
                    "(?=.*[!@#$%^&*+_=-])" +    // 1 symbol
                    ".{8,15}" +                 // min. 8 and max.15 character
                    "$");

    EditText txtSignUpEmail,txtSignUpPassword;
    TextView txtSignIn;
    Button btnSignUp;
    ProgressBar progressSignUp;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.setTitle("Sign Up");

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        txtSignUpEmail = findViewById(R.id.txtSignUpEmail);
        txtSignUpPassword = findViewById(R.id.txtSignUpPassword);
        txtSignIn = findViewById(R.id.txtSignIn);
        btnSignUp = findViewById(R.id.btnSignUp);
        progressSignUp = findViewById(R.id.progressSignUp);

        txtSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnSignUp:
                userRegistration();
                break;

            case R.id.txtSignIn:
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void userRegistration() {

        String email = txtSignUpEmail.getText().toString().trim();
        String password = txtSignUpPassword.getText().toString().trim();

        if(email.isEmpty()){
            txtSignUpEmail.setError("Please,enter email address");
            txtSignUpEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            txtSignUpEmail.setError("Enter valid email address");
            txtSignUpEmail.requestFocus();
            return;
        }

        if(password.isEmpty()){
            txtSignUpPassword.setError("Please,enter password");
            txtSignUpPassword.requestFocus();
            return;
        }
        if(!PASSWORD_Pattern.matcher(password).matches()){
            txtSignUpEmail.setError("Password must contain 1 digit, 1 lowercase, 1 uppercase, 1 symbol");
            txtSignUpEmail.requestFocus();
            return;
        }


        progressSignUp.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressSignUp.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(SignUpActivity.this, "User is already Registered", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(SignUpActivity.this, "Error :"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}
