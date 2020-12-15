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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.adapters.BackdropAdapter;
import com.lduwcs.yourcinemacritics.adapters.CommentAdapter;
import com.lduwcs.yourcinemacritics.models.apiModels.Image;
import com.lduwcs.yourcinemacritics.models.apiModels.Movie;
import com.lduwcs.yourcinemacritics.models.firebaseModels.Comment;
import com.lduwcs.yourcinemacritics.uiComponents.CustomProgressDialog;
import com.lduwcs.yourcinemacritics.uiComponents.NeuButton;
import com.lduwcs.yourcinemacritics.utils.ApiUtils;
import com.lduwcs.yourcinemacritics.utils.FirebaseUtils;
import com.lduwcs.yourcinemacritics.utils.Genres;
import com.lduwcs.yourcinemacritics.utils.listeners.ApiUtilsImagesListener;
import com.lduwcs.yourcinemacritics.utils.listeners.ApiUtilsTrailerListener;
import com.lduwcs.yourcinemacritics.utils.listeners.FireBaseUtilsAddFavoriteListener;
import com.lduwcs.yourcinemacritics.utils.listeners.FireBaseUtilsCommentListener;
import com.lduwcs.yourcinemacritics.utils.listeners.FireBaseUtilsRemoveFavoriteListener;
import com.squareup.picasso.Picasso;
import com.thelumiereguy.neumorphicview.views.NeumorphicCardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class CommentActivity extends AppCompatActivity {

    //-------UI variable------------
    private Bundle bundle;
    private RecyclerView commentRecView, photoRecView;
    private NeuButton btnBack, btnTrailer, btnSendComment;
    private ImageView btnAddToFavMovies, imgCmtBackground;
    private TextView txtDetailTitle, txtDetailReleaseDate, txtDetailOverview, txtDetailRating, txtDetailGenre;
    private EditText edtCmt;
    private ApiUtils utils;
    private NeumorphicCardView btnShowComment, btnShowPhoto;
    private TextView txtCommentOption, txtPhotoOption;
    private boolean isShowComment = true;

    private final String base_url_image = "https://image.tmdb.org/t/p/w500";
    private String movie_id = "";
    private ArrayList<Comment> comments;
    private CommentAdapter commentAdapter;
    private ArrayList<Image> images;
    private BackdropAdapter photoAdapter;
    private Context context;
    private FirebaseAuth mAuth;
    private CustomProgressDialog mProgressDialog;
    private Movie movie;
    private FirebaseUtils firebaseUtils;
    private NeumorphicCardView edtCommentBox;


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
        Objects.requireNonNull(getSupportActionBar()).hide();
        instance = this;

        bundle = getIntent().getExtras();
        imgCmtBackground = findViewById(R.id.imgCmtBackground);
        txtDetailTitle = findViewById(R.id.txtDetailTitle);
        txtDetailReleaseDate = findViewById(R.id.txtDetailReleaseDate);
        txtDetailOverview = findViewById(R.id.txtDetailOverview);
        txtDetailRating = findViewById(R.id.txtDetailRating);
        txtDetailGenre = findViewById(R.id.txtDetailGenre);
        commentRecView = findViewById(R.id.detailRecView);
        photoRecView = findViewById(R.id.photoRecView);
        btnBack = findViewById(R.id.btnDetailBack);
        btnTrailer = findViewById(R.id.btnDetailTrailer);
        btnSendComment = findViewById(R.id.btnDetailSendComment);
        edtCmt = findViewById(R.id.edtDetailComment);
        edtCommentBox = findViewById(R.id.btnDetailCommentBox);
        btnAddToFavMovies = findViewById(R.id.btnDetailFav);
        btnShowComment = findViewById(R.id.btnShowComment);
        btnShowPhoto = findViewById(R.id.btnShowPhoto);
        txtCommentOption = findViewById(R.id.txtCommentOption);
        txtPhotoOption = findViewById(R.id.txtPhotoOption);

        utils = new ApiUtils();
        context = getBaseContext();
        mProgressDialog = new CustomProgressDialog(this);
        mProgressDialog.show();
        //------UI------------
        loadUI();

        //---------adapter-----------
        loadAdapter();

        //---------Listener--------
        loadListener();

        //---------Button---------
        loadButton();
    }

    private void loadUI() {
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
        txtDetailGenre.setText("Genres: " + Genres.changeGenresIdToName(movie.getGenres()));
        txtDetailRating.setText("Rating: " + movie.getVoteAverage());
        movie_id = "" + movie.getId();
        mAuth = FirebaseAuth.getInstance();
    }

    private void loadAdapter() {
        comments = new ArrayList<>();
        commentAdapter = new CommentAdapter(comments, this);
        commentRecView.setAdapter(commentAdapter);
        firebaseUtils = FirebaseUtils.getInstance();
        firebaseUtils.getComments(movie_id);

        images = new ArrayList<>();
        photoAdapter = new BackdropAdapter(images, this);
        photoRecView.setAdapter(photoAdapter);
        photoRecView.setLayoutManager(new GridLayoutManager(this, 2));
        utils.setApiUtilsImagesListener(new ApiUtilsImagesListener() {
            @Override
            public void onGetImagesDone(ArrayList<Image> data) {
                onLoadImages(data);
            }

            @Override
            public void onGetImagesError(String err) {

            }
        });
        loadPhoto();
    }

    private void onLoadImages(ArrayList<Image> data) {
        images.clear();
        images.addAll(data);
        photoAdapter.notifyDataSetChanged();
    }

    private void loadPhoto() {
        images.clear();
        photoAdapter.notifyDataSetChanged();
        utils.getMovieImages(movie_id);
    }

    private void loadButton() {
        refreshOptionMenu();
        btnAddToFavMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog.show();
                if (firebaseUtils.isFavoriteMovie(movie.getId())) {
                    firebaseUtils.deleteFromFavMovie(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(),
                            movie.getId(), 0, btnAddToFavMovies);
                } else {
                    firebaseUtils.addToFavMovies(0, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(),
                            movie, btnAddToFavMovies);
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
        btnShowPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowComment = false;
                refreshOptionMenu();
            }
        });
        btnShowComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowComment = true;
                refreshOptionMenu();
            }
        });
    }

    private void refreshOptionMenu() {
        if (isShowComment) {
            btnShowComment.setNeuBackgroundColor(getResources().getColor(R.color.orange));
            btnShowPhoto.setNeuBackgroundColor(getResources().getColor(R.color.button_background));
            txtCommentOption.setTextColor(getResources().getColor(R.color.white));
            txtPhotoOption.setTextColor(getResources().getColor(R.color.text_color));
            commentRecView.setVisibility(View.VISIBLE);
            photoRecView.setVisibility(View.GONE);
            edtCmt.setVisibility(View.VISIBLE);
            btnSendComment.setVisibility(View.VISIBLE);
            edtCommentBox.setVisibility(View.VISIBLE);
        } else {
            btnShowComment.setNeuBackgroundColor(getResources().getColor(R.color.button_background));
            btnShowPhoto.setNeuBackgroundColor(getResources().getColor(R.color.orange));
            txtCommentOption.setTextColor(getResources().getColor(R.color.text_color));
            txtPhotoOption.setTextColor(getResources().getColor(R.color.white));
            commentRecView.setVisibility(View.GONE);
            photoRecView.setVisibility(View.VISIBLE);
            edtCmt.setVisibility(View.GONE);
            edtCommentBox.setVisibility(View.GONE);
            btnSendComment.setVisibility(View.INVISIBLE);
        }
    }

    private void loadListener() {
        if (firebaseUtils.isFavoriteMovie(movie.getId())) {
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
                comments.addAll(data);
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
    }

    private void handleSendComment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CommentActivity.this);
        View layout = null;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layout = inflater.inflate(R.layout.user_rate, null);
        final RatingBar ratingBar = (RatingBar) layout.findViewById(R.id.ratingBar);
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
}