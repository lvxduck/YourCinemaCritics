package com.lduwcs.yourcinemacritics.utils;


import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Genres {
    public static HashMap<Integer, String> genreList;
    public static void setData(){
        genreList = new HashMap<Integer, String>();
        genreList.put(28, "Action");
        genreList.put(12, "Adventure");
        genreList.put(16, "Animation");
        genreList.put(35, "Comedy");
        genreList.put(80, "Crime");
        genreList.put(99, "Documentary");
        genreList.put(18, "Drama");
        genreList.put(10751, "Family");
        genreList.put(14, "Fantasy");
        genreList.put(36, "History");
        genreList.put(27, "Horror");
        genreList.put(10402, "Music");
        genreList.put(9648, "Mystery");
        genreList.put(10749, "Romance");
        genreList.put(878, "Science fiction");
        genreList.put(10770, "TV movie");
        genreList.put(53, "Thriller");
        genreList.put(10752, "War");
        genreList.put(37, "Western");
    }

    public static String changeGenresIdToName(ArrayList<Integer> filmGenres){
        String result = "";
        for(Integer item : filmGenres){
            result = result + (genreList.get(item)+", ");
        }
        if(!result.isEmpty()){
            result = result.substring(0, result.length()-2);
        }
        return result;
    }

    public static int changeNameToGenre(String name){
        for(HashMap.Entry<Integer, String> entry : genreList.entrySet()){
            if(entry.getValue().equals(name)){
                return entry.getKey();
            }
        }
        return -1;
    }
}
