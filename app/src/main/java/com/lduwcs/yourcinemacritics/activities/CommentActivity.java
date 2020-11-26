package com.lduwcs.yourcinemacritics.activities;

import android.content.Context;
import android.content.Intent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.adapters.CommentAdapter;
import com.lduwcs.yourcinemacritics.models.firebaseModels.Comment;
import com.squareup.picasso.Picasso;
import com.lduwcs.yourcinemacritics.utils.ApiUtils;
import com.lduwcs.yourcinemacritics.utils.FirebaseUtils;

import java.util.ArrayList;
import java.util.Objects;

public class CommentActivity extends AppCompatActivity {

    //-------UI variable------------
    private RecyclerView commentRecView;
    private CardView btnBack, btnTrailer, btnSendComment;
    private ImageView btnFav;
    private EditText edtCmt;
    private ApiUtils utils;

    private String base_url_image = "https://image.tmdb.org/t/p/w500";
    private Boolean isComment = false;
    private String movie_id = "";
    private ArrayList<Comment> comments;
    private CommentAdapter commentAdapter;
    private Context context;

    //------Instance----------
    private static CommentActivity instance;



    public static CommentActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        instance = this;

        Bundle bundle = getIntent().getExtras();

        Objects.requireNonNull(getSupportActionBar()).hide();

        ImageView imgCmtBackground = findViewById(R.id.imgCmtBackground);
        TextView txtDetailTitle = findViewById(R.id.txtDetailTitle);
        TextView txtDetailReleaseDate = findViewById(R.id.txtDetailReleaseDate);
        TextView txtDetailRating = findViewById(R.id.txtDetailRating);
        TextView txtDetailGenre = findViewById(R.id.txtDetailGenre);
        context = getBaseContext();

        //------UI------------
        Picasso.get()
                .load(base_url_image + bundle.getString("img_path"))
                .fit()
                .centerCrop()
                .placeholder(R.drawable.no_preview)
                .into(imgCmtBackground);
        txtDetailTitle.setText(bundle.getString("title"));
        txtDetailReleaseDate.setText("Release Day: " + bundle.getString("release_day"));
        txtDetailGenre.setText("Genres: "+ bundle.getString("genres"));
        txtDetailRating.setText("Rating: "+ bundle.getString("rating"));
        movie_id = bundle.getString("movie_id");

        commentRecView = findViewById(R.id.detailRecView);
        btnBack = findViewById(R.id.btnDetailBack);
        btnTrailer = findViewById(R.id.btnDetailTrailer);
        btnFav = findViewById(R.id.btnDetailFav);
        btnSendComment = findViewById(R.id.btnDetailSendComment);
        edtCmt = findViewById(R.id.edtDetailComment);
        utils = new ApiUtils(getBaseContext());
        Intent intent = getIntent();

        //---------adapter-----------
        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(comments,this);
        commentRecView.setAdapter(commentAdapter);
        FirebaseUtils.getComments(movie_id);

        //---------UI--------
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnTrailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = String.valueOf(movie_id);
                utils.getTrailer(id);
            }
        });
        btnSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edtCmt.getText().toString();
                String date = "2020-1-1";
                String email = "levinhnhanduc@gmail.com";
                Float rating = 6.5f;
                Comment comment = new Comment(email,content,rating,date);
                try{
                    FirebaseUtils.writeComment("ldeuc1233",movie_id,email,content,date,rating);
                    if(isComment==false){
                        comments.add(comment);
                        commentAdapter.notifyDataSetChanged();
                        isComment=true;
                    }
                }catch (Exception e){
                    Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void onGetCommentDone(ArrayList<Comment> data){
        comments.addAll(data);
        commentAdapter.notifyDataSetChanged();
    }

    public void watchYoutubeVideo( String id){
        Intent intent = new Intent(context, YoutubeActivity.class);
        intent.putExtra("key", id);
        startActivity(intent);
    }

    public void onVideoRequestSuccess(String key){
        watchYoutubeVideo(key);
        Log.d("DEBUG1", "onVideoRequestSuccess: "+key);
    }
}