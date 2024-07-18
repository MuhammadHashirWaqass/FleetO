package com.example.fleeto;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.api.Distribution;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.IdentityHashMap;

public class DriverDash extends AppCompatActivity {
    TableLayout currentTaskLayout, completedTaskLayout;
    Button SignOutButton;
    TextView IdHeadingTextView;
    ProgressDialog progressDialog;
    JSONArray listOfTasks;
    LinearLayout newLinearLayout ;
    int currentFlag = 0, completeFlag = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_driver_dash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},999);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},998);
            return;
        }
        startLocationUpdateService();

        int userId = User.getUserInstance().getUserId();
        IdHeadingTextView = findViewById(R.id.driver_id_heading_label);
        IdHeadingTextView.setText(String.valueOf("ID: "+userId));
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Tasks...");
        SignOutButton = findViewById(R.id.driver_logout_button);
        currentTaskLayout = findViewById(R.id.current_tasks_layout);
        completedTaskLayout = findViewById(R.id.completed_tasks_layout);

        SignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // sign out here
            }
        });

        getDriverTasks();
    }
    private void startLocationUpdateService() {
        Intent intent = new Intent(this, LocationUpdateService.class);
        startService(intent);
    }

    private void getDriverTasks(){
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create JSON object for the request body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("driverId", User.getUserInstance().userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                ApiPath.getInstance().getUrl() + "/api/task/getDriverTasks", jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            listOfTasks = response.getJSONArray("data");

                            populateTaskTables(listOfTasks);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("api", "onError: " + error);
            }

        }

        ) {

            @Override
            public String getBodyContentType() {
                return "application/json; charset=UTF-8";
            }
        };

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);


    }

    private void populateTaskTables(JSONArray listOfTasks){

        for (int i = 0; i < listOfTasks.length(); i++) {



            try {
                newLinearLayout = createNewLinearLayout(listOfTasks.getJSONObject(i).getString("status"));

                // creating and setting textView Values
                TextView idTextView = createTextView("Task ID: "+listOfTasks.getJSONObject(i).getString("taskId"));
                TextView titleTextView = createTextView("Title: "+listOfTasks.getJSONObject(i).getString("title"));
                TextView descriptionTextView = createTextView("Description: "+listOfTasks.getJSONObject(i).getString("description"));
                TextView addressTextView = createTextView("Address"+listOfTasks.getJSONObject(i).getString("address"));
                TextView statusTextView = createTextView("Status: "+ listOfTasks.getJSONObject(i).getString("status"));

                // adding textviews to layout
                newLinearLayout.addView(idTextView);
                newLinearLayout.addView(titleTextView);
                newLinearLayout.addView(descriptionTextView);
                newLinearLayout.addView(addressTextView);
                newLinearLayout.addView(statusTextView);

                if (listOfTasks.getJSONObject(i).getString("status").equals("pending")){
                    // adding buttons to layout
                    Button markAsDoneButton = createMarkAsDoneButton(
                            listOfTasks.getJSONObject(i).getString("taskId"));
                    newLinearLayout.addView(markAsDoneButton);
                }

            } catch (JSONException e) {
                Log.e("json", "HEHE:" + e.getMessage());
            }
        }

        if (currentFlag == 0){
            TextView noItemTextView = new TextView(this);
            noItemTextView.setText("No Current Tasks to display");
            noItemTextView.setGravity(Gravity.CENTER);
            noItemTextView.setTextSize(20);
            noItemTextView.setTextAppearance(Typeface.BOLD);
            noItemTextView.setTextColor(getResources().getColor(android.R.color.black));
            noItemTextView.setPadding(0,10,0,0);
            currentTaskLayout.addView(noItemTextView);
        }

        if (completeFlag == 0){
            TextView noItemTextView = new TextView(this);
            noItemTextView.setText("No Completed Tasks to display");
            noItemTextView.setGravity(Gravity.CENTER);
            noItemTextView.setTextSize(20);
            noItemTextView.setTextAppearance(Typeface.BOLD);
            noItemTextView.setTextColor(getResources().getColor(android.R.color.black));
            noItemTextView.setPadding(0,30,0,30);
            completedTaskLayout.addView(noItemTextView);
        }
        progressDialog.dismiss();

    }

    @SuppressLint("ResourceAsColor")
    private LinearLayout createNewLinearLayout(String status){
        LinearLayout linearLayout = new LinearLayout(this);

        if (status.equals("pending")){
            currentFlag = 1;
            currentTaskLayout.addView(linearLayout);
        }
        else{
            completeFlag = 1;
            completedTaskLayout.addView(linearLayout);
        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );


        int marginHorizontal = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()
        );
        int marginVertical = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()
        );
        layoutParams.setMargins(marginHorizontal, marginVertical, marginHorizontal, marginVertical);

        linearLayout.setLayoutParams(layoutParams);

        // Set orientation and gravity
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);

        // Set padding
        int padding = (int) (20 * getResources().getDisplayMetrics().density);
        linearLayout.setPadding(padding, padding, padding, padding);

        // Set background color
        linearLayout.setBackgroundResource(R.drawable.rounded_background);


        return  linearLayout;

    }




    private TextView createTextView(String text) {
        TextView textView = new TextView(this);

        // Set text
        textView.setText(text);


        // Set layout parameters
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);

        // Set padding
        int paddingHorizontal = (int) (18 * getResources().getDisplayMetrics().density); // convert dp to pixels
        textView.setPadding(paddingHorizontal, 0, paddingHorizontal, 0);

        // Set text size and style
        textView.setTextSize(20);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setGravity(Gravity.CENTER);

        // Set text color to black
        textView.setTextColor(getResources().getColor(android.R.color.white));

        return textView;
    }

    private Button createMarkAsDoneButton(String taskId) {
        // Create the Button
        Button button = new Button(this);
        button.setText("Mark As Done");


        button.setLayoutParams(new TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));



        button.setTypeface(button.getTypeface(), Typeface.BOLD); // Set text style to bold
        button.setContentDescription(taskId);
        button.setGravity(Gravity.CENTER);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Mark as done here
            }
        });

        return button;
    }
}