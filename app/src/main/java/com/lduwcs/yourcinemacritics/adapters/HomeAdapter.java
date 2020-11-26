package com.lduwcs.yourcinemacritics.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.ArrayUtils;
import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.activities.YoutubeActivity;
import com.lduwcs.yourcinemacritics.activities.CommentActivity;
import com.lduwcs.yourcinemacritics.models.apiModels.Movie;
import com.lduwcs.yourcinemacritics.uiComponents.NeuButton;
import com.lduwcs.yourcinemacritics.uiComponents.StarRate;
import com.lduwcs.yourcinemacritics.utils.ApiUtils;
import com.lduwcs.yourcinemacritics.utils.Genres;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    Context context;
    ArrayList<Movie> movies;
    String base_url_image = "https://image.tmdb.org/t/p/w500";
    private ApiUtils utils;

    public HomeAdapter(Context context, @Nullable ArrayList<Movie> movies) {
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
        return new ViewHolder(view);
    }

    //change item's content; choose what item does
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        //TODO: model to view
//        holder.imgHomePoster.setImageDrawable();
        holder.txtTittle.setText(movies.get(position).getTitle());
        holder.txtOverview.setText(getLimitOverview(movies.get(position).getOverview(),200));
        Picasso.get()
                .load(base_url_image + movies.get(position).getPosterPath())
                .fit()
                .placeholder(R.drawable.no_preview)
                .into(holder.imgHomePoster);
        holder.srHome.setStarsRate((float)movies.get(position).getVoteAverage());
        holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", movies.get(position).getTitle());
                bundle.putString("img_path", movies.get(position).getPosterPath());
                String releaseDay = movies.get(position).getReleaseDay();
                String[] releaseDayArray = releaseDay.split("-");
                String reversedReleaseDay = "";
                for(int i = 2; i >= 0; i--){
                    reversedReleaseDay = reversedReleaseDay + releaseDayArray[i];
                    if(i != 0) reversedReleaseDay += "/";
                }
                bundle.putString("release_day", reversedReleaseDay);
                bundle.putString("genres", Genres.changeGenresIdToName(movies.get(position).getGenres()));
                bundle.putString("rating", String.valueOf(movies.get(position).getVoteAverage()));
                bundle.putString("movie_id",movies.get(position).getId()+"");
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        holder.btnTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = String.valueOf(movies.get(position).getId());
                utils.getTrailer(id);
            }
        });
//        ...
    }

    private String getLimitOverview(String overview, int max){
        if(overview.length()>max){
            while (overview.length()>max && overview.charAt(max)!=' '){
                max+=1;
            }
            return overview.substring(0,max)+"...";
        }
        return overview;
    }

    @Override
    public int getItemCount() {
        return this.movies.size();
    }

    public void setContacts(ArrayList<Movie> movies) {
        this.movies = movies;
        //refresh recycler view when data updates
        notifyDataSetChanged();
    }

    public static void watchYoutubeVideo(Context context, String id){
//        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
//        Intent webIntent = new Intent(Intent.ACTION_VIEW,
//                Uri.parse("http://www.youtube.com/watch?v=" + id));
//        try {
//            context.startActivity(appIntent);
//        } catch (ActivityNotFoundException ex) {
//            context.startActivity(webIntent);
//        }

        Intent intent = new Intent(context, YoutubeActivity.class);
        intent.putExtra("key", id);
        context.startActivity(intent);
    }

    public void onVideoRequestSuccess(String key){
        watchYoutubeVideo(context, key);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgHomePoster;
        private final ImageView btnAddToFavorite;
        private final NeuButton btnTrailer;
        private final NeuButton btnComment;
        private final TextView txtTittle;
        private final TextView txtOverview;
        private final StarRate srHome;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //Button
            btnAddToFavorite = itemView.findViewById(R.id.btnHomeFav);
            btnTrailer = itemView.findViewById(R.id.btnHomeTrailer);
            btnComment = itemView.findViewById(R.id.btnHomeComment);

            //Information
            imgHomePoster = itemView.findViewById(R.id.imgHomePoster);
            txtTittle = itemView.findViewById(R.id.txtHomeTittle);
            txtOverview = itemView.findViewById(R.id.txtHomeOverview);
            srHome = itemView.findViewById(R.id.srHomeRate); //rating
        }
    }
}

