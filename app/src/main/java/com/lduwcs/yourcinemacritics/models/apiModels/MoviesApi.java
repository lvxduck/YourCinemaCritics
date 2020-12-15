package com.lduwcs.yourcinemacritics.models.apiModels;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MoviesApi {
    String API_KEY = "64cd41892fdef05b98e839364f94a25b";

    @GET("search/movie?api_key="+API_KEY)
    Single<MovieData> getMovies(@Query("query") String content);

    @GET("movie/{id}/videos?api_key="+API_KEY)
    Single<Trailer> getMovieTrailer(@Path("id") String id);

    @GET("movie/{id}/images?api_key="+API_KEY)
    Single<Images> getImages(@Path("id") String id);

    @GET("trending/movie/week?api_key="+API_KEY)
    Single<MovieData> getTrending();

    @GET("movie/now_playing?api_key="+API_KEY)
    Single<MovieData> getLatest();

    @GET("movie/top_rated?api_key="+API_KEY)
    Single<MovieData> getTopRated();

    @GET("movie/upcoming?api_key="+API_KEY)
    Single<MovieData> getUpcoming();
}
