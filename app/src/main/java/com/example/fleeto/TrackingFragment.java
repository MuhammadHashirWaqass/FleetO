package com.example.fleeto;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.geojson.Point;
import com.mapbox.maps.plugin.annotation.AnnotationType;
import com.mapbox.maps.CameraOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrackingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackingFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private MapView mapView;

    public TrackingFragment() {
        // Required empty public constructor
    }

    public static TrackingFragment newInstance(String param1, String param2) {
        TrackingFragment fragment = new TrackingFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tracking, container, false);

        // Initialize MapView
        mapView = view.findViewById(R.id.mapView);
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, style -> {
            // Check if the AnnotationPlugin is available
            AnnotationPlugin annotationApi = mapView.getPlugin("annotation");
            if (annotationApi != null) {
                // Add markers for each driver location
                List<Point> driverLocations = getDriverLocations();
                PointAnnotationManager pointAnnotationManager = (PointAnnotationManager) annotationApi.createAnnotationManager(AnnotationType.PointAnnotation, new AnnotationConfig());

                for (Point location : driverLocations) {
                    PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
                            .withPoint(location)
                            .withTextField("Driver Location");
                    pointAnnotationManager.create(pointAnnotationOptions);
                    Log.d("TrackingFragment", "Added marker at: " + location.toString());
                }

                if (!driverLocations.isEmpty()) {
                    mapView.getMapboxMap().setCamera(new CameraOptions.Builder()
                            .center(driverLocations.get(0))
                            .zoom(10.0)
                            .build());
                } else {
                    Log.d("TrackingFragment", "No driver locations to display.");
                }
            } else {
                // Handle the error appropriately (e.g., show a message or log an error)
                // For example:
                Log.e("TrackingFragment", "AnnotationPlugin not found.");
            }
        });

        return view;
    }


    private List<Point> getDriverLocations() {
        // Replace this with actual data retrieval logic
        List<Point> locations = new ArrayList<>();
        locations.add(Point.fromLngLat(151, -34));
        locations.add(Point.fromLngLat(150, -35));
        locations.add(Point.fromLngLat(149, -36));
        return locations;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onStop();
    }

}
