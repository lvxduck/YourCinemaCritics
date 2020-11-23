package com.lduwcs.yourcinemacritics.models;

import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity
public class MovieData {
    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private List<Movie> results;

    public MovieData(int page, List<Movie> results) {
        this.page = page;
        this.results = results;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }
}
