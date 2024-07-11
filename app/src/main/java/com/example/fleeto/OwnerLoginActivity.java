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

public class OwnerLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button back;
    Button LoginConfirm;
    EditText Email, Password;
    ProgressDialog progressDialog;
    TextView Register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_owner_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        back = findViewById(R.id.BackFromLoginBTN);

        // Input fields
        LoginConfirm = findViewById(R.id.ConfirmLoginBTN);
        Email = findViewById(R.id.EmailLogin);
        Password = findViewById(R.id.PasswordLogin);
        Register = findViewById(R.id.RegisterTVLogin);

        // Setting color of Register
        SpannableString spannableString = new SpannableString("Already have an account? Register");
        int startIndex = spannableString.toString().indexOf("Register");
        int endIndex = startIndex + "Register".length();
        int lightBlueColor = Color.parseColor("#6495ED");
        spannableString.setSpan(new ForegroundColorSpan(lightBlueColor), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Register.setText(spannableString);

        // Click on home
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // go to owner registration
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwnerLoginActivity.this, OwnerRegisterActivity.class));
                finish();
            }
        });

        // Sign in owner
        LoginConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(OwnerLoginActivity.this);
                progressDialog.setMessage("Logging in...");

                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();

                // Handling errors
                if (email.isEmpty()){
                    Email.setError("Please enter your email");
                    Email.setFocusable(true);
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    Email.setError("Invalid Email");
                    Email.setFocusable(true);
                    return;
                }
                if (password.isEmpty()){
                    Password.setError("Please enter your password");
                    Password.setFocusable(true);
                    return;
                }

                // sign in owner
                loginUser(email,password);

            }
        });

    }

    // handling sign in
    private void loginUser(String email, String password) {
        progressDialog.show();
        // firebase sign in
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            startActivity(new Intent(OwnerLoginActivity.this, AdminDash.class));
                            finish();
                            Toast.makeText(OwnerLoginActivity.this,"User Logged In",Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(OwnerLoginActivity.this,"Incorrect Credentials",Toast.LENGTH_LONG).show();
                    }
                });
    }
}