package com.lduwcs.yourcinemacritics.utils.listeners;

import com.lduwcs.yourcinemacritics.models.apiModels.Movie;

import java.util.ArrayList;

public interface ApiUtilsUpcomingListener {
    public void onGetUpcomingDone(ArrayList<Movie> movies);
    public void onGetUpcomingError(String err);
}
