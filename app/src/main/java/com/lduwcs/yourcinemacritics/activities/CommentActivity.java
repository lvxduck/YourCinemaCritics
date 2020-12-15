package com.lduwcs.yourcinemacritics.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.adapters.CommentAdapter;
import com.lduwcs.yourcinemacritics.models.apiModels.Movie;
import com.lduwcs.yourcinemacritics.models.firebaseModels.Comment;
import com.lduwcs.yourcinemacritics.uiComponents.CustomProgressDialog;
import com.lduwcs.yourcinemacritics.utils.Genres;
import com.lduwcs.yourcinemacritics.utils.listeners.ApiUtilsTrailerListener;
import com.lduwcs.yourcinemacritics.utils.listeners.FireBaseUtilsAddFavoriteListener;
import com.lduwcs.yourcinemacritics.utils.listeners.FireBaseUtilsCommentListener;
import com.lduwcs.yourcinemacritics.utils.listeners.FirebaseUtilsRemoveFavoriteListener;
import com.squareup.picasso.Picasso;
import com.lduwcs.yourcinemacritics.uiComponents.NeuButton;
import com.lduwcs.yourcinemacritics.utils.ApiUtils;
import com.lduwcs.yourcinemacritics.utils.FirebaseUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public class CommentActivity extends AppCompatActivity {

    //-------UI variable------------
    private RecyclerView commentRecView;
    private NeuButton btnBack, btnTrailer, btnSendComment;
    private ImageView btnAddToFavMovies;
    private EditText edtCmt;
    private ApiUtils utils;

    private String base_url_image = "https://image.tmdb.org/t/p/w500";
    private String movie_id = "";
    private ArrayList<Comment> comments;
    private CommentAdapter commentAdapter;
    private Context context;
    private FirebaseAuth mAuth;
    private CustomProgressDialog mProgressDialog;
    private Movie movie;
    private FirebaseUtils firebaseUtils;


    //------Instance----------
    private static CommentActivity instance;



    public static CommentActivity getInstance() {
        return instance;
    }

    @SuppressLint({"SetTextI18n", "InflateParams", "UseCompatLoadingForColorStateLists"})
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
        btnSendComment = findViewById(R.id.btnDetailSendComment);
        edtCmt = findViewById(R.id.edtDetailComment);
        btnAddToFavMovies = findViewById(R.id.btnDetailFav);
        utils = new ApiUtils();
        context = getBaseContext();
        mProgressDialog = new CustomProgressDialog(this);
        mProgressDialog.show();

        //------UI------------
        movie = (Movie) bundle.getSerializable("movie");
        Picasso.get()
                .load(base_url_image + movie.getPosterPath())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.no_preview)
                .into(imgCmtBackground);
        txtDetailTitle.setText(movie.getTitle());
        txtDetailOverview.setText(movie.getOverview());
        String[] releaseDayArray = movie.getReleaseDay().split("-");
        String reversedReleaseDay = "";
        for (int i = 2; i >= 0; i--) {
            reversedReleaseDay = reversedReleaseDay + releaseDayArray[i];
            if (i != 0) reversedReleaseDay += "/";
        }
        txtDetailReleaseDate.setText("Release Day: " + reversedReleaseDay);
        txtDetailGenre.setText("Genres: "+ Genres.changeGenresIdToName(movie.getGenres()));
        txtDetailRating.setText("Rating: "+ movie.getVoteAverage());
        movie_id = "" + movie.getId();
        mAuth = FirebaseAuth.getInstance();

        //---------adapter-----------
        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(comments,this);
        commentRecView.setAdapter(commentAdapter);
        firebaseUtils = FirebaseUtils.getInstance();
        firebaseUtils.getComments(movie_id);

        //---------Listener--------
        if(firebaseUtils.isFavoriteMovie(movie.getId())){
            btnAddToFavMovies.setBackgroundTintList(getResources().getColorStateList(R.color.orange));
        } else {
            btnAddToFavMovies.setBackgroundTintList(getResources().getColorStateList(R.color.white));
        }
        utils.setApiUtilsTrailerListener(new ApiUtilsTrailerListener() {
            @Override
            public void onGetTrailerDone(String key) {
                onVideoRequestSuccess(key);
            }
            @Override
            public void onGetTrailerError(String err) {

            }
        });
        firebaseUtils.setFireBaseUtilsCommentListener(new FireBaseUtilsCommentListener() {
              @Override
              public void onGetCommentDone(ArrayList<Comment> data) {

                  comments.addAll(sortCommentByDate(data));
                  commentAdapter.notifyDataSetChanged();
                  mProgressDialog.dismiss();
              }

              @Override
              public void onGetCommentError(String err) {
              }
            });
        firebaseUtils.setFireBaseUtilsRemoveFavoriteListener(new FireBaseUtilsRemoveFavoriteListener() {
            @Override
            public void onSuccess(int position, View view) {
                btnAddToFavMovies.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                mProgressDialog.dismiss();
                Toast.makeText(context, "Removed from your Favorite Movie", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String err) {

            }
        });
        firebaseUtils.setFireBaseUtilsAddFavoriteListener(new FireBaseUtilsAddFavoriteListener() {
            @Override
            public void onSuccess(int position, View view) {
                btnAddToFavMovies.setBackgroundTintList(getResources().getColorStateList(R.color.orange));
                mProgressDialog.dismiss();
                Toast.makeText(context, "Added to your Favorite Movie", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String err) {

            }
        });
        btnAddToFavMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();
                if (firebaseUtils.isFavoriteMovie(movie.getId())) {
                    firebaseUtils.deleteFromFavMovie(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(),
                            movie.getId(), 0, btnAddToFavMovies);
                } else {
                    firebaseUtils.addToFavMovies(0, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(),
                            movie,btnAddToFavMovies);
                }
            }
        });
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
                handleSendComment();
            }
        });
    }

    private void handleSendComment(){
        AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this);
        View layout= null;
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.user_rate, null);
        final RatingBar ratingBar = (RatingBar)layout.findViewById(R.id.ratingBar);
        builder.setTitle("Rate this movie");
        builder.setView(layout);

        // Add the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String content = edtCmt.getText().toString();
                if(content.trim().isEmpty()){
                    Toast.makeText(getBaseContext(),"Please Enter Proper Comment!",Toast.LENGTH_LONG).show();
                } else {
                    Date date = new Date();
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    String dateComment = simpleDateFormat.format(date);
                    Float rating = ratingBar.getRating()*2;
                    FirebaseUser user = mAuth.getCurrentUser();
                    Comment comment = new Comment(user.getEmail(),content,rating,dateComment);
                    try{
                        firebaseUtils.writeComment(user.getUid(),movie_id,user.getEmail(),content,dateComment,rating);
                        if (isComment(user.getEmail()) != -1) {
                            comments.remove(isComment(user.getEmail()));
                        }
                        comments.add(comment);
                        commentAdapter.notifyDataSetChanged();
                        edtCmt.getText().clear();
                        Toast.makeText(getBaseContext(),"Your comment has been posted!",Toast.LENGTH_LONG).show();
                        hideSoftKeyBoard();
                    }catch (Exception e){
                        Toast.makeText(getBaseContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(context,"CANCEL",Toast.LENGTH_LONG).show();
            }
        });

        builder.show();
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
    @SuppressLint("SimpleDateFormat")
    private ArrayList<Comment> sortCommentByDate(ArrayList<Comment> comments){
        Collections.sort(comments, new Comparator<Comment>() {
            @Override
            public int compare(Comment o1, Comment o2) {
                Date date1 = new Date();
                Date date2 = new Date();
                try {
                    date1=new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(o1.getDate());
                    date2=new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(o2.getDate());
                } catch (ParseException e) {
                    Log.d("DUC", "compare: "+e.getMessage());
                }
                return date2.compareTo(date1);
            }
        });
        return comments;
    }
}