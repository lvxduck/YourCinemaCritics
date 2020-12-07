package com.lduwcs.yourcinemacritics.utils;

import android.util.Log;
import android.view.View;

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
import java.util.List;

public class FirebaseUtils {

    static FireBaseUtilsCommentListener fireBaseUtilsCommentListener;
    static FireBaseUtilsFavoriteMoviesListener fireBaseUtilsFavoriteMoviesListener;
    static FireBaseUtilsAddFavoriteListener fireBaseUtilsAddFavoriteListener;
    static FireBaseUtilsRemoveFavoriteListener fireBaseUtilsRemoveFavoriteListener;

    public static void setFireBaseUtilsAddFavoriteListener(FireBaseUtilsAddFavoriteListener fireBaseUtilsAddFavoriteListener) {
        FirebaseUtils.fireBaseUtilsAddFavoriteListener = fireBaseUtilsAddFavoriteListener;
    }

    public static void setFireBaseUtilsRemoveFavoriteListener(FireBaseUtilsRemoveFavoriteListener fireBaseUtilsRemoveFavoriteListener) {
        FirebaseUtils.fireBaseUtilsRemoveFavoriteListener = fireBaseUtilsRemoveFavoriteListener;
    }

    public static void setFireBaseUtilsFavoriteMoviesListener(FireBaseUtilsFavoriteMoviesListener fireBaseUtilsFavoriteMoviesListener) {
        FirebaseUtils.fireBaseUtilsFavoriteMoviesListener = fireBaseUtilsFavoriteMoviesListener;
    }

    public static void setFireBaseUtilsCommentListener(FireBaseUtilsCommentListener fireBaseUtilsCommentListener) {
        FirebaseUtils.fireBaseUtilsCommentListener = fireBaseUtilsCommentListener;
    }

    public static void writeComment(String userId, String movieId, String email, String content, String date, Float rating){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("movies");
        DatabaseReference commentRef = rootRef.child(movieId);
        Comment comment = new Comment(email,content, rating, date);
        commentRef.child(userId).setValue(comment);
    }

    public static ArrayList<Comment> getComments(String movieId){
        ArrayList<Comment> listComment = new ArrayList<>();
        Comment comment;
        comment = new Comment();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("movies");
        DatabaseReference commentRef = rootRef.child(movieId);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Comment> comments = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String content = ds.child("content").getValue(String.class);
                    String date = ds.child("date").getValue(String.class);
                    String email = ds.child("email").getValue(String.class);
                    Float rating = ds.child("rating").getValue(Float.class);
                    comments.add(new Comment(email, content,rating,date));
                };
                //CommentActivity.getInstance().onGetCommentDone(comments);
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

    public static void addToFavMovies(int position, String userId, Movie movie, View view){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference userRef = rootRef.child(userId);
        DatabaseReference movieRef = userRef.child("" + movie.getId());
        ArrayList<Integer> genres = new ArrayList<>();
        genres.addAll(movie.getGenres());
        String genresConvert = "";
        genresConvert = "" + genres.get(0);
        for(int i=1; i< genres.size(); i++){
            genresConvert += "," + genres.get(i);
        }
        movieRef.child("title").setValue(movie.getTitle());
        movieRef.child("release_day").setValue(movie.getReleaseDay());
        movieRef.child("genres").setValue(genresConvert);
        movieRef.child("poster").setValue(movie.getPosterPath());
        movieRef.child("vote_average").setValue(movie.getVoteAverage());
        movieRef.child("over_view").setValue(movie.getOverview());
        fireBaseUtilsAddFavoriteListener.onSuccess(position, view);
    }

    public static void deleteFromFavMovie(String userId, int id,int position, View view){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference userRef = rootRef.child(userId);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.getKey().equals("" + id)){
                        ds.getRef().removeValue();
                        fireBaseUtilsRemoveFavoriteListener.onSuccess(position, view);
                        return;
                    }
                };
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                fireBaseUtilsRemoveFavoriteListener.onError(databaseError.getMessage());
            }
        };
        userRef.addListenerForSingleValueEvent(eventListener);
    }


    public static void getFavMovies(String userId) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference userRef = rootRef.child(userId);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Movie> listFavMovies = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String id;
                    id = ds.getKey();
                    int movieId = Integer.parseInt(id);
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
                    Movie movie = new Movie(movieId,title,releaseDay,genresList,poster,overView,voteAverage);
                    listFavMovies.add(new Movie(movieId,title,releaseDay,genresList,poster,overView,voteAverage));
                };
                fireBaseUtilsFavoriteMoviesListener.onGetFavoriteDone(listFavMovies);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG111", "err");
            }
        };
        userRef.addListenerForSingleValueEvent(eventListener);
    }
}
