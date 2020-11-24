package com.lduwcs.yourcinemacritics.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.models.apiModels.Movie;
import com.lduwcs.yourcinemacritics.uiComponents.NeuButton;
import com.lduwcs.yourcinemacritics.uiComponents.StarRate;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//TODO: change String to model
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    Context context;
    ArrayList<Movie> movies;
    String base_url_image = "https://image.tmdb.org/t/p/w300";

    public HomeAdapter(Context context, @Nullable ArrayList<Movie> movies) {
        this.context = context;
        if (movies != null)
            this.movies = movies;
        else
            this.movies = new ArrayList<>();
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
                .resize(500,750)
                .centerCrop()
                .placeholder(R.drawable.poster_demo)
                .into(holder.imgHomePoster);
        holder.srHome.setStarsRate((float)movies.get(position).getVoteAverage());
//        ...
    }

    private String getLimitOverview(String overview, int max){
//        if(overview.length()>max){
//            while (overview.length()>max && overview.charAt(max)!=' '){
//                max+=1;
//            }
//            return overview.substring(0,max)+"...";
//        }
        return overview+overview;
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

