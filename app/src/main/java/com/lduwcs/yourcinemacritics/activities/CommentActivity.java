package com.lduwcs.yourcinemacritics.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.adapters.CommentAdapter;
import com.lduwcs.yourcinemacritics.models.firebaseModels.Comment;

import java.util.ArrayList;
import java.util.Objects;

public class CommentActivity extends AppCompatActivity {
    private RecyclerView commentRecView;
    private CardView btnBack, btnTrailer, btnSendComment;
    private ImageView btnFav;
    private EditText edtCmt;

    private ArrayList<Comment> comments;
    private CommentAdapter commentAdapter;

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
        comments = new ArrayList<>();
        comments.add(new Comment("minhchien@gmail.com","Con cho nay",(float)2.5,"2020_11_24"));
        comments.add(new Comment("phatdung@gmail.com","Dung mai thuoc ve phat",(float)3.5,"2020_11_24"));
        comments.add(new Comment("thuanLoki@gmail.com","Thuong nho dung",(float)1.5,"2020_11_24"));

        commentAdapter = new CommentAdapter(comments,this);
        commentRecView.setAdapter(commentAdapter);
    }
}