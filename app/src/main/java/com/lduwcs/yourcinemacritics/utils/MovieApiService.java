package com.lduwcs.yourcinemacritics.utils;

import com.lduwcs.yourcinemacritics.models.Movie;
import com.lduwcs.yourcinemacritics.models.MoviesApi;
import com.lduwcs.yourcinemacritics.models.Trailer;

import java.util.List;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieApiService {
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private MoviesApi api;

    public MovieApiService(){
        api = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                        .build()
                        .create(MoviesApi.class);
    }
    public Single<List<Movie>> getMovies(String content){ return api.getMovies(content);}
    public Single<Trailer> getMovieTrailer(String id){ return api.getMovieTrailer(id);}
    public Single<List<Movie>> getTrending(){ return api.getTrending();}
}
