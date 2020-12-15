package com.lduwcs.yourcinemacritics.models.roomModels;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.lduwcs.yourcinemacritics.models.apiModels.Movie;


@Database(entities = {Movie.class}, version = 3)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract MoviesDao getMoviesDao();
}
