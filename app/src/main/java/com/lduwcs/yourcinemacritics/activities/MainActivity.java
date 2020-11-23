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
    static MoviesDao moviesDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        utils = new ApiUtils(this);

        AppDatabase appDatabase= Room.databaseBuilder(this,AppDatabase.class,"yourmoviecriticsdb")
                .allowMainThreadQueries()
                .build();
        moviesDao = appDatabase.getMoviesDao();

        reloadFavorites();
    }

    private void reloadFavorites(){
        moviesDao.deleteAll();
        utils.getTrending();
    }

    static public void onLoadFavoritesDone(ArrayList<Movie> data, Context context){
        movies = new ArrayList<Movie>();
        movies.addAll(data);
        for (Movie movie : data) {
            moviesDao.insert(movie);
        }
        Log.d(TAG, "onLoadFavoritesDone: success");
    }
}