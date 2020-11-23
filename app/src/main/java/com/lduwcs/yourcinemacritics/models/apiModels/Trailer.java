package com.lduwcs.yourcinemacritics.models.apiModels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Trailer {
    @SerializedName("id")
    String id;

    @SerializedName("results")
    List<Video> results;

    public Trailer(String id, List<Video> results) {
        this.id = id;
        this.results = results;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }
}
