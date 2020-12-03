package com.lduwcs.yourcinemacritics.utils;

import android.util.Log;

import com.lduwcs.yourcinemacritics.models.apiModels.Movie;
import com.lduwcs.yourcinemacritics.models.apiModels.MovieData;
import com.lduwcs.yourcinemacritics.models.apiModels.Trailer;
import com.lduwcs.yourcinemacritics.utils.listeners.ApiUtilsCommentListener;
import com.lduwcs.yourcinemacritics.utils.listeners.ApiUtilsTrailerListener;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ApiUtils {
    MovieApiService apiService;

    ApiUtilsCommentListener apiUtilsCommentListener;
    ApiUtilsTrailerListener apiUtilsTrailerListener;

    public void setApiUtilsCommentListener(ApiUtilsCommentListener apiUtilsCommentListener) {
        this.apiUtilsCommentListener = apiUtilsCommentListener;
    }

    public void setApiUtilsTrailerListener(ApiUtilsTrailerListener apiUtilsTrailerListener) {
        this.apiUtilsTrailerListener = apiUtilsTrailerListener;
    }

    public ApiUtils() {
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
                        Log.d("DEBUG1", "Success");
                        String key = trailer.getResults().get(0).getKey();
                        if(key.isEmpty() || key == null){
//                            Toast.makeText(mContext, "No trailer available!", Toast.LENGTH_SHORT);
                            apiUtilsTrailerListener.onGetTrailerError("Key is null");
                        }
                        else {
                            apiUtilsTrailerListener.onGetTrailerDone(key);
//                            if(mContext.getClass().getSimpleName().equals("MainActivity")){
//                                HomeFragment.adapter.onVideoRequestSuccess(key);
//                            }else{
//                                CommentActivity.getInstance().onVideoRequestSuccess(key);
//                            }
                        }
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
                        apiUtilsCommentListener.onGetTrendingDone((ArrayList<Movie>) movieData.getResults());
                        Log.d("DEBUG1", "Success");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("DEBUG1", "Error!"+e.getMessage());
                        apiUtilsCommentListener.onGetTrendingError(e.getMessage());
                    }
                });
    }
}

