package com.lduwcs.yourcinemacritics.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.fragments.FavoriteFragment;
import com.lduwcs.yourcinemacritics.fragments.HomeFragment;
import com.lduwcs.yourcinemacritics.fragments.ProfileFragment;
import com.lduwcs.yourcinemacritics.fragments.SearchFragment;
import com.lduwcs.yourcinemacritics.models.apiModels.Movie;
import com.lduwcs.yourcinemacritics.models.roomModels.AppDatabase;
import com.lduwcs.yourcinemacritics.models.roomModels.MoviesDao;
import com.lduwcs.yourcinemacritics.uiComponents.BotNavBar;
import com.lduwcs.yourcinemacritics.utils.ApiUtils;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DEBUG1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        initBotNavBar();
    }



    //thuan_loki
    private void initBotNavBar() {
        BotNavBar botNavBar = findViewById(R.id.botNavBar);
        final Fragment[] fragment = new Fragment[1];
        fragment[0] = new HomeFragment();
        loadFragment(fragment[0]);
        botNavBar.setHomeOnClick(v -> {
            fragment[0] = new HomeFragment();
            loadFragment(fragment[0]);
        });
        botNavBar.setSearchOnClick(v -> {
            fragment[0] = new SearchFragment();
            loadFragment(fragment[0]);
        });
        botNavBar.setFavOnClick(v -> {
            fragment[0] = new FavoriteFragment();
            loadFragment(fragment[0]);
        });
        botNavBar.setProfileOnClick(v -> {
            fragment[0] = new ProfileFragment();
            loadFragment(fragment[0]);
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameContainer, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}