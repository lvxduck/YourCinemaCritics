package com.lduwcs.yourcinemacritics.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    static public HomeAdapter adapter;

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

        //Swipe to refresh
        SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.swipe_to_refresh);
        pullToRefresh.setOnRefreshListener(() -> {
            reloadTrending();
            pullToRefresh.setRefreshing(false);
            Toast.makeText(getContext(), "Reloading", Toast.LENGTH_SHORT).show();
        });

        //Connect to database
        AppDatabase appDatabase = Room.databaseBuilder(getContext(), AppDatabase.class, "yourmoviecriticsdb")
                .allowMainThreadQueries()
                .build();
        moviesDao = appDatabase.getMoviesDao();

        utils = new ApiUtils(getContext());
        movies = new ArrayList<>();

        adapter = new HomeAdapter(view.getContext(), movies);
        homeRecView.setAdapter(adapter);
        homeRecView.setLayoutManager(new LinearLayoutManager(view.getContext(), RecyclerView.HORIZONTAL, false));

        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(homeRecView);

        //Load trending
        if(moviesDao.getTrending().size()>0){
            loadTrendingFromRoom();
        }else reloadTrending();
    }

    //Human_duck
    private void reloadTrending() {
        movies.clear();
        adapter.notifyDataSetChanged();
        moviesDao.deleteAll();
        utils.getTrending();
    }

    private void loadTrendingFromRoom(){
        movies.clear();
        movies.addAll(moviesDao.getTrending());
        adapter.notifyDataSetChanged();
    }

    static public void onLoadTrendingDone(ArrayList<Movie> data, Context context) {
        movies.clear();
        movies.addAll(data);
        for (Movie movie : data) {
            moviesDao.insert(movie);
        }
        adapter.notifyDataSetChanged();
        Log.d(TAG, "onLoadTrendingDone: success");
    }
}