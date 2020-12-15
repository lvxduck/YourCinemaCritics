package com.lduwcs.yourcinemacritics.adapters;

import android.app.AlertDialog;
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
    String base_url_image_500 = "https://image.tmdb.org/t/p/w500";
    String base_url_image_original = "https://image.tmdb.org/t/p/original";

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
                .load(base_url_image_500 + images.get(position).getFilePath())
                .centerCrop()
                .fit()
                .placeholder(R.drawable.no_preview)
                .into(holder.ivBackdrop);

        holder.ivBackdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View layout = null;
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                layout = inflater.inflate(R.layout.image_fullscreen, null);
                Picasso.get()
                        .load(base_url_image_original + images.get(position).getFilePath())
                        .centerInside()
                        .fit()
                        .placeholder(R.drawable.no_preview)
                        .into((ImageView)layout.findViewById(R.id.iv_backdrop_fullscreen));
                builder.setView(layout);
                builder.show();
            }
        });
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
