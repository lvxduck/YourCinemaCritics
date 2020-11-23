package com.lduwcs.yourcinemacritics.models;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Movie.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MoviesDao moviesDao();
    public static AppDatabase instance;

    public static AppDatabase getInstance(Context mContext){
        if(instance == null){
            instance = Room.databaseBuilder(mContext, AppDatabase.class, "movies_database").build();
        }
        return instance;
    }
}