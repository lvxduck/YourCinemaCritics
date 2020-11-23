package com.lduwcs.yourcinemacritics.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.models.apiModels.Movie;
import com.lduwcs.yourcinemacritics.models.roomModels.AppDatabase;
import com.lduwcs.yourcinemacritics.models.roomModels.MoviesDao;
import com.lduwcs.yourcinemacritics.utils.ApiUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DEBUG1";
    ApiUtils utils;
    static ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        utils = new ApiUtils(this);
        Log.d(TAG, "onCreate: ");
        utils.getAllMovies("avenger");
//        utils.getTrailer("299534");
//        utils.getTrending();
    }

    static public void onLoadMovieDone(ArrayList<Movie> data, Context context){
        movies = new ArrayList<Movie>();
        movies.addAll(data);
        AppDatabase appDatabase= Room.databaseBuilder(context,AppDatabase.class,"yourmoviecriticsdb")
                .allowMainThreadQueries()
                .build();
        MoviesDao moviesDao = appDatabase.getMoviesDao();


        moviesDao.insert(movies.get(0));
        moviesDao.insert(movies.get(1));
        moviesDao.insert(movies.get(2));

        List<Movie> movies2 = moviesDao.getAllFavorites();
        Log.d(TAG, "onCreate: "+movies2);
    }
}