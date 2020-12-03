package com.lduwcs.yourcinemacritics.utils.listeners;

import com.lduwcs.yourcinemacritics.models.apiModels.Movie;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface FireBaseUtilsFavoriteMoviesListener {
    public void onGetFavoriteDone(ArrayList<Movie> movies);
}
