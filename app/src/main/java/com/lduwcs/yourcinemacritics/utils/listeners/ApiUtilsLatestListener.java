package com.lduwcs.yourcinemacritics.utils.listeners;

import com.lduwcs.yourcinemacritics.models.apiModels.Movie;

import java.util.ArrayList;

public interface ApiUtilsLatestListener {
    public void onGetLatestDone(ArrayList<Movie> movies);
    public void onGetLatestError(String err);
}
