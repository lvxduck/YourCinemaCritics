package com.lduwcs.yourcinemacritics.uiComponents;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.lduwcs.yourcinemacritics.R;

public class StarRate extends LinearLayout {
    ImageView[] stars = new ImageView[5];

    public StarRate(Context context) {
        super(context);
        initView(null);
    }

    public StarRate(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public StarRate(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    public StarRate(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs);
    }

    public void initView(@Nullable AttributeSet attrs) {
        inflate(getContext(), R.layout.star_rate, this);
        @SuppressLint("Recycle") TypedArray ta = getContext()
                .obtainStyledAttributes(attrs, R.styleable.StarRate, 0, 0);
        try {
            int iconSize = ta.getDimensionPixelSize(R.styleable.StarRate_srIconSize, 32);
            float rate = ta.getFloat(R.styleable.StarRate_srRate, 0);
            stars[0] = findViewById(R.id.star1);
            stars[1] = findViewById(R.id.star2);
            stars[2] = findViewById(R.id.star3);
            stars[3] = findViewById(R.id.star4);
            stars[4] = findViewById(R.id.star5);
            if (iconSize != 32) {
                for (ImageView star :
                        stars) {
                    star.getLayoutParams().height = iconSize;
                    star.getLayoutParams().width = iconSize;
                    star.requestLayout();
                }
            }
            if (rate != 0)
                setStarsRate(rate);
        } finally {
            ta.recycle();
        }
    }

    public void setStarsRate(float rate) {
        if (rate >= 0) {
            rate /= 2;
            int yellowStars = (int) rate;
            int greyStars = 5 - yellowStars;
            if (rate - yellowStars >= 0.8) {
                yellowStars++;
                greyStars--;
            }
            //yellow stars
            if (yellowStars > 0) {
                for (int i = 0; i < yellowStars; i++) {
                    @SuppressLint("UseCompatLoadingForDrawables") Drawable dr = getResources()
                            .getDrawable(R.drawable.ic_round_star_rate_24);
                    stars[i].setImageDrawable(dr);
                }
            }
            //half star
            if (rate - yellowStars > 0.2) {
                @SuppressLint("UseCompatLoadingForDrawables") Drawable dr = getResources()
                        .getDrawable(R.drawable.ic_round_star_half_24);
                stars[yellowStars].setImageDrawable(dr);
                greyStars--;
            }
            //grey stars
            if (greyStars > 0) {
                for (int i = 0; i < greyStars; i++) {
                    @SuppressLint("UseCompatLoadingForDrawables") Drawable dr = getResources()
                            .getDrawable(R.drawable.ic_round_star_outline_24);
                    stars[4 - i].setImageDrawable(dr);
                }
            }
        }
    }
}
