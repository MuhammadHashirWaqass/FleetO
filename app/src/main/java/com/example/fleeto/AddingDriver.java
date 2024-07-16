package com.example.fleeto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class AddingDriver extends AppCompatActivity {

    EditText DriverN, DriverA,DriverVehicle,DriverPass, DriverConfirmPassword;
    Button AddDbtn, back;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_driver);

        DriverN = findViewById(R.id.AddingDriverName);
        DriverA = findViewById(R.id.AddingDriverAge);
        DriverVehicle= findViewById(R.id.AddingDriverVehicle);
        DriverPass = findViewById(R.id.AddingDriverPassword);
        DriverConfirmPassword = findViewById(R.id.AddingDriverConfirmPassword);
        AddDbtn = findViewById(R.id.ConfirmDriverBTN);
        back = findViewById(R.id.BackFromAddingDBTN);



        progressDialog = new ProgressDialog(AddingDriver.this);
        progressDialog.setMessage("Adding Driver...");



        AddDbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            // getting field values
            String driverName = DriverN.getText().toString().trim();
            String driverAge = DriverA.getText().toString();
            String driverPassword = DriverPass.getText().toString().trim();
            String driverConfirmPassword = DriverConfirmPassword.getText().toString().trim();
            String driverVehicle = DriverVehicle.getText().toString().trim();

             // Handle error validations

            // Handling empty Fields
           if (setErrorOnEmptyField(DriverN) || setErrorOnEmptyField(DriverA)  || setErrorOnEmptyField(DriverPass) || setErrorOnEmptyField(DriverConfirmPassword) || setErrorOnEmptyField(DriverVehicle)){
               return;
           }

            // Invalid password length
            if(driverPassword.length()<6)
            {
                DriverPass.setError("Password Length Must be At least 6 Characters");
                DriverPass.setFocusable(true);
                return;
            }

            // Passwords do not match
            if (!driverPassword.equals(driverConfirmPassword) ){
                DriverConfirmPassword.setError("Passwords do not match");
                DriverConfirmPassword.setFocusable(true);
                return;
            }

            // add the driver here
            addDriverToOwner(driverName, Integer.parseInt(driverAge), driverPassword, driverVehicle);
            }
        });

        // back button click
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public boolean setErrorOnEmptyField(EditText fieldEditText){
        String fieldValue = fieldEditText.getText().toString().trim();
        Log.d("print", fieldValue);
        if (fieldValue.isEmpty()){
            fieldEditText.setError("Please enter this field");
            fieldEditText.setFocusable(true);
            return true;
        }
        return false;
    }

    public void addDriverToOwner(String name, int age, String password, String vehicle){
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create JSON object for the request body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", name);
            jsonBody.put("age", age);
            jsonBody.put("password", password);
            jsonBody.put("vehicle", vehicle);
            jsonBody.put("ownerId", User.getUserInstance().getUserId());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest  = new JsonObjectRequest(Request.Method.POST, ApiPath.getInstance().getUrl() + "/api/driver/addDriver", jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject  response) {
                        try {
                            progressDialog.dismiss();
                            Toast.makeText(AddingDriver.this,response.getString("message"),Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddingDriver.this, AdminDash.class));
                            finish();
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
