package com.lduwcs.yourcinemacritics.utils;

import android.util.Log;

import com.lduwcs.yourcinemacritics.models.Movie;
import com.lduwcs.yourcinemacritics.models.Trailer;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ApiUtils {
    MovieApiService apiService;

    public void getAllMovies(String content){
        apiService = new MovieApiService();
        apiService.getMovies(content)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Movie>>() {
                    @Override
                    public void onSuccess(@NonNull List<Movie> movies) {
                        Log.d("DEBUG1", "Success");
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
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("DEBUG1", "Error!");
                    }
                });
    }


    public void getTrending(){
        apiService = new MovieApiService();
        apiService.getTrending()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Movie>>() {
                    @Override
                    public void onSuccess(@NonNull List<Movie> movies) {
                        Log.d("DEBUG1", "Success");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("DEBUG1", "Error!");
                    }
                });
    }
}
