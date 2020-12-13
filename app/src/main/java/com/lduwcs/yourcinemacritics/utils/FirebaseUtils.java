package com.lduwcs.yourcinemacritics.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lduwcs.yourcinemacritics.models.apiModels.Movie;
import com.lduwcs.yourcinemacritics.models.firebaseModels.Comment;
import com.lduwcs.yourcinemacritics.utils.listeners.FireBaseUtilsAddFavoriteListener;
import com.lduwcs.yourcinemacritics.utils.listeners.FireBaseUtilsCommentListener;
import com.lduwcs.yourcinemacritics.utils.listeners.FireBaseUtilsFavoriteMoviesListener;
import com.lduwcs.yourcinemacritics.utils.listeners.FireBaseUtilsRemoveFavoriteListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FirebaseUtils {

    static FireBaseUtilsCommentListener fireBaseUtilsCommentListener;
    static FireBaseUtilsFavoriteMoviesListener fireBaseUtilsFavoriteMoviesListener;
    static FireBaseUtilsAddFavoriteListener fireBaseUtilsAddFavoriteListener;
    static FireBaseUtilsRemoveFavoriteListener fireBaseUtilsRemoveFavoriteListener;
    private static FirebaseUtils instance;

    private String userId = "";
    public HashMap<Integer, Boolean> hashMapFavorite;
    private ArrayList<Movie> movies;


    public static FirebaseUtils getInstance() {
        if (instance == null) {
            instance = new FirebaseUtils();
        }
        return instance;
    }

    public ArrayList<Movie> getMovies() {
        if (movies != null)
            return movies;
        else return new ArrayList<>();
    }

    private FirebaseUtils() {
        // getFavMovies(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        userId = FirebaseAuth.getInstance().getUid();
    }

    public void setFireBaseUtilsAddFavoriteListener(FireBaseUtilsAddFavoriteListener fireBaseUtilsAddFavoriteListener) {
        FirebaseUtils.fireBaseUtilsAddFavoriteListener = fireBaseUtilsAddFavoriteListener;
    }

    public void setFireBaseUtilsRemoveFavoriteListener(FireBaseUtilsRemoveFavoriteListener fireBaseUtilsRemoveFavoriteListener) {
        FirebaseUtils.fireBaseUtilsRemoveFavoriteListener = fireBaseUtilsRemoveFavoriteListener;
    }

    public void setFireBaseUtilsFavoriteMoviesListener(FireBaseUtilsFavoriteMoviesListener fireBaseUtilsFavoriteMoviesListener) {
        FirebaseUtils.fireBaseUtilsFavoriteMoviesListener = fireBaseUtilsFavoriteMoviesListener;
    }

    public void setFireBaseUtilsCommentListener(FireBaseUtilsCommentListener fireBaseUtilsCommentListener) {
        FirebaseUtils.fireBaseUtilsCommentListener = fireBaseUtilsCommentListener;
    }

    public void writeComment(String userId, String movieId, String email, String content, String date, Float rating) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("movies");
        DatabaseReference commentRef = rootRef.child(movieId);
        Comment comment = new Comment(email, content, rating, date);
        commentRef.child(userId).setValue(comment);
    }

    public ArrayList<Comment> getComments(String movieId) {
        ArrayList<Comment> listComment = new ArrayList<>();
        Comment comment;
        comment = new Comment();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("movies");
        DatabaseReference commentRef = rootRef.child(movieId);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Comment> comments = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String content = ds.child("content").getValue(String.class);
                    String date = ds.child("date").getValue(String.class);
                    String email = ds.child("email").getValue(String.class);
                    Float rating = ds.child("rating").getValue(Float.class);
                    comments.add(new Comment(email, content, rating, date));
                }
                ;
                fireBaseUtilsCommentListener.onGetCommentDone(comments);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fireBaseUtilsCommentListener.onGetCommentError(databaseError.getMessage());
            }
        };
        commentRef.addListenerForSingleValueEvent(eventListener);
        return listComment;
    }

    public void addToFavMovies(int position, String userId, Movie movie, View view) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference userRef = rootRef.child(userId);
        DatabaseReference movieRef = userRef.child("" + movie.getId());
        ArrayList<Integer> genres = new ArrayList<>();
        genres.addAll(movie.getGenres());
        String genresConvert = "";
        genresConvert = "" + genres.get(0);
        for (int i = 1; i < genres.size(); i++) {
            genresConvert += "," + genres.get(i);
        }
        movieRef.child("title").setValue(movie.getTitle());
        movieRef.child("release_day").setValue(movie.getReleaseDay());
        movieRef.child("genres").setValue(genresConvert);
        movieRef.child("poster").setValue(movie.getPosterPath());
        movieRef.child("vote_average").setValue(movie.getVoteAverage());
        movieRef.child("over_view").setValue(movie.getOverview());
        addFavMovie(movie);
        fireBaseUtilsAddFavoriteListener.onSuccess(position, view);
    }

    public void deleteFromFavMovie(String userId, int id, int position, View view) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference userRef = rootRef.child(userId);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals("" + id)) {
                        ds.getRef().removeValue();
                        removeFavMovie(id);
                        fireBaseUtilsRemoveFavoriteListener.onSuccess(position, view);
                        return;
                    }
                }
                ;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fireBaseUtilsRemoveFavoriteListener.onError(databaseError.getMessage());
            }
        };
        userRef.addListenerForSingleValueEvent(eventListener);
    }

    private void removeFavMovie(int id) {
        if (movies != null) {
            hashMapFavorite.remove(id);
            for (Movie movie : movies) {
                if (movie.getId() == id) {
                    movies.remove(movie);
                    return;
                }
            }
        }
    }

    private void addFavMovie(Movie movie) {
        if (movies != null) {
            hashMapFavorite.put(movie.getId(), true);
            movies.add(movie);
        }
    }

    public void getFavMovies(String userId) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference userRef = rootRef.child(userId);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                hashMapFavorite = new HashMap<Integer, Boolean>();
                movies = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id;
                    id = ds.getKey();
                    int movieId = Integer.parseInt(id);
                    String genres = ds.child("genres").getValue(String.class);
                    List<String> arrayList = new ArrayList<String>(Arrays.asList(genres.split(",")));
                    ArrayList<Integer> genresList = new ArrayList<Integer>();
                    for (String g : arrayList) {
                        genresList.add(Integer.parseInt(g.trim()));
                    }
                    String overView = ds.child("over_view").getValue(String.class);
                    String poster = ds.child("poster").getValue(String.class);
                    String releaseDay = ds.child("release_day").getValue(String.class);
                    String title = ds.child("title").getValue(String.class);
                    Double voteAverage = ds.child("vote_average").getValue(Double.class);
                    Movie movie = new Movie(movieId, title, releaseDay, genresList, poster, overView, voteAverage);

                    movies.add(movie);
                    hashMapFavorite.put(movie.getId(), true);
                }
                fireBaseUtilsFavoriteMoviesListener.onGetFavoriteDone();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG111", "err");
            }
        };
        userRef.addListenerForSingleValueEvent(eventListener);
        //}
    }

    public boolean isFavoriteMovie(int id) {
        if (hashMapFavorite != null) return hashMapFavorite.containsKey(id);
        return false;
    }

}
