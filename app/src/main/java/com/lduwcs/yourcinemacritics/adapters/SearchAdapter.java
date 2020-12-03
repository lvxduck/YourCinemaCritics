package com.lduwcs.yourcinemacritics.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.activities.CommentActivity;
import com.lduwcs.yourcinemacritics.models.apiModels.Movie;
import com.lduwcs.yourcinemacritics.uiComponents.StarRate;
import com.lduwcs.yourcinemacritics.utils.ApiUtils;
import com.lduwcs.yourcinemacritics.utils.Genres;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    Context context;
    ArrayList<Movie> movies;
    String base_url_image = "https://image.tmdb.org/t/p/w500";
    private ApiUtils utils;

    public SearchAdapter(Context context, @Nullable ArrayList<Movie> movies) {
        this.context = context;
        if (movies != null)
            this.movies = movies;
        else
            this.movies = new ArrayList<>();
        utils = new ApiUtils(context);
    }

    @NonNull
    @Override
    //create viewHolder
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_small_item, parent, false);
        return new SearchAdapter.ViewHolder(view);
    }

    //change item's content; choose what item does
    @Override
    public void onBindViewHolder(@NonNull final SearchAdapter.ViewHolder holder, final int position) {

        holder.txtTitleSmall.setText(movies.get(position).getTitle());

        String reversedReleaseDay = "";
        String releaseDay = movies.get(position).getReleaseDay();
        if(releaseDay != null){
            String[] releaseDayArray = releaseDay.split("-");
            for(int i = releaseDayArray.length - 1; i >= 0; i--){
                reversedReleaseDay = reversedReleaseDay + releaseDayArray[i];
                if(i != 0) reversedReleaseDay += "/";
            }
        }
        holder.txtDateSmall.setText(reversedReleaseDay);

        Picasso.get()
                .load(base_url_image + movies.get(position).getPosterPath())
                .fit()
                .placeholder(R.drawable.no_preview)
                .into(holder.imgPosterSmall);
        holder.srSmall.setStarsRate((float)movies.get(position).getVoteAverage());

        String finalReversedReleaseDay = reversedReleaseDay;
        holder.searchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", movies.get(position).getTitle());
                bundle.putString("img_path", movies.get(position).getPosterPath());
                bundle.putString("release_day", finalReversedReleaseDay);
                bundle.putString("genres", Genres.changeGenresIdToName(movies.get(position).getGenres()));
                bundle.putString("rating", String.valueOf(movies.get(position).getVoteAverage()));
                bundle.putString("movie_id",movies.get(position).getId()+"");
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.movies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgPosterSmall;
        private final ImageView imgIsFav;
        private final TextView txtTitleSmall;
        private final TextView txtDateSmall;
        private final StarRate srSmall;
        private final CardView searchItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Card view
            searchItem = itemView.findViewById(R.id.cardSmall);

            //Button
            imgIsFav = itemView.findViewById(R.id.imgIsFav);

            //Information
            imgPosterSmall = itemView.findViewById(R.id.imgPosterSmall);
            txtTitleSmall = itemView.findViewById(R.id.txtTitleSmall);
            txtDateSmall = itemView.findViewById(R.id.txtDateSmall);
            srSmall = itemView.findViewById(R.id.srSmall); //rating
        }
    }
}
