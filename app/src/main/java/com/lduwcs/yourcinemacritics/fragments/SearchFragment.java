package com.lduwcs.yourcinemacritics.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.adapters.HomeAdapter;
import com.lduwcs.yourcinemacritics.adapters.SearchAdapter;
import com.lduwcs.yourcinemacritics.models.apiModels.Movie;
import com.lduwcs.yourcinemacritics.models.roomModels.AppDatabase;
import com.lduwcs.yourcinemacritics.models.roomModels.MoviesDao;
import com.lduwcs.yourcinemacritics.utils.ApiUtils;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private static final String TAG = "DEBUG1";

    private RecyclerView searchRecView;
    static ArrayList<Movie> movies;
    private SearchView searchView;

    private ApiUtils utils;

    @SuppressLint("StaticFieldLeak")
    static public SearchAdapter adapter;

    public SearchFragment() {
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
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchRecView = view.findViewById(R.id.favoriteRecView);
        searchView = view.findViewById(R.id.edtSearch);

        utils = new ApiUtils(getContext());
        movies = new ArrayList<>();

        adapter = new SearchAdapter(view.getContext(), movies);
        searchRecView.setAdapter(adapter);
        searchRecView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                utils.getAllMovies(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    static public void onSearchingDone(ArrayList<Movie> data, Context context) {
        movies.clear();
        movies.addAll(data);
        adapter.notifyDataSetChanged();
        Log.d(TAG, "onSearchingDone: success");
    }

}