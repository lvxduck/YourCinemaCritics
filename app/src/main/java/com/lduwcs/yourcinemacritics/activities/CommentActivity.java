package com.lduwcs.yourcinemacritics.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lduwcs.yourcinemacritics.R;

import java.util.Objects;

public class CommentActivity extends AppCompatActivity {
    private RecyclerView commentRecView;
    private CardView btnBack, btnTrailer, btnSendComment;
    private ImageView btnFav;
    private EditText edtCmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Objects.requireNonNull(getSupportActionBar()).hide();

        commentRecView = findViewById(R.id.detailRecView);
        btnBack = findViewById(R.id.btnDetailBack);
        btnTrailer = findViewById(R.id.btnDetailTrailer);
        btnFav = findViewById(R.id.btnDetailFav);
        btnSendComment = findViewById(R.id.btnDetailSendComment);
        edtCmt = findViewById(R.id.edtDetailComment);
    }
}