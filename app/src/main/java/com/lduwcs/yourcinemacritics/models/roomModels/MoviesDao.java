package com.lduwcs.yourcinemacritics.models.roomModels;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.lduwcs.yourcinemacritics.models.apiModels.Movie;
import com.lduwcs.yourcinemacritics.models.firebaseModels.Comment;

import java.util.List;

//luu tru movie
@Dao
public interface MoviesDao {
    @Query("SELECT * FROM movie WHERE film_type = 0")
    public List<Movie> getTrending();

    @Query("SELECT * FROM movie WHERE film_type = 1")
    public List<Movie> getLatest();

    @Query("SELECT * FROM movie WHERE film_type = 2")
    public List<Movie> getTopRated();

    @Query("SELECT * FROM movie WHERE film_type = 3")
    public List<Movie> getUpcoming();

    @Insert
    public void insert(Movie... movie);

    @Query("DELETE FROM movie WHERE film_type = :type")
    public void deleteAll(int type);
}
