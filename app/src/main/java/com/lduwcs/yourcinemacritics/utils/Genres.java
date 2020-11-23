package com.lduwcs.yourcinemacritics.utils;

import com.google.gson.Gson;
import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.models.Genre;


import java.util.List;

public class Genres {
    public static List<Genre> genreList;
    public static void setData(){
        //String data = "[{\"id\":28,\"name\":\"Action\"},{\"id\":12,\"name\":\"Adventure\"},{\"id\":16,\"name\":\"Animation\"},{\"id\":35,\"name\":\"Comedy\"},{\"id\":80,\"name\":\"Crime\"},{\"id\":99,\"name\":\"Documentary\"},{\"id\":18,\"name\":\"Drama\"},{\"id\":10751,\"name\":\"Family\"},{\"id\":14,\"name\":\"Fantasy\"},{\"id\":36,\"name\":\"History\"},{\"id\":27,\"name\":\"Horror\"},{\"id\":10402,\"name\":\"Music\"},{\"id\":9648,\"name\":\"Mystery\"},{\"id\":10749,\"name\":\"Romance\"},{\"id\":878,\"name\":\"Science Fiction\"},{\"id\":10770,\"name\":\"TV Movie\"},{\"id\":53,\"name\":\"Thriller\"},{\"id\":10752,\"name\":\"War\"},{\"id\":37,\"name\":\"Western\"}]";
        //Gson gson = new Gson(); // khởi tạo Gson
        genreList.add(new Genre(28, "Action"));
        genreList.add(new Genre(12, "Adventure"));
        genreList.add(new Genre(16, "Animation"));
        genreList.add(new Genre(35, "Comedy"));
        genreList.add(new Genre(80, "Crime"));
        genreList.add(new Genre(99, "Documentary"));
        genreList.add(new Genre(18, "Drama"));
        genreList.add(new Genre(10751, "Family"));
        genreList.add(new Genre(9648, "Mystery"));
        genreList.add(new Genre(10749, "Romance"));
        genreList.add(new Genre(878, "Science fiction"));
        genreList.add(new Genre(10770, "TV movie"));
        genreList.add(new Genre(53, "Thriller"));
        genreList.add(new Genre(10752, "War"));
        genreList.add(new Genre(37, "Western"));
    }
}
