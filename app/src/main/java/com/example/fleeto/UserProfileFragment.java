package com.example.fleeto;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class UserProfileFragment extends Fragment {

    TextView ProfileName, ProfileEmail;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    private FirebaseAuth mAuth;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        ProfileEmail = view.findViewById(R.id.AdminProfileEmail);
        ProfileName = view.findViewById(R.id.AdminProfileName);

        ProfileEmail.setText(User.getUserInstance().getEmail());
        ProfileName.setText(User.getUserInstance().getName());
        ProfileEmail.setTextColor(getResources().getColor(android.R.color.black));
        ProfileName.setTextColor(getResources().getColor(android.R.color.black));

        Button signOutButton = view.findViewById(R.id.SignOutBTNOwnerProfile);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        return view;
    }

    private void logoutUser() {
        mAuth.signOut();
        Intent intent = new Intent(requireActivity(), MainActivity.class);
        startActivity(intent);
        Toast.makeText(requireActivity(), "Logged Out Successfully", Toast.LENGTH_LONG).show();
    }
}