package com.td.yassine.zekri.boomboom.Account;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.td.yassine.zekri.boomboom.R;

import java.io.IOException;


public class ProfilActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int CHOOSE_IMAGE = 101;

    private Button buttonSave;
    private EditText editTextName;
    private TextView textViewEmail;
    private ImageView imageViewPicture;
    private TextView textViewVerified;

    private Uri uriProfileImage;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabaseReference;

    private String profileImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){

            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        editTextName = (EditText) findViewById(R.id.editTextName);
        imageViewPicture = (ImageView) findViewById(R.id.imageViewPicture);
        textViewVerified = (TextView) findViewById(R.id.textViewVerified);
        textViewEmail = (TextView) findViewById(R.id.textViewEmail);

        textViewEmail.setText("Bienvenue " + user.getEmail());

        buttonSave.setOnClickListener(this);

        imageViewPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooser();
            }
        });

        loadUserInformation();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        if(firebaseAuth.getCurrentUser() == null) {

            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    private void loadUserInformation() {

        final FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(imageViewPicture);
            }

            if(user.getDisplayName() != null){
                editTextName.setText(user.getDisplayName());
            }

            if(user.isEmailVerified()) {

                textViewVerified.setText("Email Verified");
            }else{

                textViewVerified.setText("Email not Verified (Click to Verify)");
                textViewVerified.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(ProfilActivity.this, "Verification Email Sent !", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {

           uriProfileImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imageViewPicture.setImageBitmap(bitmap);

                uploadImagetoFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void uploadImagetoFirebaseStorage() {

        StorageReference profileImageReference =
                    FirebaseStorage.getInstance().getReference("profilepics/" + System.currentTimeMillis() + ".jpg");
        if (uriProfileImage != null) {

            progressBar.setVisibility(View.VISIBLE);

            profileImageReference.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(View.GONE);

                            profileImageUrl = taskSnapshot.getDownloadUrl().toString();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.GONE);

                    Toast.makeText(ProfilActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void showImageChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
    }

    private void saveUserInformation() {

        String name = editTextName.getText().toString().trim();

        if (name.isEmpty()) {

            editTextName.setError("Name required");
            editTextName.requestFocus();
            return;
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null && profileImageUrl != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .setPhotoUri(Uri.parse(profileImageUrl))
                    .build();

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                Toast.makeText(ProfilActivity.this,"Save Done !", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void onClick(View view) {

        if(view == buttonSave) {

            saveUserInformation();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.menuLogout:

                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));

                break;
        }

        return true;
    }
}
