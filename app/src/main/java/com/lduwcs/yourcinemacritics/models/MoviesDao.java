package com.lduwcs.yourcinemacritics.models;

import androidx.room.Dao;
import androidx.room.Query;

import com.lduwcs.yourcinemacritics.models.Movie;

import java.util.List;

@Dao
public interface MoviesDao {
    @Query("SELECT * FROM movie")
    public List<Movie> getAllFavorites();

    @Query("SELECT * FROM movie WHERE film_title LIKE '%' || :content || '%' ")
    public List<Movie> searchByTitle(String content);
}
