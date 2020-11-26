package com.lduwcs.yourcinemacritics.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.lduwcs.yourcinemacritics.R;

public class YoutubeActivity extends YouTubeBaseActivity {

    YouTubePlayerView youTubePlayerView;
    public static final String API_KEY = "AIzaSyB2SB3KAAwxNFTtD7Uw4URcx0TukR3dleo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube);
        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        youTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtube_player);

        youTubePlayerView.initialize(API_KEY, new YouTubePlayer.OnInitializedListener(){

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d("DEBUG1", "Youtube video loaded!");
                youTubePlayer.loadVideo(key);
                youTubePlayer.play();
                youTubePlayer.setFullscreen(true);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d("DEBUG1", "Failed to load Youtube video!");
            }
        });
    }
}