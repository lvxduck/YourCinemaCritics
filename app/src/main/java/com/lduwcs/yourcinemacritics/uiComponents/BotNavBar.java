package com.lduwcs.yourcinemacritics.uiComponents;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.lduwcs.yourcinemacritics.R;

public class BotNavBar extends ConstraintLayout {
    private NeuButton homeButton, searchButton, favButton, profileButton;

    public BotNavBar(Context context) {
        super(context);
        initView();
    }

    public BotNavBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BotNavBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public BotNavBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    public void initView() {
        inflate(getContext(), R.layout.bot_nav_bar, this);
        homeButton = findViewById(R.id.nav_home);
        searchButton = findViewById(R.id.nav_search);
        favButton = findViewById(R.id.nav_fav);
        profileButton = findViewById(R.id.nav_profile);

        homeButton.setOnActive(true);
    }

    public void setHomeOnClick(OnClickListener onClickListener) {
        homeButton.setOnClickListener(v -> {
            onClickListener.onClick(v);
            homeButton.setOnActive(true);
            searchButton.setOnActive(false);
            favButton.setOnActive(false);
            profileButton.setOnActive(false);
        });
    }

    public void setSearchOnClick(OnClickListener onClickListener) {
        searchButton.setOnClickListener(v -> {
            onClickListener.onClick(v);
            searchButton.setOnActive(true);
            homeButton.setOnActive(false);
            favButton.setOnActive(false);
            profileButton.setOnActive(false);
        });
    }

    public void setFavOnClick(OnClickListener onClickListener) {
        favButton.setOnClickListener(v -> {
            onClickListener.onClick(v);
            favButton.setOnActive(true);
            homeButton.setOnActive(false);
            searchButton.setOnActive(false);
            profileButton.setOnActive(false);
        });
    }

    public void setProfileOnClick(OnClickListener onClickListener) {
        profileButton.setOnClickListener(v -> {
            onClickListener.onClick(v);
            profileButton.setOnActive(true);
            homeButton.setOnActive(false);
            searchButton.setOnActive(false);
            favButton.setOnActive(false);
        });
    }

}
