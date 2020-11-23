package com.lduwcs.yourcinemacritics.utils;

import android.content.Context;
import android.util.Log;

import com.lduwcs.yourcinemacritics.activities.MainActivity;
import com.lduwcs.yourcinemacritics.models.apiModels.Genre;
import com.lduwcs.yourcinemacritics.models.apiModels.Movie;
import com.lduwcs.yourcinemacritics.models.apiModels.MovieData;
import com.lduwcs.yourcinemacritics.models.apiModels.Trailer;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ApiUtils {
    MovieApiService apiService;
    Context mContext;

    public ApiUtils(Context context) {
        this.mContext=context;
    }

    public void getAllMovies(String content){
        apiService = new MovieApiService();
        apiService.getMovies(content)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MovieData>() {
                    @Override
                    public void onSuccess(@NonNull MovieData movieData) {
                         Log.d("DEBUG1", String.valueOf(movieData.getResults().size()));
                         Log.d("DEBUG1", movieData.getResults().get(0).getTitle());
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("DEBUG1", "Error!");
                    }
                });
    }


    public void getTrailer(String id){
        apiService = new MovieApiService();
        apiService.getMovieTrailer(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Trailer>() {
                    @Override
                    public void onSuccess(@NonNull Trailer trailer) {
                        Log.d("DEBUG1", trailer.getResults().get(0).getKey());
                        Log.d("DEBUG1", "Success");
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("DEBUG1", "Error!");
                    }
                });
    }


    public void getTrending(){
        ArrayList<Movie> movies = new ArrayList<Movie>();
        apiService = new MovieApiService();
        apiService.getTrending()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MovieData>() {
                    @Override
                    public void onSuccess(@NonNull MovieData movieData) {
                        //TODO: đưa danh sách trending vào mảng
                        movies.addAll(movieData.getResults());
                        MainActivity.onLoadFavoritesDone(movies,mContext);
                        Log.d("DEBUG1", "Success");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("DEBUG1", "Error!"+e.getMessage());
                    }
                });
    }
}
