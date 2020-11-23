package com.lduwcs.yourcinemacritics.models.apiModels;

import com.google.gson.annotations.SerializedName;

public class Video {
    @SerializedName("id")
    String id;

    @SerializedName("key")
    String key;

    @SerializedName("name")
    String name;

    public Video(String id, String key, String name) {
        this.id = id;
        this.key = key;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
