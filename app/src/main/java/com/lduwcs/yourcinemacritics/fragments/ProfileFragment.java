package com.lduwcs.yourcinemacritics.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.activities.LoginActivity;
import com.lduwcs.yourcinemacritics.activities.MainActivity;
import com.lduwcs.yourcinemacritics.activities.ProfileSettingActivity;
import com.lduwcs.yourcinemacritics.uiComponents.NeuButton;
import com.lduwcs.yourcinemacritics.utils.FirebaseUtils;
import com.lduwcs.yourcinemacritics.utils.listeners.FirebaseUtilsGetUserInfoListener;
import com.squareup.picasso.Picasso;

import java.io.File;


public class ProfileFragment extends Fragment {
    SwitchMaterial swDarkMode;
    private NeuButton btnLogout;
    private TextView txtProfileEmail;
    private ImageView imgAvatar;
    FirebaseUser user;
    FirebaseUtils firebaseUtils;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogout = view.findViewById(R.id.btnProfileLogout);
        txtProfileEmail = view.findViewById(R.id.txtProfileEmail);
        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUtils = FirebaseUtils.getInstance();
        if (user != null) {
            firebaseUtils.getUserInfo(user.getUid());
            firebaseUtils.setFirebaseUtilsGetUserNameListener(new FirebaseUtilsGetUserInfoListener() {
                @Override
                public void onGetNameDone(String name, String path) {
                    if(!name.isEmpty())
                    txtProfileEmail.setText(name);
                    else{
                        String email = user.getEmail();
                        txtProfileEmail.setText(email);
                    }
                    if(!path.isEmpty()){
                        Picasso.get()
                                .load(path)
                                .fit()
                                .into(imgAvatar);
                    }
                }
            });

        }

        btnLogout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Are you sure want to log out?");
            //Add buttons
            builder.setPositiveButton(R.string.yes, (dialog, id) -> {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getContext(), "Signed out!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
            builder.setNegativeButton(R.string.no, (dialog, id) -> {
            });
            builder.show();
        });

        swDarkMode = view.findViewById(R.id.btnDarkMode);
        if (MainActivity.isDarkMode)
            swDarkMode.setChecked(true);
        swDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (MainActivity.isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                MainActivity.editor.putBoolean("isDarkMode", false);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                MainActivity.editor.putBoolean("isDarkMode", true);
            }
            MainActivity.editor.apply();
        });

        imgAvatar = view.findViewById(R.id.imageView);
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileSettingActivity.class);
                startActivity(intent);
            }
        });
    }
}