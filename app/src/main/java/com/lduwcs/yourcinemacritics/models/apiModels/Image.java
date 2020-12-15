package com.lduwcs.yourcinemacritics.models.apiModels;

import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("aspect_ratio")
    String aspectRatio;

    @SerializedName("file_path")
    String filePath;

    public Image(String aspectRatio, String filePath) {
        this.aspectRatio = aspectRatio;
        this.filePath = filePath;
    }

    public String getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(String aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
