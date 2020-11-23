package com.lduwcs.yourcinemacritics.models;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MoviesApi {
    String API_KEY = "64cd41892fdef05b98e839364f94a25b";

    @GET("/search/movie?api_key="+API_KEY+"&query={content}")
    Single<List<Movie>> getMovies(@Path("content") String content);

    @GET("/movie/{id}/videos?api_key="+API_KEY)
    Single<Trailer> getMovieTrailer(@Path("id") String id);

    @GET("/trending/movie/week?api_key="+API_KEY)
    Single<List<Movie>> getTrending();
}
