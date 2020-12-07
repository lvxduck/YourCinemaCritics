package com.lduwcs.yourcinemacritics.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.adapters.CommentAdapter;
import com.lduwcs.yourcinemacritics.models.firebaseModels.Comment;
import com.lduwcs.yourcinemacritics.uiComponents.NeuButton;
import com.lduwcs.yourcinemacritics.utils.ApiUtils;
import com.lduwcs.yourcinemacritics.utils.FirebaseUtils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class CommentActivity extends AppCompatActivity {

    //-------UI variable------------
    private RecyclerView commentRecView;
    private NeuButton btnBack, btnTrailer, btnSendComment;
    private ImageView btnFav;
    private EditText edtCmt;
    private ApiUtils utils;

    private final String base_url_image = "https://image.tmdb.org/t/p/w500";
    private String movie_id = "";
    private ArrayList<Comment> comments;
    private CommentAdapter commentAdapter;
    private Context context;
    private FirebaseAuth mAuth;


    //------Instance----------
    private static CommentActivity instance;



    public static CommentActivity getInstance() {
        return instance;
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
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
        TextView txtDetailOverview = findViewById(R.id.txtDetailOverview);
        TextView txtDetailRating = findViewById(R.id.txtDetailRating);
        TextView txtDetailGenre = findViewById(R.id.txtDetailGenre);
        commentRecView = findViewById(R.id.detailRecView);
        btnBack = findViewById(R.id.btnDetailBack);
        btnTrailer = findViewById(R.id.btnDetailTrailer);
        btnFav = findViewById(R.id.btnDetailFav);
        btnSendComment = findViewById(R.id.btnDetailSendComment);
        edtCmt = findViewById(R.id.edtDetailComment);
        utils = new ApiUtils(getBaseContext());
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
        txtDetailOverview.setText(bundle.getString("overview"));
        txtDetailGenre.setText("Genres: "+ bundle.getString("genres"));
        txtDetailRating.setText("Rating: "+ bundle.getString("rating"));
        movie_id = bundle.getString("movie_id");
        mAuth = FirebaseAuth.getInstance();

        //---------adapter-----------
        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(comments, this);
        commentRecView.setAdapter(commentAdapter);
        FirebaseUtils.getComments(movie_id);

        //---------UI--------
        btnBack.setOnClickListener(v -> onBackPressed());
        btnTrailer.setOnClickListener(v -> {
            String id = String.valueOf(movie_id);
            utils.getTrailer(id);
        });
        btnSendComment.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this);
            View layout;
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layout = inflater.inflate(R.layout.user_rate, null);
            final RatingBar ratingBar = layout.findViewById(R.id.ratingBar);
            builder.setTitle("Rate this movie");
            builder.setView(layout);

            // Add the buttons
            builder.setPositiveButton(R.string.ok, (dialog, id) -> {
                String content = edtCmt.getText().toString();
                if (content.trim().isEmpty()) {
                    Toast.makeText(getBaseContext(), "Please Enter Proper Comment!", Toast.LENGTH_LONG).show();
                } else {
                    Date date = new Date();
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String dateComment = simpleDateFormat.format(date);
                    Float rating = ratingBar.getRating() * 2;
                    FirebaseUser user = mAuth.getCurrentUser();
                    Comment comment = new Comment(user.getEmail(), content, rating, dateComment);
                    try {
                        FirebaseUtils.writeComment(user.getUid(), movie_id, user.getEmail(), content, dateComment, rating);
                        if (isComment(user.getEmail()) != -1) {
                            comments.remove(isComment(user.getEmail()));
                        }
                        comments.add(comment);
                        commentAdapter.notifyDataSetChanged();
                        edtCmt.getText().clear();
                        Toast.makeText(getBaseContext(), "Your comment has been posted!", Toast.LENGTH_LONG).show();
                        hideSoftKeyBoard();
                    } catch (Exception e) {
                        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.setNegativeButton(R.string.cancel, (dialog, id) -> {
            });

            builder.show();
        });
    }

    public void onGetCommentDone(ArrayList<Comment> data){
        comments.addAll(data);
        commentAdapter.notifyDataSetChanged();
    }

    private int isComment(String email){
        for(int i = 0; i < comments.size(); i++){
            if(comments.get(i).getEmail().equals(email)){
                return i;
            }
        }
        return -1;
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
    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if(imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}