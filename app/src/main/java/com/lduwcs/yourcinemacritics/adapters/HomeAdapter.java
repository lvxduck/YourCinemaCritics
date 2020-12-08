package com.lduwcs.yourcinemacritics.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.activities.YoutubeActivity;
import com.lduwcs.yourcinemacritics.activities.CommentActivity;
import com.lduwcs.yourcinemacritics.models.apiModels.Movie;
import com.lduwcs.yourcinemacritics.uiComponents.CustomProgressDialog;
import com.lduwcs.yourcinemacritics.uiComponents.NeuButton;
import com.lduwcs.yourcinemacritics.uiComponents.StarRate;
import com.lduwcs.yourcinemacritics.utils.ApiUtils;
import com.lduwcs.yourcinemacritics.utils.FirebaseUtils;
import com.lduwcs.yourcinemacritics.utils.Genres;
import com.lduwcs.yourcinemacritics.utils.listeners.ApiUtilsTrailerListener;
import com.lduwcs.yourcinemacritics.utils.listeners.FireBaseUtilsAddFavoriteListener;
import com.lduwcs.yourcinemacritics.utils.listeners.FireBaseUtilsFavoriteMoviesListener;
import com.lduwcs.yourcinemacritics.utils.listeners.FireBaseUtilsRemoveFavoriteListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    Context context;
    ArrayList<Movie> movies;
    String base_url_image = "https://image.tmdb.org/t/p/w500";
    private ApiUtils utils;
    FirebaseUser user;
    CustomProgressDialog myProgressDialog;
    FirebaseUtils firebaseUtils;


    public HomeAdapter(Context context, @Nullable ArrayList<Movie> movies) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseUtils = FirebaseUtils.getInstance();
        this.context = context;
        this.movies = movies != null ? movies : new ArrayList<>();
        myProgressDialog = new CustomProgressDialog(context);
        utils = new ApiUtils();
        utils.setApiUtilsTrailerListener(new ApiUtilsTrailerListener() {
            @Override
            public void onGetTrailerDone(String key) {
                watchYoutubeVideo(context, key);
            }

            @Override
            public void onGetTrailerError(String err) {

            }
        });
        myProgressDialog.show();
        //firebaseUtils.getFavMovies(user.getUid());
        firebaseUtils.setFireBaseUtilsFavoriteMoviesListener(new FireBaseUtilsFavoriteMoviesListener() {
            @Override
            public void onGetFavoriteDone() {
                myProgressDialog.dismiss();
                Log.d("DEBUG2", "onGetFavoriteDone: "+"co roi nha");
                notifyDataSetChanged();
            }
        });
        initFirebaseListener();
    }

    public void initFirebaseListener(){
        notifyDataSetChanged();
        firebaseUtils.setFireBaseUtilsAddFavoriteListener(new FireBaseUtilsAddFavoriteListener() {
            @Override
            public void onSuccess(int position, View view) {
                ((NeuButton) view).setOnActive(true);
          //      favoriteMovies.add(movies.get(position));
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
            @Override
            public void onSuccess(int position, View view) {
                ((NeuButton) view).setOnActive(false);
               // favoriteMovies.remove(movies.get(position));
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
        return new ViewHolder(view);
    }

    //change item's content; choose what item does
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
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
                bundle.putSerializable("movie", movies.get(position));
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
        if(firebaseUtils.isFavoriteMovie(movies.get(position).getId())){
            holder.btnAddToFavorite.setOnActive(true);
        } else {
            holder.btnAddToFavorite.setOnActive(false);
        }
        holder.btnAddToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myProgressDialog.show();
                if (firebaseUtils.isFavoriteMovie(movies.get(position).getId())) {
                    firebaseUtils.deleteFromFavMovie(user.getUid(), movies.get(position).getId(), position, holder.btnAddToFavorite);
                } else {
                    firebaseUtils.addToFavMovies(position, user.getUid(), movies.get(position), holder.btnAddToFavorite);
                }
            }
        });
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

    private void watchYoutubeVideo(Context context, String id) {
        Intent intent = new Intent(context, YoutubeActivity.class);
        intent.putExtra("key", id);
        context.startActivity(intent);
    }

    public void onVideoRequestSuccess(String key){
        watchYoutubeVideo(context, key);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgHomePoster;
        private final NeuButton btnAddToFavorite;
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

