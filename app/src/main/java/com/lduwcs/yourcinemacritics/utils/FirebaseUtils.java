package com.lduwcs.yourcinemacritics.utils;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lduwcs.yourcinemacritics.activities.CommentActivity;
import com.lduwcs.yourcinemacritics.models.apiModels.Movie;
import com.lduwcs.yourcinemacritics.models.firebaseModels.Comment;
import com.lduwcs.yourcinemacritics.utils.listeners.FireBaseUtilsCommentListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtils {

    static FireBaseUtilsCommentListener fireBaseUtilsCommentListener;

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

    public static void addToFavMovies(String userId, String movieId){

    }

    public static ArrayList<Movie> getFavMovies(String userId) {
        ArrayList<Movie> listFavMovies = new ArrayList<>();
        Movie movie;
        movie = new Movie();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("user");
        DatabaseReference userRef = rootRef.child(userId);
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
                    listFavMovies.add(movie);
                };
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        userRef.addListenerForSingleValueEvent(eventListener);
        return listFavMovies;
    }
}

//interface FirebaseUtilsListener {
//    public  onGetCommentDone();
//}