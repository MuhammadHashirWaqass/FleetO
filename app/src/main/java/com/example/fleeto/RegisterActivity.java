package com.example.fleeto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    Button back;
    Button RegisterConfirm;
    EditText Email, Password;
    ProgressDialog progressDialog;
    TextView Login;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_owner_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        back = findViewById(R.id.BackFromRegisterBTN);
        Email = findViewById(R.id.EmailRegister);
        Password = findViewById(R.id.PasswordRegister);
        RegisterConfirm = findViewById(R.id.ConfirmRegisterBTN);
        mAuth = FirebaseAuth.getInstance();
        Login = findViewById(R.id.LoginTVRegister);
        SpannableString spannableString = new SpannableString("Already have an account? Login");
        int startIndex = spannableString.toString().indexOf("Login");
        int endIndex = startIndex + "Login".length();
        int lightBlueColor = Color.parseColor("#6495ED");
        spannableString.setSpan(new ForegroundColorSpan(lightBlueColor), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Login.setText(spannableString);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RegisterConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(RegisterActivity.this);
                progressDialog.setMessage("Registering User...");

                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    Email.setError("Invalid Email");
                    Email.setFocusable(true);
                }
                else if(password.length()<6)
                {
                    Password.setError("Password Length Must be At least 6 Characters");
                    Password.setFocusable(true);
                }
                else
                {
                    registerUser(email,password);
                }
            }
        });

    }

    private void registerUser(String email, String password) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();
                            Toast.makeText(RegisterActivity.this,"User Registered",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });
    }
}