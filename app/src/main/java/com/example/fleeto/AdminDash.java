package com.example.fleeto;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class AdminDash extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Button DMbtn, VFbtn, FMbtn, signoutBTN, TaskBTN;
    TextView DISPadmin;
    DatabaseReference Ref;
    ProgressDialog progressDialog;

    BottomNavigationView navigationView;
    ActionBar actionBar;

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
        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        actionBar = getSupportActionBar(); // Initialize the actionBar correctly
        if (actionBar != null) {
            actionBar.setTitle("Profile");
        }
        navigationView.setSelectedItemId(R.id.nav_profile);
        UserProfileFragment fragment1 = new UserProfileFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content, fragment1, "");
        ft1.commit();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener selectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @SuppressLint("NonConstantResourceId")
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if(item.getItemId() == R.id.nav_profile) {
                        if (actionBar != null) {
                            actionBar.setTitle("Profile");
                        }
                        UserProfileFragment fragment1 = new UserProfileFragment();
                        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
                        ft1.replace(R.id.content, fragment1, "");
                        ft1.commit();
                        return true;
                    }
                    if(item.getItemId() == R.id.nav_tracker) {
                        if (actionBar != null) {
                            actionBar.setTitle("Tracking");
                        }
                        TrackingFragment fragment2 = new TrackingFragment();
                        FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                        ft2.replace(R.id.content, fragment2, "");
                        ft2.commit();
                        return true;
                    }

                    if(item.getItemId() == R.id.nav_drivers) {
                        if (actionBar != null) {
                            actionBar.setTitle("Drivers");
                        }
                        DriverManagementFragment fragment3 = new DriverManagementFragment();
                        FragmentTransaction ft3 = getSupportFragmentManager().beginTransaction();
                        ft3.replace(R.id.content, fragment3, "");
                        ft3.commit();
                        return true;
                    }
                    if(item.getItemId() == R.id.nav_tasks) {
                        if (actionBar != null) {
                            actionBar.setTitle("Tasks");
                        }
                        Intent intent = new Intent(AdminDash.this, AddingTask.class);
                        startActivity(intent);
                        return true;
                    }

                    else {
                        return false;
                    }
                }
            };

}
//        mAuth = FirebaseAuth.getInstance();
//
//        DMbtn = findViewById(R.id.DMbtn);
//        FMbtn = findViewById(R.id.FMbtn);
//        DISPadmin = findViewById(R.id.DISPadmin);
//        TaskBTN = findViewById(R.id.TaskBTN);
//        signoutBTN = findViewById(R.id.signoutBTN);
//        Ref = FirebaseDatabase.getInstance().getReference("Admins");
//
//        String adminName = mAuth.getCurrentUser().getEmail();
//        System.out.println("ADMIN NAME: "+ adminName);
//        DISPadmin.setText(adminName);
//
//        // logout user
//        signoutBTN.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                progressDialog = new ProgressDialog(AdminDash.this);
//                progressDialog.setMessage("Logging Out...");
//                logoutUser();
//            }
//        });
//
//        DMbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent dm = new Intent(getApplicationContext(), AddingDriver.class);
//                startActivity(dm);
//            }
//        });
//
////        VFbtn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////               // Intent vf = new Intent(getApplicationContext(), VehicleFleet.class);
////               // startActivity(vf);
////            }
////        });
//
////        FMbtn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////               // Intent fm = new Intent(getApplicationContext(), FleetMap.class);
////               // startActivity(fm);
////            }
////        });
//
////        signoutBTN.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                SharedPreferences.Editor edit = getSharedPreferences("loginDetails", MODE_PRIVATE).edit();
////                edit.clear();
////                edit.apply();
////
////                Intent signout = new Intent(getApplicationContext(), MainActivity.class);
////                startActivity(signout);
////                finish();
////                Toast.makeText(getApplicationContext(), "Signed out", Toast.LENGTH_SHORT).show();
////            }
////        });
//
////        TaskBTN.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////              //  Intent t = new Intent(getApplicationContext(), TaskC.class);
////               // startActivity(t);
////            }
////        });
//    }
//    private String getAdminName() {
//        Ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    //Admin admin = snapshot.getValue(Admin.class);
//                    // if (admin != null) {
//                    //    DISPadmin.setText(admin.name);
//                        return;
//                    //}
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        return null;
//    }
//
//    // function to logout owner
//    private void logoutUser() {
//        progressDialog.show();
//        mAuth.signOut();
//        progressDialog.dismiss();
//        Intent intent = new Intent(AdminDash.this, MainActivity.class);
//        startActivity(intent);
//        finish();
//        Toast.makeText(AdminDash.this,"Logged Out Successfully",Toast.LENGTH_LONG).show();
//
//    }
//}