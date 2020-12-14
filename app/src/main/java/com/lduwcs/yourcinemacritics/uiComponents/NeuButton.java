package com.lduwcs.yourcinemacritics.uiComponents;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import com.lduwcs.yourcinemacritics.R;
import com.thelumiereguy.neumorphicview.views.NeumorphicCardView;

public class NeuButton extends RelativeLayout {
    private NeumorphicCardView neoFrame;
    private ImageView icon;

    public NeuButton(@NonNull Context context) {
        super(context);
        initView(null);
    }

    public NeuButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public NeuButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    public void initView(@Nullable AttributeSet attrs) {
        inflate(getContext(), R.layout.neumorphic_button, this);
        @SuppressLint("Recycle") TypedArray ta = getContext()
                .obtainStyledAttributes(attrs, R.styleable.NeuButton, 0, 0);
        try {
            int drawableId = ta.getResourceId(R.styleable.NeuButton_nbSrc, 0);
            icon = findViewById(R.id.neu_button_icon);
            if (drawableId != 0) {
                Drawable dr = AppCompatResources.getDrawable(getContext(), drawableId);
                icon.setImageDrawable(dr);
            }

            icon.setColorFilter(getContext().getResources().getColor(R.color.icon_inactive));

            neoFrame = findViewById(R.id.neu_button_frame);
            int buttonSize = ta.getDimensionPixelSize(R.styleable.NeuButton_nbSize, 16);
            neoFrame.setVerticalPadding(dpToPixel(getContext(), buttonSize));
            neoFrame.setHorizontalPadding(dpToPixel(getContext(), buttonSize));

            boolean isShadowAlt = ta.getBoolean(R.styleable.NeuButton_nbShadowAlt, false);
            if (isShadowAlt) {
                neoFrame.setShadowColor(R.color.black);
                neoFrame.setHighlightColor(R.color.grey);
            }

            int shadowPadding = ta.getDimensionPixelOffset(R.styleable.NeuButton_nbShadowPadding, 16);
            setMargin(getContext(), icon, shadowPadding);

            boolean isRound = ta.getBoolean(R.styleable.NeuButton_nbRound, false);
            if (isRound) {
                neoFrame.setCardRadius(100);
            }

            int iconSize = ta.getDimensionPixelSize(R.styleable.NeuButton_nbIconSize, (int) dpToPixel(getContext(), 35));
            icon.getLayoutParams().height = iconSize;
            icon.getLayoutParams().width = iconSize;
            icon.requestLayout();

        } finally {
            ta.recycle();
        }
    }

    public void setOnActive(boolean isOnActive) {
        if (isOnActive) {
            icon.setColorFilter(getContext().getResources().getColor(R.color.orange));
        } else {
            icon.setColorFilter(getContext().getResources().getColor(R.color.icon_inactive));
        }
    }

    public void setMargin(Context con, View view, int dp) {
        // convert the DP into pixel
        int pixel = (int) dpToPixel(con, dp);

        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(pixel, pixel, pixel, pixel);
            view.requestLayout();
        }
    }

    private float dpToPixel(Context con, int dp) {
        final float scale = con.getResources().getDisplayMetrics().density;
        // convert the DP into pixel
        return (dp * scale + 0.5f);
    }
}
