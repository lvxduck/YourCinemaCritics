package com.lduwcs.yourcinemacritics.utils.listeners;

import com.lduwcs.yourcinemacritics.models.apiModels.Movie;

import java.util.ArrayList;

public interface ApiUtilsGetAllMoviesListener {
    public void onGetAllMoviesDone(ArrayList<Movie> movies);
    public void onError(String error);
}
