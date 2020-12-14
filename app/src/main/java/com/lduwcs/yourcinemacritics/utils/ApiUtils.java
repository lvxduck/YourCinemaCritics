package com.lduwcs.yourcinemacritics.utils;

import android.content.Context;
import android.util.Log;
import com.lduwcs.yourcinemacritics.fragments.SearchFragment;
import com.lduwcs.yourcinemacritics.models.apiModels.Images;
import com.lduwcs.yourcinemacritics.models.apiModels.Movie;
import com.lduwcs.yourcinemacritics.models.apiModels.MovieData;
import com.lduwcs.yourcinemacritics.models.apiModels.Trailer;
import com.lduwcs.yourcinemacritics.utils.listeners.ApiUtilsCommentListener;
import com.lduwcs.yourcinemacritics.utils.listeners.ApiUtilsGetAllMoviesListener;
import com.lduwcs.yourcinemacritics.utils.listeners.ApiUtilsImagesListener;
import com.lduwcs.yourcinemacritics.utils.listeners.ApiUtilsLatestListener;
import com.lduwcs.yourcinemacritics.utils.listeners.ApiUtilsTopRatedListener;
import com.lduwcs.yourcinemacritics.utils.listeners.ApiUtilsTrailerListener;
import com.lduwcs.yourcinemacritics.utils.listeners.ApiUtilsUpcomingListener;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ApiUtils {
    MovieApiService apiService;
    private ApiUtilsGetAllMoviesListener apiUtilsGetAllMoviesListener;
    private ApiUtilsTrailerListener apiUtilsTrailerListener;
    private ApiUtilsImagesListener apiUtilsImagesListener;
    private ApiUtilsCommentListener apiUtilsCommentListener;
    private ApiUtilsLatestListener apiUtilsLatestListener;
    private ApiUtilsTopRatedListener apiUtilsTopRatedListener;
    private ApiUtilsUpcomingListener apiUtilsUpcomingListener;

    public void setApiUtilsGetAllMoviesListener(ApiUtilsGetAllMoviesListener apiUtilsGetAllMoviesListener) {
        this.apiUtilsGetAllMoviesListener = apiUtilsGetAllMoviesListener;
    }

    public void setApiUtilsTrailerListener(ApiUtilsTrailerListener apiUtilsTrailerListener) {
        this.apiUtilsTrailerListener = apiUtilsTrailerListener;
    }

    public void setApiUtilsImagesListener(ApiUtilsImagesListener apiUtilsImagesListener){
        this.apiUtilsImagesListener = apiUtilsImagesListener;
    }

    public void setApiUtilsCommentListener(ApiUtilsCommentListener apiUtilsCommentListener) {
        this.apiUtilsCommentListener = apiUtilsCommentListener;
    }

    public void setApiUtilsLatestListener(ApiUtilsLatestListener apiUtilsLatestListener){
        this.apiUtilsLatestListener = apiUtilsLatestListener;
    }

    public void setApiUtilsTopRatedListener(ApiUtilsTopRatedListener apiUtilsTopRatedListener){
        this.apiUtilsTopRatedListener = apiUtilsTopRatedListener;
    }

    public void setApiUtilsUpcomingListener(ApiUtilsUpcomingListener apiUtilsUpcomingListener){
        this.apiUtilsUpcomingListener = apiUtilsUpcomingListener;
    }

    public ApiUtils() {}

    public void getAllMovies(String content){
        ArrayList<Movie> movies = new ArrayList<Movie>();
        apiService = new MovieApiService();
        apiService.getMovies(content)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MovieData>() {
                    @Override
                    public void onSuccess(@NonNull MovieData movieData) {
                         movies.addAll(movieData.getResults());
                         apiUtilsGetAllMoviesListener.onGetAllMoviesDone(movies);
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        apiUtilsGetAllMoviesListener.onError(e.getMessage());
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
                        if(trailer.getResults() != null && trailer.getResults().size() > 0){
                            String key = trailer.getResults().get(0).getKey();
                            apiUtilsTrailerListener.onGetTrailerDone(key);
                        }
                        else apiUtilsTrailerListener.onGetTrailerError("No trailer available!");
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        apiUtilsTrailerListener.onGetTrailerError(e.getMessage());
                    }
                });
    }

    public void getMovieImages(String id){
        apiService = new MovieApiService();
        apiService.getImages(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Images>() {
                    @Override
                    public void onSuccess(@NonNull Images images) {
                        if(images.getBackdrops() != null && images.getBackdrops().size() > 0){
                            apiUtilsImagesListener.onGetImagesDone(images.getBackdrops());
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        apiUtilsImagesListener.onGetImagesError(e.getMessage());
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
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        apiUtilsCommentListener.onGetTrendingError(e.getMessage());
                    }
                });
    }

    public void getLatest(){
        ArrayList<Movie> movies = new ArrayList<Movie>();
        apiService = new MovieApiService();
        apiService.getLatest()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MovieData>() {
                    @Override
                    public void onSuccess(@NonNull MovieData movieData) {
                        apiUtilsLatestListener.onGetLatestDone((ArrayList<Movie>) movieData.getResults());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        apiUtilsLatestListener.onGetLatestError(e.getMessage());
                    }
                });
    }

    public void getTopRated(){
        ArrayList<Movie> movies = new ArrayList<Movie>();
        apiService = new MovieApiService();
        apiService.getTopRated()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MovieData>() {
                    @Override
                    public void onSuccess(@NonNull MovieData movieData) {
                        apiUtilsTopRatedListener.onGetTopRatedDone((ArrayList<Movie>) movieData.getResults());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        apiUtilsTopRatedListener.onGetTopRatedError(e.getMessage());
                    }
                });
    }

    public void getUpcoming(){
        ArrayList<Movie> movies = new ArrayList<Movie>();
        apiService = new MovieApiService();
        apiService.getUpcoming()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MovieData>() {
                    @Override
                    public void onSuccess(@NonNull MovieData movieData) {
                        apiUtilsUpcomingListener.onGetUpcomingDone((ArrayList<Movie>) movieData.getResults());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        apiUtilsUpcomingListener.onGetUpcomingError(e.getMessage());
                    }
                });
    }
}
