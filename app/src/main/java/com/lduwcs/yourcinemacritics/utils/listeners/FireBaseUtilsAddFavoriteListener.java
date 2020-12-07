package com.lduwcs.yourcinemacritics.utils.listeners;

import android.view.View;

public interface FireBaseUtilsAddFavoriteListener {
    public void onSuccess(int position, View view);
    public void onError(String err);
}
