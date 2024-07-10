package com.example.fleeto;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminDash extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button DMbtn, VFbtn, FMbtn, signoutBTN, TaskBTN;
    TextView DISPadmin;
    DatabaseReference Ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();

        DMbtn = findViewById(R.id.DMbtn);
        FMbtn = findViewById(R.id.FMbtn);
        DISPadmin = findViewById(R.id.DISPadmin);
        TaskBTN = findViewById(R.id.TaskBTN);
        signoutBTN = findViewById(R.id.signoutBTN);
        Ref = FirebaseDatabase.getInstance().getReference("Admins");

        String adminName = mAuth.getCurrentUser().getEmail();
        System.out.println("ADMIN NAME: "+ adminName);
        DISPadmin.setText(adminName);

        DMbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dm = new Intent(getApplicationContext(), AddingDriver.class);
                startActivity(dm);
            }
        });

//        VFbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // Intent vf = new Intent(getApplicationContext(), VehicleFleet.class);
//               // startActivity(vf);
//            }
//        });

//        FMbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // Intent fm = new Intent(getApplicationContext(), FleetMap.class);
//               // startActivity(fm);
//            }
//        });

//        signoutBTN.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences.Editor edit = getSharedPreferences("loginDetails", MODE_PRIVATE).edit();
//                edit.clear();
//                edit.apply();
//
//                Intent signout = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(signout);
//                finish();
//                Toast.makeText(getApplicationContext(), "Signed out", Toast.LENGTH_SHORT).show();
//            }
//        });

//        TaskBTN.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//              //  Intent t = new Intent(getApplicationContext(), TaskC.class);
//               // startActivity(t);
//            }
//        });
    }
    private String getAdminName() {
        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Admin admin = snapshot.getValue(Admin.class);
                    // if (admin != null) {
                    //    DISPadmin.setText(admin.name);
                        return;
                    //}
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return null;
    }
}