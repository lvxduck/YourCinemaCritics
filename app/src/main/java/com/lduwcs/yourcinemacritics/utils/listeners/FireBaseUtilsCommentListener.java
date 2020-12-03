package com.lduwcs.yourcinemacritics.utils.listeners;

import com.lduwcs.yourcinemacritics.models.firebaseModels.Comment;

import java.util.ArrayList;

public interface FireBaseUtilsCommentListener {
    public void onGetCommentDone(ArrayList<Comment> comments);
    public void onGetCommentError(String err);
}
