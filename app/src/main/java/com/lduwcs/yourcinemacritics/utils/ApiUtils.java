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
        ArrayList<Movie> movies = new ArrayList<Movie>();
//        ArrayList<Integer> genres = new ArrayList<Integer>();

        apiService = new MovieApiService();
        apiService.getMovies(content)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MovieData>() {
                    @Override
                    public void onSuccess(@NonNull MovieData movieData) {
                         Log.d("DEBUG1", String.valueOf(movieData.getResults().size()));
                         Log.d("DEBUG1", movieData.getResults().get(0).getTitle());
                         Movie movie = movieData.getResults().get(0);
//                         movies.add(new Movie(movie.getId(),
//                                 movie.getTitle(),
//                                 movie.getReleaseDay(),
//                                 movie.getGenres(),
//                                 movie.getPosterPath(),
//                                 movie.getOverview(),
//                                 movie.getVoteAverage()));

                        movies.add(movieData.getResults().get(0));
                        movies.add(movieData.getResults().get(1));
                        movies.add(movieData.getResults().get(2));
                        movies.add(movieData.getResults().get(3));
                        Log.d("DEBUG1", movie.getTitle());
                        Log.d("DEBUG1", movie.getReleaseDay());
                        Log.d("DEBUG1", movie.getGenres().toString());
                        Log.d("DEBUG1", movie.getPosterPath());
                        Log.d("DEBUG1", movie.getOverview());
                        Log.d("DEBUG1", String.valueOf(movie.getVoteAverage()));
                        Log.d("DEBUG1", "Success");


                        MainActivity.onLoadMovieDone(movies,mContext);
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
        apiService = new MovieApiService();
        apiService.getTrending()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MovieData>() {
                    @Override
                    public void onSuccess(@NonNull MovieData movies) {
                        //TODO: đưa danh sách trending vào mảng
                        Log.d("DEBUG1", movies.getResults().get(0).getTitle());
                        Log.d("DEBUG1", movies.getResults().get(0).getReleaseDay());
                        Log.d("DEBUG1", movies.getResults().get(0).getOverview());
                        Log.d("DEBUG1", movies.getResults().get(0).getPosterPath());
                        Log.d("DEBUG1", String.valueOf(movies.getResults().get(0).getVoteAverage()));
                        Log.d("DEBUG1", "Success");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("DEBUG1", "Error!"+e.getMessage());
                    }
                });
    }
}
