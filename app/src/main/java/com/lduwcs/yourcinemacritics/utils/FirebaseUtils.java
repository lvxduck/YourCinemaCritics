package com.lduwcs.yourcinemacritics.utils;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lduwcs.yourcinemacritics.activities.CommentActivity;
import com.lduwcs.yourcinemacritics.models.firebaseModels.Comment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtils {
    public static void writeComment(int userId, int movieId, String email, String content, String date, Float rating){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("movies");
        DatabaseReference commentRef = rootRef.child(movieId);
        DatabaseReference commentRef = rootRef.child("" + movieId);
        Comment comment = new Comment(email,content, rating, date);
        commentRef.child("" + userId).setValue(comment);
    }

    public static void getComments(String movieId){

    public static ArrayList<Comment> getComments(int movieId){
        ArrayList<Comment> listComment = new ArrayList<>();
        Comment comment;
        comment = new Comment();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("movies");
        DatabaseReference commentRef = rootRef.child("" + movieId);
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
                CommentActivity.onGetCommentDone(comments);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        commentRef.addListenerForSingleValueEvent(eventListener);
        return listComment;
    }

    public static void addToFavMovies(String userId, String movieId){

    }

//    public static List<String> getFavMovies(String userId){
//        final String[] listFavMovies = new String[1];
//        final List<String> movieIdlist = new List<>();
//        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("users");
//        ValueEventListener eventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                listFavMovies[0] = dataSnapshot.child(userId).getValue(String.class);
//                movieIdlist[0] = Arrays.asList(listFavMovies[0].split(","));
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        };
//        rootRef.addListenerForSingleValueEvent(eventListener);
//        return movieIdlist[0];
//    }
}
