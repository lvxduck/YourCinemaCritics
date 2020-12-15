package com.lduwcs.yourcinemacritics.utils.listeners;

import com.lduwcs.yourcinemacritics.models.apiModels.Movie;

import java.util.ArrayList;

public interface ApiUtilsTopRatedListener {
    public void onGetTopRatedDone(ArrayList<Movie> movies);
    public void onGetTopRatedError(String err);
}
