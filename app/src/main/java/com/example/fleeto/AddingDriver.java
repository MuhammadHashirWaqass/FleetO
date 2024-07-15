package com.example.fleeto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddingDriver extends AppCompatActivity {

    EditText DriverID, DriverN, DriverA,DriverVehicle,DriverPass;
    Button AddDbtn, back;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference Ref;
    FirebaseAuth auth;
    String adminEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_driver);

        DriverID = findViewById(R.id.AddingDriverID);
        DriverN = findViewById(R.id.AddingDriverName);
        DriverA = findViewById(R.id.AddingDriverAge);
        DriverVehicle= findViewById(R.id.AddingDriverVehicle);
        DriverPass = findViewById(R.id.AddingDriverPassword);
        AddDbtn = findViewById(R.id.ConfirmDriverBTN);
        back = findViewById(R.id.BackFromAddingDBTN);


        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            adminEmail = user.getEmail().replace(".", "_"); // Replace "." to avoid issues with Firebase keys
            Ref = database.getReference("Drivers").child(adminEmail);
        } else {
            Toast.makeText(getApplicationContext(), "Admin not logged in", Toast.LENGTH_SHORT).show();
            finish();
        }


        AddDbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnAddBTN(v);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void OnAddBTN(View v) {
        String driverID = DriverID.getText().toString();
        String driverNAME = DriverN.getText().toString();
        String driverAGE = DriverA.getText().toString();
        String driverPASS = DriverPass.getText().toString();

        if (!driverID.isEmpty() && !driverNAME.isEmpty() && !driverAGE.isEmpty() && !driverPASS.isEmpty()) {
            Ref.child(driverID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(getApplicationContext(), "The Driver ID already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        Driver driv = new Driver(driverID, driverNAME, driverAGE, driverPASS);
                        addDriver(driv);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Database error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Please fill out ALL the fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void addDriver(Driver driv) {
        Ref.child(driv.getDriverID()).setValue(driv).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Driver is successfully registered", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Driver failed to register", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
