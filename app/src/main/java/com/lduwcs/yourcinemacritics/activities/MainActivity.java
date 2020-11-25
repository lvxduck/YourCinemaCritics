package com.lduwcs.yourcinemacritics.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.fragments.FavoriteFragment;
import com.lduwcs.yourcinemacritics.fragments.HomeFragment;
import com.lduwcs.yourcinemacritics.fragments.ProfileFragment;
import com.lduwcs.yourcinemacritics.fragments.SearchFragment;
import com.lduwcs.yourcinemacritics.models.apiModels.Movie;
import com.lduwcs.yourcinemacritics.models.firebaseModels.Comment;
import com.lduwcs.yourcinemacritics.models.roomModels.AppDatabase;
import com.lduwcs.yourcinemacritics.models.roomModels.MoviesDao;
import com.lduwcs.yourcinemacritics.uiComponents.BotNavBar;
import com.lduwcs.yourcinemacritics.utils.ApiUtils;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "DEBUG1";
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() == null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            initBotNavBar();
            ArrayList<Movie> listFavMovies = new ArrayList<>();
            Movie movie;
            movie = new Movie();
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("user");
            DatabaseReference userRef = rootRef.child("user_id");
            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        String genres = ds.child("genres").getValue(String.class);
                        List<String> arrayList = new ArrayList<String>(Arrays.asList(genres.split(",")));
                        ArrayList<Integer> genresList = new ArrayList<Integer>();
                        for(String g:arrayList){
                            genresList.add(Integer.parseInt(g.trim()));
                        }
                        String overView = ds.child("over_view").getValue(String.class);
                        String poster = ds.child("poster").getValue(String.class);
                        String releaseDay = ds.child("release_day").getValue(String.class);
                        String title = ds.child("title").getValue(String.class);
                        Double voteAverage = ds.child("vote_average").getValue(Double.class);
                        movie.setGenres(genresList);
                        movie.setOverview(overView);
                        movie.setPosterPath(poster);
                        movie.setReleaseDay(releaseDay);
                        movie.setTitle(title);
                        movie.setVoteAverage(voteAverage);
                        Log.d(TAG, overView);
                        Log.d(TAG, genres);
                        Log.d(TAG, poster);
                        Log.d(TAG, "" + voteAverage);
                        listFavMovies.add(movie);
//                        Log.d(TAG, listFavMovies.get(0).getOverview());
                    };
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            };
            userRef.addListenerForSingleValueEvent(eventListener);

        }
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