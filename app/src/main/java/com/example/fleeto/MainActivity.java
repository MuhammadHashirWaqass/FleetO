package com.example.fleeto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    TextView temp, owner;
    EditText DriverId, DriverPassword;
    Button LoginButton;
    ProgressDialog progressDialog;
    Button goToOwner;


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // initializing variables
        temp = findViewById(R.id.AdminLoginTV);
        DriverId = findViewById(R.id.DriverEmail);
        DriverPassword = findViewById(R.id.DriverPassword);
        LoginButton = findViewById(R.id.ConfirmDriverLoginBTN);
        goToOwner = findViewById(R.id.go_to_owner);
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Logging in...");

        // Setting color of Open Owner Login
        SpannableString spannableString = new SpannableString("Are you an Owner? Login Here");
        int startIndex = spannableString.toString().indexOf("Login Here");
        int endIndex = startIndex + "Login Here".length();
        int lightBlueColor = Color.parseColor("#6495ED");
        spannableString.setSpan(new ForegroundColorSpan(lightBlueColor), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        temp.setText(spannableString);

        // Open Owner Login
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent temp = new Intent( MainActivity.this, OwnerLoginActivity.class);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                startActivity(temp);
            }
        });

        goToOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent temp = new Intent( MainActivity.this, OwnerLoginActivity.class);
                overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
                startActivity(temp);
            }
        });

        // event listener for driver login
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String driverId = DriverId.getText().toString().trim();
                String driverPassword = DriverPassword.getText().toString().trim();

                // Handling errors
                if (driverId.isEmpty()){
                    DriverId.setError("Please enter driver id");
                    DriverId.setFocusable(true);
                    return;
                }

                if (driverPassword.isEmpty()){
                    DriverPassword.setError("Please enter driver id");
                    DriverPassword.setFocusable(true);
                    return;
                }

                // Login Driver
                loginDriver( driverId,  driverPassword);
            }
        });
    }

    private void loginDriver(String driverId, String driverPassword){
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create JSON object for the request body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("driverId", driverId);
            jsonBody.put("password", driverPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest  = new JsonObjectRequest(Request.Method.POST, ApiPath.getInstance().getUrl()+ "/api/auth/signInDriver", jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject  response) {
                        try {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this,response.getString("message"),Toast.LENGTH_SHORT).show();

                            if (response.getString("message").equals("Logged In Successfully")){
                                // Store user instance
                                User.getUserInstance().setUserId(response.getInt("userId"));
                                User.getUserInstance().setPassword(driverPassword);
                                User.getUserInstance().setName(response.getString("name"));
                                startActivity(new Intent(MainActivity.this, DriverDash.class));
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