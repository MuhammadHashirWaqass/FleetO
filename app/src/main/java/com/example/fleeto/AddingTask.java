package com.example.fleeto;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

public class AddingTask extends AppCompatActivity {
    Button back, addTaskButton;
    TextView AddTaskDetailsTextView;
    EditText TaskTitle, TaskDescription, TaskAddress;
    ProgressDialog progressDialog;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adding_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        back = findViewById(R.id.BackFromAddingTBTN);
        addTaskButton = findViewById(R.id.ConfirmTaskBTN);
        AddTaskDetailsTextView = findViewById(R.id.driver_id_show_label);
        TaskTitle = findViewById(R.id.AddingTaskTitle);
        TaskDescription = findViewById(R.id.AddingTaskDescription);
        TaskAddress = findViewById(R.id.AddingTaskAddress);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding Task");

        AddTaskDetailsTextView.setText("Add Task Details for Driver: " + Global.getInstance().getDriverId());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = TaskTitle.getText().toString().trim();
                String description = TaskDescription.getText().toString().trim();
                String address = TaskAddress.getText().toString().trim();

                // Handling Errors

                //Check for empty fields

                if (title.isEmpty()){
                    TaskTitle.setError("Please enter task title");
                    TaskTitle.setFocusable(true);
                    return;
                }

                if (description.isEmpty()){
                    TaskDescription.setError("Please enter task description");
                    TaskDescription.setFocusable(true);
                    return;
                }

                if (address.isEmpty()){
                    TaskAddress.setError("Please enter task address");
                    TaskAddress.setFocusable(true);
                    return;
                }

                addTaskToDriver(title, description, address );
            }
        });
    }

    private void addTaskToDriver(String title, String description, String address){
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        // Create JSON object for the request body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("title", title);
            jsonBody.put("description", description);
            jsonBody.put("address", address);
            jsonBody.put("driverId", Global.getInstance().getDriverId());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest  = new JsonObjectRequest(Request.Method.POST, ApiPath.getInstance().getUrl()+ "/api/task/addTaskToDriver", jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            Toast.makeText(AddingTask.this,response.getString("message"),Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddingTask.this, AdminDash.class));
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