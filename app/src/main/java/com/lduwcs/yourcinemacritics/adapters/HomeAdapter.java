package com.lduwcs.yourcinemacritics.adapters;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    Context context;
    ArrayList<Movie> movies;
    ArrayList<Movie> favoriteMovies;
    String base_url_image = "https://image.tmdb.org/t/p/w500";
    private ApiUtils utils;
    FirebaseUser user;
    CustomProgressDialog myProgressDialog;

    public HomeAdapter(Context context, @Nullable ArrayList<Movie> movies) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        favoriteMovies = new ArrayList<>();
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
        FirebaseUtils.getFavMovies(user.getUid());
        FirebaseUtils.setFireBaseUtilsFavoriteMoviesListener(new FireBaseUtilsFavoriteMoviesListener() {
            @Override
            public void onGetFavoriteDone(ArrayList<Movie> movies) {
                favoriteMovies.addAll(movies);
                myProgressDialog.dismiss();
                notifyDataSetChanged();
            }
        });
        FirebaseUtils.setFireBaseUtilsAddFavoriteListener(new FireBaseUtilsAddFavoriteListener() {
            @Override
            public void onSuccess(int position, View view) {
                ((NeuButton) view).setOnActive(true);
                favoriteMovies.add(movies.get(position));
                Toast.makeText(context, "Added to your Favorite Movie", Toast.LENGTH_SHORT).show();
                myProgressDialog.dismiss();
            }

            @Override
            public void onError(String err) {
                Toast.makeText(context, "ERROR: " + err, Toast.LENGTH_SHORT).show();
                myProgressDialog.dismiss();
            }
        });
        FirebaseUtils.setFireBaseUtilsRemoveFavoriteListener(new FireBaseUtilsRemoveFavoriteListener() {
            @Override
            public void onSuccess(int position, View view) {
                ((NeuButton) view).setOnActive(false);
                favoriteMovies.remove(movies.get(position));
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
        holder.txtOverview.setText(getLimitOverview(movies.get(position).getOverview(), 200));
        Picasso.get()
                .load(base_url_image + movies.get(position).getPosterPath())
                .fit()
                .placeholder(R.drawable.no_preview)
                .into(holder.imgHomePoster);
        holder.srHome.setStarsRate((float) movies.get(position).getVoteAverage());
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
                for (int i = 2; i >= 0; i--) {
                    reversedReleaseDay = reversedReleaseDay + releaseDayArray[i];
                    if (i != 0) reversedReleaseDay += "/";
                }
                bundle.putString("release_day", reversedReleaseDay);
                bundle.putString("genres", Genres.changeGenresIdToName(movies.get(position).getGenres()));
                bundle.putString("rating", String.valueOf(movies.get(position).getVoteAverage()));
                bundle.putString("movie_id", movies.get(position).getId() + "");
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
        if(isInFavoriteMovies(movies.get(position))){
            holder.btnAddToFavorite.setOnActive(true);
        } else {
            holder.btnAddToFavorite.setOnActive(false);
        }
        holder.btnAddToFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myProgressDialog.show();
                if (isInFavoriteMovies(movies.get(position))) {
                    FirebaseUtils.deleteFromFavMovie(user.getUid(), movies.get(position).getId(), position, holder.btnAddToFavorite);
                } else {
                    Movie movie = new Movie(movies.get(position).getId(),
                            movies.get(position).getTitle(), movies.get(position).getReleaseDay(),
                            movies.get(position).getGenres(), movies.get(position).getPosterPath(),
                            movies.get(position).getOverview(), movies.get(position).getVoteAverage());
                    FirebaseUtils.addToFavMovies(position, user.getUid(), movie, holder.btnAddToFavorite);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.movies.size();
    }

    //-----------------------------//

    private String getLimitOverview(String overview, int max) {
        if (overview.length() > max) {
            while (overview.length() > max && overview.charAt(max) != ' ') {
                max += 1;
            }
            return overview.substring(0, max) + "...";
        }
        return overview;
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

    public boolean isInFavoriteMovies(Movie movie) {
        for (Movie m : favoriteMovies) {
            if (movie.getId() == m.getId()) return true;
        }
        return false;
    }

    //------------------------------------//
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

