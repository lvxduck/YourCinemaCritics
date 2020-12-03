package com.lduwcs.yourcinemacritics.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.MediaRouteButton;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.auth.FirebaseUser;
import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.activities.CommentActivity;
import com.lduwcs.yourcinemacritics.adapters.HomeAdapter;
import com.lduwcs.yourcinemacritics.adapters.SearchAdapter;
import com.lduwcs.yourcinemacritics.models.apiModels.Movie;
import com.lduwcs.yourcinemacritics.models.firebaseModels.Comment;
import com.lduwcs.yourcinemacritics.models.roomModels.AppDatabase;
import com.lduwcs.yourcinemacritics.models.roomModels.MoviesDao;
import com.lduwcs.yourcinemacritics.utils.ApiUtils;
import com.lduwcs.yourcinemacritics.utils.FirebaseUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchFragment extends Fragment {
    private static final String TAG = "DEBUG1";

    private RecyclerView searchRecView;
    static ArrayList<Movie> movies;
    private static TextView txtNoResult;
    private SearchView searchView;
    private CardView btnFilter;
    private CardView btnSort;
    private ArrayList<MaterialCheckBox> checkBoxes;
    private boolean isDescendingSorted = true;

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
        btnFilter = view.findViewById(R.id.btnFilter);
        btnSort = view.findViewById(R.id.btnSort);
        txtNoResult = view.findViewById(R.id.txtNoResult);
        checkBoxes = new ArrayList<MaterialCheckBox>();
//        MaterialCheckBox checkBox1 = view.findViewById(R.id.cb12);
//        MaterialCheckBox checkBox2 = view.findViewById(R.id.cb14);
//        MaterialCheckBox checkBox3 = view.findViewById(R.id.cb16);
//        MaterialCheckBox checkBox4 = view.findViewById(R.id.cb18);
//        MaterialCheckBox checkBox5 = view.findViewById(R.id.cb27);
//        MaterialCheckBox checkBox6 = view.findViewById(R.id.cb28);
//        MaterialCheckBox checkBox7 = view.findViewById(R.id.cb35);
//        MaterialCheckBox checkBox8 = view.findViewById(R.id.cb36);
//        MaterialCheckBox checkBox9 = view.findViewById(R.id.cb37);
//        MaterialCheckBox checkBox10 = view.findViewById(R.id.cb53);
//        MaterialCheckBox checkBox11 = view.findViewById(R.id.cb80);
//        MaterialCheckBox checkBox12 = view.findViewById(R.id.cb99);
//        MaterialCheckBox checkBox13 = view.findViewById(R.id.cb878);
//        MaterialCheckBox checkBox14 = view.findViewById(R.id.cb9648);
//        MaterialCheckBox checkBox15 = view.findViewById(R.id.cb10402);
//        MaterialCheckBox checkBox16 = view.findViewById(R.id.cb10749);
//        MaterialCheckBox checkBox17 = view.findViewById(R.id.cb10751);
//        MaterialCheckBox checkBox18 = view.findViewById(R.id.cb10752);
//        MaterialCheckBox checkBox19 = view.findViewById(R.id.cb10770);
//        checkBoxes.add(checkBox1);
//        checkBoxes.add(checkBox2);
//        checkBoxes.add(checkBox3);
//        checkBoxes.add(checkBox4);
//        checkBoxes.add(checkBox5);
//        checkBoxes.add(checkBox6);
//        checkBoxes.add(checkBox7);
//        checkBoxes.add(checkBox8);
//        checkBoxes.add(checkBox9);
//        checkBoxes.add(checkBox10);
//        checkBoxes.add(checkBox11);
//        checkBoxes.add(checkBox12);
//        checkBoxes.add(checkBox13);
//        checkBoxes.add(checkBox14);
//        checkBoxes.add(checkBox15);
//        checkBoxes.add(checkBox16);
//        checkBoxes.add(checkBox17);
//        checkBoxes.add(checkBox18);
//        checkBoxes.add(checkBox19);

        utils = new ApiUtils(getContext());
        movies = new ArrayList<>();

        adapter = new SearchAdapter(view.getContext(), movies);
        searchRecView.setAdapter(adapter);
        searchRecView.setLayoutManager(new GridLayoutManager(view.getContext(), 2));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                utils.getAllMovies(s);
                isDescendingSorted = true;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View layout= null;
                LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                layout = inflater.inflate(R.layout.filter, null);
                final ArrayList<MaterialCheckBox> checkedGenres = new ArrayList<>();
                builder.setTitle("Choose genres");
                builder.setView(layout);

                // Add the buttons
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //CANCELED
                    }
                });

                builder.show();
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Movie> sortedMovies = new ArrayList<Movie>();
                ArrayList<Movie> noReleaseDayMovies = new ArrayList<Movie>();
                int length = movies.size();
                for(int i = 0; i < length; i++){
                    Movie latestMovie = movies.get(0);
                    for(Movie movie : movies){
                        if(isDescendingSorted){
                            if(movie.getReleaseDay() != null && movie.getReleaseDay().compareTo(latestMovie.getReleaseDay()) > 0){
                                latestMovie = movie;
                            }
                        }
                        else{
                            if(movie.getReleaseDay() != null && movie.getReleaseDay().compareTo(latestMovie.getReleaseDay()) <= 0){
                                latestMovie = movie;
                            }
                        }
                    }
                    movies.remove(latestMovie);
                    sortedMovies.add(latestMovie);
                }
                noReleaseDayMovies.addAll(movies);
                movies.clear();
                movies.addAll(sortedMovies);
                movies.addAll(noReleaseDayMovies);
                adapter.notifyDataSetChanged();

                if(isDescendingSorted){
                    Toast.makeText(getContext(), "Sorted by Release Day, From Latest to Oldest", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Sorted by Release Day, From Oldest to Latest", Toast.LENGTH_SHORT).show();
                }

                isDescendingSorted = !isDescendingSorted;
            }
        });
    }

    public static void onSearchingDone(ArrayList<Movie> data, Context context) {
        movies.clear();
        movies.addAll(data);
        adapter.notifyDataSetChanged();
        txtNoResult.setVisibility(View.INVISIBLE);
        if(movies.size() == 0){
            txtNoResult.setVisibility(View.VISIBLE);
        }
        Log.d(TAG, "onSearchingDone: success");
    }

}