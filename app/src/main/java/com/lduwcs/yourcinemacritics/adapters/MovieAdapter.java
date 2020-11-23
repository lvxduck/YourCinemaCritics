package com.lduwcs.yourcinemacritics.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MoviViewHolder> {

    @NonNull
    @Override
    public MoviViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MoviViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MoviViewHolder extends RecyclerView.ViewHolder {
        public MoviViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
