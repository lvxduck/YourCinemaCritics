package com.lduwcs.yourcinemacritics.models.apiModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Images {
    @SerializedName("id")
    String id;

    @SerializedName("backdrops")
    ArrayList<Image> backdrops;

    public Images(String id, ArrayList<Image> backdrops) {
        this.id = id;
        this.backdrops = backdrops;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Image> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(ArrayList<Image> backdrops) {
        this.backdrops = backdrops;
    }

}
