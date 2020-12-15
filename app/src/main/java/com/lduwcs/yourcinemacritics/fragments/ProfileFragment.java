package com.lduwcs.yourcinemacritics.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.activities.AboutActivity;
import com.lduwcs.yourcinemacritics.activities.LoginActivity;
import com.lduwcs.yourcinemacritics.activities.MainActivity;
import com.lduwcs.yourcinemacritics.activities.ProfileSettingActivity;
import com.lduwcs.yourcinemacritics.uiComponents.CustomProgressDialog;
import com.lduwcs.yourcinemacritics.uiComponents.NeuButton;
import com.lduwcs.yourcinemacritics.utils.FirebaseUtils;
import com.lduwcs.yourcinemacritics.utils.listeners.FirebaseUtilsGetUserInfoListener;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {
    private NeuButton btnLogout;
    private NeuButton btnEditProfile;
    private TextView txtProfileEmail, txtDarkModeContent;
    private ConstraintLayout btnDarkMode, btnAbout;
    private ImageView iconDarkMode;
    private ImageView imgAvatar;
    FirebaseUser user;
    FirebaseUtils firebaseUtils;
    private CustomProgressDialog mDialog;


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
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnDarkMode = view.findViewById(R.id.btnDarkMode);
        btnAbout = view.findViewById(R.id.btnAbout);
        txtProfileEmail = view.findViewById(R.id.txtProfileEmail);
        txtDarkModeContent = view.findViewById(R.id.txtDarkModeContent);
        iconDarkMode = view.findViewById(R.id.iconDarkMode);
        imgAvatar = view.findViewById(R.id.imageView);

        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUtils = FirebaseUtils.getInstance();
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

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAbout();
            }
        });

        mDialog = new CustomProgressDialog(getContext());


        if (MainActivity.isDarkMode) {
            iconDarkMode.setImageResource(R.drawable.ic_round_wb_sunny_24);
            txtDarkModeContent.setText(R.string.turn_on_light_mode);
        } else {
            iconDarkMode.setImageResource(R.drawable.ic_round_nights_stay_24);
            txtDarkModeContent.setText(R.string.turn_on_dark_mode);
        }


        btnDarkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.isDarkMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    MainActivity.editor.putBoolean("isDarkMode", false);
                    MainActivity.editor.apply();
                    iconDarkMode.setImageResource(R.drawable.ic_round_nights_stay_24);
                    txtDarkModeContent.setText(R.string.turn_on_dark_mode);

                    MainActivity.isDarkMode = false;
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    MainActivity.editor.putBoolean("isDarkMode", true);
                    MainActivity.editor.apply();
                    iconDarkMode.setImageResource(R.drawable.ic_round_wb_sunny_24);
                    txtDarkModeContent.setText(R.string.turn_on_light_mode);

                    MainActivity.isDarkMode = true;
                }
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileSettingActivity.class);
                startActivity(intent);
            }
        });


    }
    public void onResume() {
        Log.d("111", "onResume: ");
        super.onResume();
        if (user != null) {
            mDialog.show();
            firebaseUtils.getUserInfo(user.getUid(), null, null, 0);
            firebaseUtils.setFirebaseUtilsGetUserNameListener(new FirebaseUtilsGetUserInfoListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onGetNameDone(String name, String path, ImageView imageView, TextView textView, int position) {
                    if (!name.isEmpty())
                        txtProfileEmail.setText(name + "!");
                    else {
                        String email = user.getEmail();
                        txtProfileEmail.setText(email + "!");
                    }
                    if (!path.isEmpty()) {
                        Picasso.get()
                                .load(path)
                                .placeholder(R.drawable.spinner4)
                                .centerCrop()
                                .fit()
                                .into(imgAvatar);
                    }
                    mDialog.dismiss();
                }
            });
        }
    }

    private void onAbout() {
        Intent intent = new Intent(getContext(), AboutActivity.class);
        startActivity(intent);
    }
}