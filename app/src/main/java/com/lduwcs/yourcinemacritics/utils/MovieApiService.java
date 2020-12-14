package com.lduwcs.yourcinemacritics.utils;

import com.lduwcs.yourcinemacritics.models.apiModels.Images;
import com.lduwcs.yourcinemacritics.models.apiModels.MovieData;
import com.lduwcs.yourcinemacritics.models.apiModels.MoviesApi;
import com.lduwcs.yourcinemacritics.models.apiModels.Trailer;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieApiService {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private MoviesApi api;

    public MovieApiService(){
        api = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                        .build()
                        .create(MoviesApi.class);
    }
    public Single<MovieData> getMovies(String content){ return api.getMovies(content);}
    public Single<Trailer> getMovieTrailer(String id){ return api.getMovieTrailer(id);}
    public Single<Images> getImages(String id){ return api.getImages(id);}
    public Single<MovieData> getTrending(){ return api.getTrending();}
    public Single<MovieData> getLatest(){ return api.getLatest();}
    public Single<MovieData> getTopRated(){ return api.getTopRated();}
    public Single<MovieData> getUpcoming(){ return api.getUpcoming();}
}
