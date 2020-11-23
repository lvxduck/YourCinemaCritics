package com.lduwcs.yourcinemacritics.uiComponents;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import com.borutsky.neumorphism.NeumorphicFrameLayout;
import com.lduwcs.yourcinemacritics.R;

public class NeuButton extends NeumorphicFrameLayout {
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

    public NeuButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
        } finally {
            ta.recycle();
        }
    }

    public void setOnActive(boolean isOnActive) {
        if (isOnActive) {
            icon.setColorFilter(getContext().getResources().getColor(R.color.orange));
            this.setState(NeumorphicFrameLayout.State.CONCAVE);
        } else {
            icon.setColorFilter(getContext().getResources().getColor(R.color.grey));
            this.setState(NeumorphicFrameLayout.State.FLAT);
        }
    }
}
