package com.example.fleeto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
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
import com.android.volley.toolbox.JsonArrayRequest;
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

        progressDialog.show();
        getDriversOfOwner();
        progressDialog.dismiss();

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





    public void getDriversOfOwner (){
        RequestQueue queue = Volley.newRequestQueue(this.getContext());

        // Create JSON object for the request body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("userId", User.getUserInstance().userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest  = new JsonObjectRequest(Request.Method.POST, ApiPath.getInstance().getUrl() + "/api/driver/getDrivers", jsonBody,
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

    void populateDriverTable(JSONArray listOfDrivers){
        for (int i=0;i<listOfDrivers.length();i++){
            TableRow tableRow = new TableRow(this.getContext());

            try {
                // creating and setting textView Values
                TextView idTextView =  createTextView(listOfDrivers.getJSONObject(i).getString("driverId"));
                TextView nameTextView = createTextView(listOfDrivers.getJSONObject(i).getString("name"));
                TextView passwordTextView = createTextView(listOfDrivers.getJSONObject(i).getString("password"));
                TextView carTextView = createTextView(listOfDrivers.getJSONObject(i).getString("vehicle"));
                Button addTaskToDriverButton = createAddDriverButton(listOfDrivers.getJSONObject(i).getString("driverId"));

                tableRow.addView(idTextView);
                tableRow.addView(nameTextView);
                tableRow.addView(passwordTextView);
                tableRow.addView(carTextView);
                tableRow.addView(addTaskToDriverButton);

                int paddingVertical = (int) (10 * getResources().getDisplayMetrics().density);
                tableRow.setPadding(0,paddingVertical,0,paddingVertical);

            } catch (JSONException e) {
                Log.e("json", "HEHE:" +e.getMessage());
            }
            tableLayout.addView(tableRow);
        }

    }

    private Button createAddDriverButton (String contentDescription){
        // Create the Button
        Button button = new Button(this.getContext());
        button.setText("+");
        button.setBackgroundResource(R.drawable.button_drawable); // Set background drawable

            button.setLayoutParams(new TableRow.LayoutParams(
                    (int) getResources().getDisplayMetrics().scaledDensity * 20,
                    (int) getResources().getDisplayMetrics().scaledDensity * 20
            )) ;
        button.setPadding(0, 0, 0, 0); // Set horizontal padding in dp
        button.setTextSize(16); // Set text size to 16sp
        button.setTypeface(button.getTypeface(), Typeface.BOLD); // Set text style to bold
        button.setContentDescription(contentDescription);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DriverId.getInstance().setDriverId(Integer.parseInt(contentDescription));
                startActivity(new Intent(requireContext(), AddingTask.class));
            }
        });

        return button;
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(this.getContext());

        // Set text
        textView.setText(text);

        // Set layout parameters
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
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
}
