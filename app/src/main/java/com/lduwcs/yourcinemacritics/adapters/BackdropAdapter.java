package com.lduwcs.yourcinemacritics.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.models.apiModels.Image;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BackdropAdapter extends RecyclerView.Adapter<BackdropAdapter.BackdropViewHolder> {

    ArrayList<Image> images;
    Context context;
    String base_url_image = "https://image.tmdb.org/t/p/original";

    public BackdropAdapter(ArrayList<Image> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @NonNull
    @Override
    public BackdropAdapter.BackdropViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BackdropAdapter.BackdropViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.backdrop_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BackdropAdapter.BackdropViewHolder holder, int position) {
        Picasso.get()
                .load(base_url_image + images.get(position).getFilePath())
                .centerCrop()
                .fit()
                .placeholder(R.drawable.no_preview)
                .into(holder.ivBackdrop);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class BackdropViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivBackdrop;

        public BackdropViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBackdrop = itemView.findViewById(R.id.iv_backdrop);
        }
    }
}
