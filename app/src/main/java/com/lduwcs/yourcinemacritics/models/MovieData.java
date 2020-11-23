package com.lduwcs.yourcinemacritics.models;

import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieData {

    @SerializedName("results")
    private List<Movie> results;

    public MovieData(List<Movie> results) {
        this.results = results;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }
}
