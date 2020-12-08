package com.lduwcs.yourcinemacritics.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lduwcs.yourcinemacritics.activities.CommentActivity;
import com.lduwcs.yourcinemacritics.activities.MainActivity;
import com.lduwcs.yourcinemacritics.fragments.HomeFragment;
import com.lduwcs.yourcinemacritics.fragments.SearchFragment;
import com.lduwcs.yourcinemacritics.models.apiModels.Movie;
import com.lduwcs.yourcinemacritics.models.apiModels.MovieData;
import com.lduwcs.yourcinemacritics.models.apiModels.Trailer;
import com.lduwcs.yourcinemacritics.utils.listeners.ApiUtilsCommentListener;
import com.lduwcs.yourcinemacritics.utils.listeners.ApiUtilsGetAllMoviesListener;
import com.lduwcs.yourcinemacritics.utils.listeners.ApiUtilsTrailerListener;

import java.util.ArrayList;

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
        apiService = new MovieApiService();
        apiService.getMovies(content)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MovieData>() {
                    @Override
                    public void onSuccess(@NonNull MovieData movieData) {
                         movies.addAll(movieData.getResults());
                         SearchFragment.onSearchingDone(movies, mContext);
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
                        if(trailer.getResults() != null && trailer.getResults().size() > 0){
                            String key = trailer.getResults().get(0).getKey();
//                            if(mContext.getClass().getSimpleName().equals("MainActivity")){
//                                HomeFragment.adapter.onVideoRequestSuccess(key);
//                            }
//                            else{
//                                CommentActivity.getInstance().onVideoRequestSuccess(key);
//                            }
                            apiUtilsTrailerListener.onGetTrailerDone(key);

                        }
                        else{
                            apiUtilsTrailerListener.onGetTrailerError("No trailer available!");

                           // Toast.makeText(mContext, "No trailer available!", Toast.LENGTH_SHORT).show();
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
                        apiUtilsCommentListener.onGetTrendingError(e.getMessage());  }
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
//                        movies.addAll(movieData.getResults());
//                        HomeFragment.onLoadTrendingDone(movies,mContext);
                        Log.d("DEBUG1", "Success");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("DEBUG1", "Error!"+e.getMessage());
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
//                        movies.addAll(movieData.getResults());
//                        HomeFragment.onLoadTrendingDone(movies,mContext);
                        Log.d("DEBUG1", "Success");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("DEBUG1", "Error!"+e.getMessage());
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
//                        movies.addAll(movieData.getResults());
//                        HomeFragment.onLoadTrendingDone(movies,mContext);
                        Log.d("DEBUG1", "Success");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d("DEBUG1", "Error!"+e.getMessage());
                    }
                });
    }
}
