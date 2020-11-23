package com.lduwcs.yourcinemacritics.models.roomModels;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.lduwcs.yourcinemacritics.models.apiModels.Movie;

import java.util.List;

//luu tru movie
@Dao
public interface MoviesDao {
    @Query("SELECT * FROM movie")
    public List<Movie> getAllFavorites();

    @Insert
    public void insert(Movie... movie);

    @Query("DELETE FROM movie")
    public void deleteAll();

    @Delete
    public void deleteFavoriteMovie(Movie... movie);

    @Query("SELECT * FROM movie WHERE film_title LIKE '%' || :content || '%' ")
    public List<Movie> searchByTitle(String content);
}
