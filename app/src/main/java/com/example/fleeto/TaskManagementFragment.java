package com.example.fleeto;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import androidx.fragment.app.FragmentController;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
        RequestQueue queue = Volley.newRequestQueue(requireContext());

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
        tableLayout.removeAllViews();
        if (listOfTasks.length() == 0){
            TextView noItemTextView = new TextView(requireContext());
            noItemTextView.setText("No Tasks to display");
            noItemTextView.setGravity(Gravity.CENTER);
            noItemTextView.setTextSize(20);
            noItemTextView.setTextAppearance(Typeface.BOLD);
            noItemTextView.setTextColor(getResources().getColor(android.R.color.black));
            noItemTextView.setPadding(0,10,0,0);
            tableLayout.addView(noItemTextView);
            progressDialog.dismiss();
            return;
        }
        for (int i = 0; i < listOfTasks.length(); i++) {

            // Parent Linear Layout
            LinearLayout parentLinearLayout = createParentLinearLayout();
            LinearLayout infoLinearLayout = createInfoLinearLayout();
            LinearLayout buttonsLinearLayout = createButtonsLinearLayout();
            parentLinearLayout.addView(infoLinearLayout);
            parentLinearLayout.addView(buttonsLinearLayout);

            try {
                // creating and setting textView Values
                TextView idTextView = createTextView("Task ID: "+listOfTasks.getJSONObject(i).getString("taskId"));
                TextView titleTextView = createTextView("Title: "+listOfTasks.getJSONObject(i).getString("title"));
                TextView descriptionTextView = createTextView("Description: "+listOfTasks.getJSONObject(i).getString("description"));
                TextView addressTextView = createTextView("Address: "+listOfTasks.getJSONObject(i).getString("address"));
                TextView driverIdTextView = createTextView("Driver ID: "+listOfTasks.getJSONObject(i).getString("driverId"));
                String status = listOfTasks.getJSONObject(i).getString("status");
                status = status.substring(0, 1).toUpperCase() + status.substring(1);
                TextView statusTextView = createTextView("Status: "+ status);

                String id = listOfTasks.getJSONObject(i).getString("taskId");
                String title = listOfTasks.getJSONObject(i).getString("title");
                String description =listOfTasks.getJSONObject(i).getString("description");
                String address = listOfTasks.getJSONObject(i).getString("address");
                String driverId = listOfTasks.getJSONObject(i).getString("driverId");

                Button editTaskButton = createEditTaskButton(
                        listOfTasks.getJSONObject(i).getString("taskId"), listOfTasks.getJSONObject(i).getString("title"), listOfTasks.getJSONObject(i).getString("description"), listOfTasks.getJSONObject(i).getString("address"));

                Button deleteTaskButton = createDeleteTaskButton(
                        listOfTasks.getJSONObject(i).getString("taskId"));

                Button viewTaskDetailsButton = createViewTaskDetailsButton(id, title, description, address, driverId, status);

                // adding textviews to layout
//                infoLinearLayout.addView(idTextView);
                infoLinearLayout.addView(titleTextView);
//                infoLinearLayout.addView(descriptionTextView);
//                infoLinearLayout.addView(addressTextView);
                infoLinearLayout.addView(driverIdTextView);
                infoLinearLayout.addView(statusTextView);

                // adding buttons to layout
                buttonsLinearLayout.addView(editTaskButton);
                buttonsLinearLayout.addView(viewTaskDetailsButton);
                buttonsLinearLayout.addView(deleteTaskButton);

            } catch (JSONException e) {
                Log.e("json", "HEHE:" + e.getMessage());
            }
        }
        progressDialog.dismiss();

    }

    @SuppressLint("ResourceAsColor")
    private LinearLayout createParentLinearLayout(){
        LinearLayout linearLayout = new LinearLayout(requireContext());
        tableLayout.addView(linearLayout);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );


        int marginBottom = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()
        );
        layoutParams.setMargins(0, marginBottom, 0, marginBottom);

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

    private LinearLayout createInfoLinearLayout(){
        LinearLayout linearLayout = new LinearLayout(this.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        linearLayout.setLayoutParams(layoutParams);

        // Set orientation and gravity
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        // Set padding
        int padding = (int) (10 * getResources().getDisplayMetrics().density);
        linearLayout.setPadding(0, padding, 0, padding);

        return  linearLayout;
    }

    private LinearLayout createButtonsLinearLayout(){
        LinearLayout linearLayout = new LinearLayout(this.getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        linearLayout.setLayoutParams(layoutParams);

        // Set orientation and gravity
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);

        return  linearLayout;
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
        textView.setTextSize(18);
        textView.setTypeface(null, Typeface.BOLD);

        // Set text color to black
        textView.setTextColor(getResources().getColor(android.R.color.white));

        return textView;
    }

    private Button createEditTaskButton(String taskId, String title, String description, String address) {
        // Create the Button
        Button button = new Button(this.getContext());
        button.setText("Edit");


        button.setLayoutParams(new TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        button.setPadding(0, 0, 0, 0); // Set horizontal padding in dp
        button.setTextSize(16); // Set text size to 16sp
        button.setTypeface(button.getTypeface(), Typeface.BOLD); // Set text style to bold
        button.setContentDescription(taskId);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Edit Task Activity here
                Dialog dialog = new Dialog(requireContext());
                dialog.setContentView(R.layout.customdialog_edittask);
                dialog.show();
                TextView TaskId = dialog.findViewById(R.id.PopupEditTaskId);
                EditText t1 = dialog.findViewById(R.id.EditTaskTitle);
                EditText t2 = dialog.findViewById(R.id.EditTaskDescription);
                EditText t3 = dialog.findViewById(R.id.EditTaskAddress);
                Button b1 = dialog.findViewById(R.id.btn_yes);
                Button b2 = dialog.findViewById(R.id.btn_no);

                TaskId.setText("Task ID: "+taskId);
                t1.setText(title);
                t2.setText(description);
                t3.setText(address);

                b2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // check if any value was changed
                        String newTitle = t1.getText().toString().trim();
                        String newDescription = t2.getText().toString().trim();
                        String newAddress = t3.getText().toString().trim();

                        if (title.equals(newTitle) && description.equals(newDescription) && address.equals(newAddress)){
                            dialog.dismiss();
                            return;
                        }

                        // edit the task
                        editTask(Integer.parseInt(taskId), newTitle, newDescription, newAddress);
                    }
                });
            }
        });

        return button;
    }

    private void editTask(int taskId, String title, String description, String address){
        progressDialog.setMessage("Editing Task...");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        // Create JSON object for the request body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("taskId", taskId);
            jsonBody.put("title", title);
            jsonBody.put("description", description);
            jsonBody.put("address", address);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                ApiPath.getInstance().getUrl() + "/api/task/update", jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(requireContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            startActivity(new Intent(requireContext(), AdminDash.class));
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

    private Button createViewTaskDetailsButton(String id, String title, String description,String address, String driverId,String status){
        // Create the Button
        Button button = new Button(this.getContext());
        button.setText("Details");


        button.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        button.setPadding(0, 0, 0, 0); // Set horizontal padding in dp
        button.setTextSize(16); // Set text size to 16sp
        button.setTypeface(button.getTypeface(), Typeface.BOLD); // Set text style to bold
        button.setContentDescription(id);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(requireContext());
                dialog.setContentView(R.layout.customdialog_task);
                dialog.show();
                TextView TaskId = dialog.findViewById(R.id.PopupTaskId);
                TextView t1 = dialog.findViewById(R.id.PopupTaskTitle);
                TextView t2 = dialog.findViewById(R.id.PopupTaskDescription);
                TextView t3 = dialog.findViewById(R.id.PopupTaskAddress);
                TextView DriverId = dialog.findViewById(R.id.PopupTaskDriverId);
                TextView Status = dialog.findViewById(R.id.PopupTaskStatus);

                Button b1 = dialog.findViewById(R.id.btn_ok);
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                TaskId.setText(id);
                t1.setText(title);
                t2.setText(description);
                t3.setText(address);
                DriverId.setText(driverId);
                Status.setText(status);
            }
        });

        return button;
    }

    private Button createDeleteTaskButton(String taskId) {
        // Create the Button
        Button button = new Button(this.getContext());
        button.setText("Delete");

        button.setLayoutParams(new TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
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
                            progressDialog.setMessage("Loading Tasks");
                            getTasksOfOwner();

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
