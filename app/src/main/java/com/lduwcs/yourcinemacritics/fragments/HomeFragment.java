package com.lduwcs.yourcinemacritics.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.room.Room;

import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.adapters.HomeAdapter;
import com.lduwcs.yourcinemacritics.models.apiModels.Movie;
import com.lduwcs.yourcinemacritics.models.roomModels.AppDatabase;
import com.lduwcs.yourcinemacritics.models.roomModels.MoviesDao;
import com.lduwcs.yourcinemacritics.utils.ApiUtils;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private static final String TAG = "DEBUG1";

    private RecyclerView homeRecView;
    static ArrayList<Movie> movies;

    private ApiUtils utils;
    static MoviesDao moviesDao;

    @SuppressLint("StaticFieldLeak")
    static HomeAdapter adapter;

    public HomeFragment() {
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        homeRecView = view.findViewById(R.id.homeRecView);

        utils = new ApiUtils(getContext());
        AppDatabase appDatabase = Room.databaseBuilder(getContext(), AppDatabase.class, "yourmoviecriticsdb")
                .allowMainThreadQueries()
                .build();
        moviesDao = appDatabase.getMoviesDao();

        movies = new ArrayList<>();
//        movies.add(new Movie(
//                299534,
//                "Avengers: Endgame",
//                "2019-04-24",
//                ints,
//                "/or06FN3Dka5tukK1e9sl16pB3iy.jpg",
//                "After the devastating events of Avengers: Infinity War, the universe is in ruins due to the efforts of the Mad Titan, Thanos. With the help of remaining allies, the Avengers must assemble once more in order to undo Thanos' actions and restore order to the universe once and for all, no matter what consequences may be in store.",
//                8.3
//        ));
//        movies.add(new Movie(
//                299534,
//                "Avengers: Endgame",
//                "2019-04-24",
//                ints,
//                "/aKx1ARwG55zZ0GpRvU2WrGrCG9o.jpg",
//                "After the devastating events of Avengers: Infinity War, the universe is in ruins due to the efforts of the Mad Titan, Thanos. With the help of remaining allies, the Avengers must assemble once more in order to undo Thanos' actions and restore order to the universe once and for all, no matter what consequences may be in store.",
//                8.3
//        ));
//        movies.add(new Movie(
//                299534,
//                "Avengers: Endgame",
//                "2019-04-24",
//                ints,
//                "/jlJ8nDhMhCYJuzOw3f52CP1W8MW.jpg",
//                "After the devastating events of Avengers: Infinity War, the universe is in ruins due to the efforts of the Mad Titan, Thanos. With the help of remaining allies, the Avengers must assemble once more in order to undo Thanos' actions and restore order to the universe once and for all, no matter what consequences may be in store.",
//                8.3
//        ));

        adapter = new HomeAdapter(view.getContext(), movies);
        homeRecView.setAdapter(adapter);
        homeRecView.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(homeRecView);

        //    reloadFavorites();
        loadFavoriteFromRoom();
    }

    //Human_duck
    private void reloadFavorites() {
        moviesDao.deleteAll();
        utils.getTrending();
    }

    private void loadFavoriteFromRoom(){
        movies.addAll(moviesDao.getAllFavorites());
    }

    static public void onLoadFavoritesDone(ArrayList<Movie> data, Context context) {
        movies = new ArrayList<>();
        movies.addAll(data);
        for (Movie movie : data) {
            moviesDao.insert(movie);
        }
        adapter.notifyDataSetChanged();
        Log.d(TAG, "onLoadFavoritesDone: success");
    }
}