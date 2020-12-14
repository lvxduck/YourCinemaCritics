package com.lduwcs.yourcinemacritics.utils.listeners;

import com.lduwcs.yourcinemacritics.models.firebaseModels.Comment;

import java.util.ArrayList;

public interface FirebaseUtilsGetUserInfoListener {
    public void onGetNameDone(String displayName, String avatarPath);
}
