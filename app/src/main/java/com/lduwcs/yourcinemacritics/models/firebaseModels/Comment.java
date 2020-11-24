package com.lduwcs.yourcinemacritics.models.firebaseModels;

import androidx.room.Entity;

@Entity
public class Comment {
    String email;
    String content;
    Float rating;
    String date;
}
