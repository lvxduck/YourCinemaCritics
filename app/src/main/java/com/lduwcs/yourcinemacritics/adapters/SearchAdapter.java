package com.lduwcs.yourcinemacritics.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.activities.CommentActivity;
import com.lduwcs.yourcinemacritics.models.apiModels.Movie;
import com.lduwcs.yourcinemacritics.uiComponents.CustomProgressDialog;
import com.lduwcs.yourcinemacritics.uiComponents.NeuButton;
import com.lduwcs.yourcinemacritics.uiComponents.StarRate;
import com.lduwcs.yourcinemacritics.utils.FirebaseUtils;
import com.lduwcs.yourcinemacritics.utils.Genres;
import com.lduwcs.yourcinemacritics.utils.listeners.FireBaseUtilsAddFavoriteListener;
import com.lduwcs.yourcinemacritics.utils.listeners.FireBaseUtilsFavoriteMoviesListener;
import com.lduwcs.yourcinemacritics.utils.listeners.FireBaseUtilsRemoveFavoriteListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    Context context;
    ArrayList<Movie> movies;
    String base_url_image = "https://image.tmdb.org/t/p/w500";
    CustomProgressDialog myProgressDialog;
    private FirebaseUtils firebaseUtils;

    public SearchAdapter(Context context, @Nullable ArrayList<Movie> movies) {
        this.context = context;
        this.movies = movies != null ? movies : new ArrayList<>();
        firebaseUtils = FirebaseUtils.getInstance();
        myProgressDialog = new CustomProgressDialog(context);
        initFirebaseListener();
    }

    public void initFirebaseListener(){
        firebaseUtils.setFireBaseUtilsAddFavoriteListener(new FireBaseUtilsAddFavoriteListener() {
            @SuppressLint("UseCompatLoadingForColorStateLists")
            @Override
            public void onSuccess(int position, View view) {
                ((ImageButton) view).setBackgroundTintList(context.getResources().getColorStateList( R.color.orange));
                firebaseUtils.hashMapFavorite.put(movies.get(position).getId(),true);
                Toast.makeText(context, "Added to your Favorite Movie", Toast.LENGTH_SHORT).show();
                myProgressDialog.dismiss();
            }
            @Override
            public void onError(String err) {
                Toast.makeText(context, "ERROR: " + err, Toast.LENGTH_SHORT).show();
                myProgressDialog.dismiss();
            }
        });
        firebaseUtils.setFireBaseUtilsRemoveFavoriteListener(new FireBaseUtilsRemoveFavoriteListener() {
            @SuppressLint("UseCompatLoadingForColorStateLists")
            @Override
            public void onSuccess(int position, View view) {
                ((ImageButton) view).setBackgroundTintList(context.getResources().getColorStateList( R.color.white));
                firebaseUtils.hashMapFavorite.remove(movies.get(position).getId());
                Toast.makeText(context, "Removed from your Favorite Movie", Toast.LENGTH_SHORT).show();
                myProgressDialog.dismiss();
            }
            @Override
            public void onError(String err) {
                Toast.makeText(context, "ERROR: " + err, Toast.LENGTH_SHORT).show();
                myProgressDialog.dismiss();
            }
        });
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
                bundle.putSerializable("movie",movies.get(position));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        if(firebaseUtils.isFavoriteMovie(movies.get(position).getId())){
            holder.imgIsFav.setBackgroundTintList(context.getResources().getColorStateList( R.color.orange));
        } else {
            holder.imgIsFav.setBackgroundTintList(context.getResources().getColorStateList( R.color.white));
        }
        holder.imgIsFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myProgressDialog.show();
                if (firebaseUtils.isFavoriteMovie(movies.get(position).getId())) {
                    firebaseUtils.deleteFromFavMovie(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            movies.get(position).getId(), position, holder.imgIsFav);
                } else {
                    firebaseUtils.addToFavMovies(position, FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            movies.get(position), holder.imgIsFav);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.movies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgPosterSmall;
        private final ImageButton imgIsFav;
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
