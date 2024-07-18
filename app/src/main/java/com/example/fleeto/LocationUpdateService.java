//package com.example.fleeto;
//
//import android.Manifest;
//import android.app.Service;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.os.IBinder;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.ActivityCompat;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
//
//public class LocationUpdateService extends Service {
//
//    private FusedLocationProviderClient fusedLocationProviderClient;
//    private LocationCallback locationCallback;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//
//        LocationRequest locationRequest = LocationRequest.create();
//        locationRequest.setInterval(5000); // 5 seconds
//        locationRequest.setFastestInterval(2000); // 2 seconds
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(@NonNull LocationResult locationResult) {
//                if (locationResult == null) {
//                    return;
//                }
//
//                for (Location location : locationResult.getLocations()) {
//                    Log.d("LocationUpdateService", "Location: " + location.getLatitude() + ", " + location.getLongitude());
//                    // Send location to server
//                    sendLocationToServer(location);
//                }
//            }
//        };
//
//        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
//    }
//
//    private void sendLocationToServer(Location location) {
//        // Implement your logic to send the location to the server
//       String longis = String.valueOf(location.getLongitude());
//       String latis = String.valueOf(location.getLatitude());
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        return START_STICKY;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//}
