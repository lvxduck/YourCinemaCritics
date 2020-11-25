package com.lduwcs.yourcinemacritics.models.apiModels;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Movie {

    @PrimaryKey
    @ColumnInfo(name = "film_id")
    @SerializedName("id")
    private int id;

    @ColumnInfo(name = "film_title")
    @SerializedName("title")
    private String title;

    @ColumnInfo(name = "film_release_day")
    @SerializedName("release_date")
    private String releaseDay;

    @ColumnInfo(name = "film_genres")
    @SerializedName("genre_ids")
    private ArrayList<Integer> genres;

    @ColumnInfo(name = "film_poster_path")
    @SerializedName("poster_path")
    private String posterPath;

    @ColumnInfo(name = "film_overview")
    @SerializedName("overview")
    private String overview;

    @ColumnInfo(name = "film_vote_average")
    @SerializedName("vote_average")
    private double voteAverage;

    public Movie(){

    }

    public Movie(int id, String title, String releaseDay, ArrayList<Integer> genres, String posterPath, String overview, double voteAverage) {
        this.id = id;
        this.title = title;
        this.releaseDay = releaseDay;
        this.genres = genres;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDay() {
        return releaseDay;
    }

    public void setReleaseDay(String releaseDay) {
        this.releaseDay = releaseDay;
    }

    public ArrayList<Integer> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Integer> genres) {
        this.genres = genres;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }
}

