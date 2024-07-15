package com.example.fleeto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OwnerRegisterActivity extends AppCompatActivity {
    String url;
    Button back;
    Button RegisterConfirm;
    EditText Name, Email, Password, confirmPassword;
    ProgressDialog progressDialog;
    TextView Login;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

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

        url = ApiPath.getInstance().getUrl();
        back = findViewById(R.id.BackFromRegisterBTN);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        Login = findViewById(R.id.LoginTVRegister);

        // Initializing input fields
        Name = findViewById(R.id.NameRegister);
        Email = findViewById(R.id.EmailRegister);
        Password = findViewById(R.id.PasswordRegister);
        confirmPassword = findViewById(R.id.ConfirmPasswordRegister);
        RegisterConfirm = findViewById(R.id.ConfirmRegisterBTN);


        // Setting color of String Login
        SpannableString spannableString = new SpannableString("Already have an account? Login");
        int startIndex = spannableString.toString().indexOf("Login");
        int endIndex = startIndex + "Login".length();
        int lightBlueColor = Color.parseColor("#6495ED");
        spannableString.setSpan(new ForegroundColorSpan(lightBlueColor), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Login.setText(spannableString);

        // home button click
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Go to Owner login page
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OwnerRegisterActivity.this, OwnerLoginActivity.class));
                finish();
            }
        });

        // Handle Sign Up
        RegisterConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(OwnerRegisterActivity.this);
                progressDialog.setMessage("Registering User...");

                String name = Name.getText().toString().trim();
                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();
                String cPassword = confirmPassword.getText().toString().trim();

                // Handle Errors

                // Handling empty Fields
                if (name.isEmpty()){
                    Name.setError("Please enter your name");
                    Name.setFocusable(true);
                    return;
                }
                if (email.isEmpty()){
                    Email.setError("Please enter your email");
                    Email.setFocusable(true);
                    return;
                }

                // Invalid Email
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
                if (cPassword.isEmpty()){
                    confirmPassword.setError("Please confirm your password");
                    confirmPassword.setFocusable(true);
                    return;
                }


                // Invalid password length
                if(password.length()<6)
                {
                    Password.setError("Password Length Must be At least 6 Characters");
                    Password.setFocusable(true);
                    return;
                }

                // Passwords do not match
                if (!password.equals(cPassword) ){
                    confirmPassword.setError("Passwords do not match");
                    confirmPassword.setFocusable(true);
                    return;
                }

                // register the owner
                registerUser(name, email,password);

            }
        });

    }

    // function to register owner
    private void registerUser(String name, String email, String password) {
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        // Create JSON object for the request body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            jsonBody.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest  = new JsonObjectRequest(Request.Method.POST, url + "/api/auth/signUpOwner", jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            Toast.makeText(OwnerRegisterActivity.this,response.getString("message"),Toast.LENGTH_SHORT).show();


                            if (response.getString("message").equals("User Added Successfully")){
                                // Store user instance
                                User.getUserInstance().setUserId(response.getInt("userId"));
                                User.getUserInstance().setEmail(email);
                                User.getUserInstance().setPassword(password);
                                User.getUserInstance().setName(name);
                                startActivity(new Intent(OwnerRegisterActivity.this, AdminDash.class));
                                finish();
                            }


                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("api", "onError: " +  error);
            }

        }

        )
        {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=UTF-8";
            }
        };


        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }
}