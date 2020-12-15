package com.lduwcs.yourcinemacritics.utils.listeners;

import com.lduwcs.yourcinemacritics.models.apiModels.Image;

import java.util.ArrayList;

public interface ApiUtilsImagesListener {
    public void onGetImagesDone(ArrayList<Image> data);
    public void onGetImagesError(String err);
}
