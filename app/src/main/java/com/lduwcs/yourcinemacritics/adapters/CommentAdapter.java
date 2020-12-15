package com.lduwcs.yourcinemacritics.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.models.firebaseModels.Comment;
import com.lduwcs.yourcinemacritics.uiComponents.StarRate;
import com.lduwcs.yourcinemacritics.utils.FirebaseUtils;
import com.lduwcs.yourcinemacritics.utils.listeners.FirebaseUtilsGetUserInfoListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    ArrayList<Comment> comments;
    Context context;
    FirebaseUtils firebaseUtils;
    public CommentAdapter(ArrayList<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
        firebaseUtils = FirebaseUtils.getInstance();
        firebaseUtils.setFirebaseUtilsGetUserNameListener(new FirebaseUtilsGetUserInfoListener() {
            @Override
            public void onGetNameDone(String name, String path, ImageView imgAvatar, TextView textView, int position) {
                if(!name.isEmpty())
                    textView.setText(name);
                else{
                    textView.setText(comments.get(position).getEmail());
                }
                if(!path.isEmpty()){
                    Log.d("TAG123", "onGetNameDone: " + path);
                    Picasso.get()
                            .load(path)
                            .placeholder(R.drawable.spinner4)
                            .centerCrop()
                            .fit()
                            .into(imgAvatar);
                }
                Log.d("TAG123", "onGetNameDone: " + path);
            }
        });
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.srCmt.setStarsRate(comment.getRating());
        holder.txtCmtDate.setText(comment.getDate());
        holder.txtCmtContent.setText(comment.getContent());
        String userId = comment.getId();
        firebaseUtils.getUserInfo(userId, holder.imgCmtAvatar, holder.txtCmtEmail, position);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private final StarRate srCmt;
        private final TextView txtCmtDate;
        private final TextView txtCmtEmail;
        private final TextView txtCmtContent;
        private final ImageView imgCmtAvatar;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            srCmt = itemView.findViewById(R.id.srCmt);
            txtCmtDate = itemView.findViewById(R.id.txtCmtDate);
            txtCmtEmail = itemView.findViewById(R.id.txtCmtEmail);
            txtCmtContent = itemView.findViewById(R.id.txtCmtContent);
            imgCmtAvatar = itemView.findViewById(R.id.imgCmtAvatar);
        }
    }
}
