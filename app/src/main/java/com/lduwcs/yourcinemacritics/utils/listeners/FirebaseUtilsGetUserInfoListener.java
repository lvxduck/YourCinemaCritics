package com.lduwcs.yourcinemacritics.utils.listeners;

import android.provider.ContactsContract;
import android.widget.ImageView;
import android.widget.TextView;

import com.lduwcs.yourcinemacritics.models.firebaseModels.Comment;

import java.util.ArrayList;

public interface FirebaseUtilsGetUserInfoListener {
    public void onGetNameDone(String displayName, String avatarPath, ImageView imgAvatar, TextView textView, int position);
}
