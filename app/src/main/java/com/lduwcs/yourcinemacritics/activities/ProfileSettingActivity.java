package com.lduwcs.yourcinemacritics.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lduwcs.yourcinemacritics.R;
import com.lduwcs.yourcinemacritics.fragments.ProfileFragment;
import com.lduwcs.yourcinemacritics.utils.FirebaseUtils;
import com.lduwcs.yourcinemacritics.utils.listeners.FirebaseUtilsGetUserInfoListener;
import com.squareup.picasso.Picasso;

public class ProfileSettingActivity extends AppCompatActivity {
    ImageView imgAvatar;
    TextView txtProfileEmail;
    EditText edtDisplayName;
    Button btnDone;
    FirebaseUser user;
    Uri imgAvatarUri;
    StorageReference storageReference;
    String email;
    String displayName;
    String avatarPath;
    private FirebaseAuth mAuth;
    private FirebaseUtils firebaseUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);
        imgAvatar = findViewById(R.id.imgAvatar);
        edtDisplayName = findViewById(R.id.edtName);
        txtProfileEmail = findViewById(R.id.txtProfileEmail);
        btnDone = findViewById(R.id.btnDone);

        firebaseUtils = FirebaseUtils.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            txtProfileEmail.setText(email);
            firebaseUtils.getUserInfo(user.getUid());
            firebaseUtils.setFirebaseUtilsGetUserNameListener(new FirebaseUtilsGetUserInfoListener() {
                @Override
                public void onGetNameDone(String name, String path) {
                    displayName = name;
                    edtDisplayName.setText(displayName);
                    if(!path.isEmpty()){
                        avatarPath = path;
                        Picasso.get()
                                .load(avatarPath)
                                .fit()
                                .into(imgAvatar);
                    }
                }
            });

        }

        storageReference = FirebaseStorage.getInstance().getReference("images");
        mAuth = FirebaseAuth.getInstance();
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseUtils.updateUser(user.getUid(), edtDisplayName.getText().toString(), avatarPath);
                Toast.makeText(ProfileSettingActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                hideSoftKeyBoard();
                onBackPressed();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == -1 && data != null && data.getData() != null) {
            imgAvatarUri = data.getData();
            imgAvatar.setImageURI(imgAvatarUri);
            uploadToStorage();
        }
    }
    public void uploadToStorage() {
        StorageReference riversRef = storageReference.child(mAuth.getUid());
        riversRef.putFile(imgAvatarUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("TAG123", "onSuccess: " );
                getUrl();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG123", "feo: ");
            }
        });
    }

    public void getUrl(){
        StorageReference pathReference = storageReference.child( mAuth.getUid());
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("TAG123", "onSuccess: " + uri);
                avatarPath = uri.toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG123", "feo: ");
            }
        });
    }
    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if(imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}