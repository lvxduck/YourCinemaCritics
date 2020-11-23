package com.lduwcs.yourcinemacritics.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.utils.ApiUtils;

public class MainActivity extends AppCompatActivity {
    ApiUtils utils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        utils = new ApiUtils();
        utils.getAllMovies("Avengers");
        utils.getTrailer("299534");
        utils.getTrending();
    }
}