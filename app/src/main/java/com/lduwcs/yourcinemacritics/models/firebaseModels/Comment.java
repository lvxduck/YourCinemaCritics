package com.lduwcs.yourcinemacritics.models.firebaseModels;

import androidx.room.Entity;

@Entity
public class Comment {
    String id;
    String email;
    String content;
    Float rating;
    String date;
    public Comment(){

    }
    public Comment(String id, String email, String content, Float rating, String date) {
        this.id = id;
        this.email = email;
        this.content = content;
        this.rating = rating;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
