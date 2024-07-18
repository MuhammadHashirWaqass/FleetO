package com.example.fleeto;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TaskManagementFragment extends Fragment {
    JSONArray listOfTasks;
    TableLayout tableLayout;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task_management, container, false);

        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setMessage("Loading Tasks");
        tableLayout = view.findViewById(R.id.task_table_layout);
        listOfTasks = new JSONArray();


        getTasksOfOwner();
        return view;
    }

    public void getTasksOfOwner() {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this.getContext());

        // Create JSON object for the request body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("ownerId", User.getUserInstance().userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                ApiPath.getInstance().getUrl() + "/api/task/getOwnerTasks", jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            listOfTasks = response.getJSONArray("data");
                            populateTaskTable(listOfTasks);
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

    public void populateTaskTable(JSONArray listOfTasks){
        for (int i = 0; i < listOfTasks.length(); i++) {
            TableRow tableRow = new TableRow(this.getContext());

            try {
                // creating and setting textView Values
                TextView idTextView = createTextView(listOfTasks.getJSONObject(i).getString("taskId"));
                TextView titleTextView = createTextView(listOfTasks.getJSONObject(i).getString("title"));
                TextView descriptionTextView = createTextView(listOfTasks.getJSONObject(i).getString("description"));
                TextView addressTextView = createTextView(listOfTasks.getJSONObject(i).getString("address"));
                TextView driverIdTextView = createTextView(listOfTasks.getJSONObject(i).getString("driverId"));
                TextView statusTextView = createTextView(listOfTasks.getJSONObject(i).getString("status"));

                Button editTaskButton = createEditTaskButton(
                        listOfTasks.getJSONObject(i).getString("taskId"));

                Button deleteTaskButton = createDeleteTaskButton(
                        listOfTasks.getJSONObject(i).getString("taskId"));

                tableRow.addView(idTextView);
                tableRow.addView(titleTextView);
                tableRow.addView(descriptionTextView);
                tableRow.addView(addressTextView);
                tableRow.addView(driverIdTextView);
                tableRow.addView(statusTextView);
                tableRow.addView(editTaskButton);
                tableRow.addView(deleteTaskButton);

                int paddingVertical = (int) (10 * getResources().getDisplayMetrics().density);
                tableRow.setPadding(0, paddingVertical, 0, paddingVertical);

            } catch (JSONException e) {
                Log.e("json", "HEHE:" + e.getMessage());
            }
            tableLayout.addView(tableRow);
        }
        progressDialog.dismiss();

    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this.getContext());

        // Set text
        textView.setText(text);

        // Set layout parameters
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(layoutParams);

        // Set padding
        int paddingHorizontal = (int) (18 * getResources().getDisplayMetrics().density); // convert dp to pixels
        textView.setPadding(paddingHorizontal, 0, paddingHorizontal, 0);

        // Set text size and style
        textView.setTextSize(16);
        textView.setTypeface(null, Typeface.BOLD);

        // Set text color to black
        textView.setTextColor(getResources().getColor(android.R.color.black));

        return textView;
    }

    private Button createEditTaskButton(String taskId) {
        // Create the Button
        Button button = new Button(this.getContext());
        button.setText("✏️");
        button.setBackgroundResource(R.drawable.button_drawable); // Set background drawable

        button.setLayoutParams(new TableRow.LayoutParams(
                (int) getResources().getDisplayMetrics().scaledDensity * 20,
                (int) getResources().getDisplayMetrics().scaledDensity * 20));
        button.setPadding(0, 0, 0, 0); // Set horizontal padding in dp
        button.setTextSize(16); // Set text size to 16sp
        button.setTypeface(button.getTypeface(), Typeface.BOLD); // Set text style to bold
        button.setContentDescription(taskId);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Edit Task Activity here
//                Global.getInstance().setDriverId(Integer.parseInt(contentDescription));
//                startActivity(new Intent(requireContext(), AddingTask.class));
            }
        });

        return button;
    }

    private Button createDeleteTaskButton(String taskId) {
        // Create the Button
        Button button = new Button(this.getContext());
        button.setText("🗑️");
        button.setBackgroundResource(R.drawable.button_drawable); // Set background drawable

        button.setLayoutParams(new TableRow.LayoutParams(
                (int) getResources().getDisplayMetrics().scaledDensity * 20,
                (int) getResources().getDisplayMetrics().scaledDensity * 20));
        button.setPadding(0, 0, 0, 0); // Set horizontal padding in dp
        button.setTextSize(16); // Set text size to 16sp
        button.setTypeface(button.getTypeface(), Typeface.BOLD); // Set text style to bold
        button.setContentDescription(taskId);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create and configure the AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Delete Task");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    // Handle the positive button click
                    progressDialog.setMessage("Deleting Task");

                    deleteTask(Integer.parseInt(taskId));

                });
                builder.setNegativeButton("No", (dialog, which) -> {
                    // Handle the negative button click

                });

                // Create and show the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        return button;
    }

    public void deleteTask(int taskId) {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this.getContext());

        // Create JSON object for the request body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("taskId", taskId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                ApiPath.getInstance().getUrl() + "/api/task/deleteTask", jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(requireContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



    }
}
