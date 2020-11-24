package com.lduwcs.yourcinemacritics.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.models.firebaseModels.Comment;
import com.lduwcs.yourcinemacritics.uiComponents.StarRate;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    ArrayList<Comment> comments;
    Context context;

    public CommentAdapter(ArrayList<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
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
        holder.txtCmtEmail.setText(comment.getEmail());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private StarRate srCmt;
        private TextView txtCmtDate;
        private TextView txtCmtEmail;
        private TextView txtCmtContent;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            srCmt = itemView.findViewById(R.id.srCmt);
            txtCmtDate = itemView.findViewById(R.id.txtCmtDate);
            txtCmtEmail = itemView.findViewById(R.id.txtCmtEmail);
            txtCmtContent = itemView.findViewById(R.id.txtCmtContent);
        }
    }
}
