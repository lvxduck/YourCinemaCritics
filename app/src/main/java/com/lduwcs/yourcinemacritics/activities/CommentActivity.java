package com.lduwcs.yourcinemacritics.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.adapters.CommentAdapter;
import com.lduwcs.yourcinemacritics.models.firebaseModels.Comment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class CommentActivity extends AppCompatActivity {
    private RecyclerView commentRecView;
    private CardView btnBack, btnTrailer, btnSendComment;
    private ImageView btnFav;
    private EditText edtCmt;

    private ImageView imgCmtBackground;
    private TextView txtDetailTitle;
    private TextView txtDetailReleaseDate;
    private TextView txtDetailRating;
    private TextView txtDetailGenre;

    private ArrayList<Comment> comments;
    private CommentAdapter commentAdapter;
    String base_url_image = "https://image.tmdb.org/t/p/w500";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Bundle bundle = getIntent().getExtras();

        Objects.requireNonNull(getSupportActionBar()).hide();

        imgCmtBackground = findViewById(R.id.imgCmtBackground);
        txtDetailTitle = findViewById(R.id.txtDetailTitle);
        txtDetailReleaseDate = findViewById(R.id.txtDetailReleaseDate);
        txtDetailRating = findViewById(R.id.txtDetailRating);
        txtDetailGenre = findViewById(R.id.txtDetailGenre);

        Picasso.get()
                .load(base_url_image + bundle.getString("img_path"))
                .fit()
                .placeholder(R.drawable.no_preview)
                .into(imgCmtBackground);
        txtDetailTitle.setText(bundle.getString("title"));
        txtDetailReleaseDate.setText("Release Day: " + bundle.getString("release_day"));
        txtDetailGenre.setText("Genres: "+ bundle.getString("genres"));
        txtDetailRating.setText("Rating: "+ bundle.getString("rating"));


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