package com.lduwcs.yourcinemacritics.utils.listeners;

import com.lduwcs.yourcinemacritics.models.apiModels.Movie;

import java.util.ArrayList;

public interface ApiUtilsCommentListener{
    public void onGetTrendingDone(ArrayList<Movie> movies);
    public void onGetTrendingError(String err);
}