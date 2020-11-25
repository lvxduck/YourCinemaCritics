package com.lduwcs.yourcinemacritics.utils;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lduwcs.yourcinemacritics.models.firebaseModels.Comment;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUtils {
    public static void writeComment(String userId, String movieId, String email, String content, String date, Float rating){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("movies");
        DatabaseReference commentRef = rootRef.child(movieId).child(userId);
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
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String content = ds.child("content").getValue(String.class);
                    String date = ds.child("date").getValue(String.class);
                    String email = ds.child("email").getValue(String.class);
                    Float rating = ds.child("rating").getValue(Float.class);
                    comment.setContent(content);
                    comment.setDate(date);
                    comment.setEmail(email);
                    comment.setRating(rating);
                    listComment.add(comment);
                };
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        commentRef.addListenerForSingleValueEvent(eventListener);
        return listComment;
    }
}
