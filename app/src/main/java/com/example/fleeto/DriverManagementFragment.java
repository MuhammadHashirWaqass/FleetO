package com.example.fleeto;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverManagementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverManagementFragment extends Fragment {
    JSONArray listOfDrivers;
    TableLayout tableLayout;
    ProgressDialog progressDialog;
    Button addDriverButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DriverManagementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment driver_management.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverManagementFragment newInstance(String param1, String param2) {
        DriverManagementFragment fragment = new DriverManagementFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_driver_management, container, false);
        progressDialog = new ProgressDialog(this.getContext());
        progressDialog.setMessage("Loading Drivers");
        tableLayout = view.findViewById(R.id.driver_table_layout);
        listOfDrivers = new JSONArray();
        addDriverButton = view.findViewById(R.id.add_driver_btn);


        getDriversOfOwner();

        addDriverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), AddingDriver.class);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void getDriversOfOwner() {
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this.getContext());

        // Create JSON object for the request body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("userId", User.getUserInstance().userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                ApiPath.getInstance().getUrl() + "/api/driver/getDrivers", jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            listOfDrivers = response.getJSONArray("data");
                            populateDriverTable(listOfDrivers);
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

    void populateDriverTable(JSONArray listOfDrivers) {
        if (listOfDrivers.length() == 0){
            TextView noItemTextView = new TextView(requireContext());
            noItemTextView.setText("No Drivers to display");
            noItemTextView.setGravity(Gravity.CENTER);
            noItemTextView.setTextSize(20);
            noItemTextView.setTextAppearance(Typeface.BOLD);
            noItemTextView.setTextColor(getResources().getColor(android.R.color.black));
            noItemTextView.setPadding(0,10,0,0);
            tableLayout.addView(noItemTextView);
            progressDialog.dismiss();
            return;
        }
        for (int i = 0; i < listOfDrivers.length(); i++) {
            // Parent Linear Layout
            LinearLayout parentLinearLayout = createParentLinearLayout();
            LinearLayout infoLinearLayout = createInfoLinearLayout();
            LinearLayout buttonsLinearLayout = createButtonsLinearLayout();
            parentLinearLayout.addView(infoLinearLayout);
            parentLinearLayout.addView(buttonsLinearLayout);


            try {
                // creating and setting textView Values
                TextView idTextView = createTextView("Driver ID: " + listOfDrivers.getJSONObject(i).getString("driverId"));
                TextView nameTextView = createTextView("Name: "+listOfDrivers.getJSONObject(i).getString("name"));
                TextView passwordTextView = createTextView("Password: "+listOfDrivers.getJSONObject(i).getString("password"));
                TextView carTextView = createTextView("Vehicle: " + listOfDrivers.getJSONObject(i).getString("vehicle"));

                Button addTaskToDriverButton = createAddTaskToDriverButton(
                        listOfDrivers.getJSONObject(i).getString("driverId"));

                String id = listOfDrivers.getJSONObject(i).getString("driverId");
                String name = listOfDrivers.getJSONObject(i).getString("name");
                String age = listOfDrivers.getJSONObject(i).getString("age");
                String password=listOfDrivers.getJSONObject(i).getString("password");
                String vehicle = listOfDrivers.getJSONObject(i).getString("vehicle");
                String license = listOfDrivers.getJSONObject(i).getString("licenseNumber");; //TODO: add here

                Button viewDriverDetailsButton = createViewDriverDetailsButton(id, name, age, password, vehicle, license);
                Button deleteDriverButton = createDeleteDriverButton(listOfDrivers.getJSONObject(i).getString("driverId"));

                // adding info
                infoLinearLayout.addView(idTextView);
                infoLinearLayout.addView(nameTextView);
//                infoLinearLayout.addView(passwordTextView);
                infoLinearLayout.addView(carTextView);

//                adding buttons
                buttonsLinearLayout.addView(addTaskToDriverButton);
                buttonsLinearLayout.addView(viewDriverDetailsButton);
                buttonsLinearLayout.addView(deleteDriverButton);



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

    private Button createAddTaskToDriverButton(String contentDescription) {
        // Create the Button
        Button button = new Button(this.getContext());
        button.setText("Add Task");


        button.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
                        ));
        button.setPadding(0, 0, 0, 0); // Set horizontal padding in dp
        button.setTextSize(16); // Set text size to 16sp
        button.setTypeface(button.getTypeface(), Typeface.BOLD); // Set text style to bold
        button.setContentDescription(contentDescription);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Global.getInstance().setDriverId(Integer.parseInt(contentDescription));
                startActivity(new Intent(requireContext(), AddingTask.class));
            }
        });

        return button;
    }

    private Button createViewDriverDetailsButton(String driverId, String name, String age, String password, String vehicle, String license){
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
        button.setContentDescription(driverId);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(requireContext());
                dialog.setContentView(R.layout.customdialog_driver);
                dialog.show();
                TextView DriverId = dialog.findViewById(R.id.PopupDriverId);
                TextView t1 = dialog.findViewById(R.id.PopupDriverName);
                TextView t2 = dialog.findViewById(R.id.PopupDriverAge);
                TextView t3 = dialog.findViewById(R.id.PopupDriverPassword);
                TextView t4 = dialog.findViewById(R.id.PopupDriverVehicle);
                TextView t5 = dialog.findViewById(R.id.PopupDriverLicenseNumber);


                Button b1 = dialog.findViewById(R.id.btn_okd);
                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                DriverId.setText(driverId);
                t1.setText(name);
                t2.setText(age);
                t3.setText(password);
                t4.setText(vehicle);
                t5.setText(license);
            }
        });

        return button;
    }

    private Button createDeleteDriverButton (String driverId){
        // Create the Button
        Button button = new Button(this.getContext());
        button.setText("Delete");


        button.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        button.setPadding(0, 0, 0, 0); // Set horizontal padding in dp
        button.setTextSize(16); // Set text size to 16sp
        button.setTypeface(button.getTypeface(), Typeface.BOLD); // Set text style to bold
        button.setContentDescription(driverId);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Delete Driver");
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    // Handle the positive button click
                    progressDialog.setMessage("Deleting Task");

                    deleteDriver(Integer.parseInt(driverId));

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

    private void deleteDriver (int driverId){
        progressDialog.setMessage("Deleting Driver...");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        // Create JSON object for the request body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("driverId", driverId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                ApiPath.getInstance().getUrl() + "/api/driver/delete", jsonBody,
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
        textView.setGravity(Gravity.CENTER);

        // Set text color to black
        textView.setTextColor(getResources().getColor(android.R.color.white));

        return textView;
    }
}
