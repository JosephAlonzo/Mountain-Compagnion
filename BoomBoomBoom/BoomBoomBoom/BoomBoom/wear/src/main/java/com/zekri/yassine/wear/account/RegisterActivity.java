package com.zekri.yassine.wear.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.zekri.yassine.wear.MainActivity;
import com.zekri.yassine.wear.R;

public class RegisterActivity extends Activity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonValider;

    private ProgressDialog mProgressDialog;
    private FirebaseAuth mFirebaseAuth;

    private static String TAG = "WearRegisterTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.mFirebaseAuth = FirebaseAuth.getInstance();

        if(mFirebaseAuth.getCurrentUser() != null) {
            finish();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        this.mProgressDialog = new ProgressDialog(this);

        this.editTextEmail = findViewById(R.id.editTextEmail);
        this.editTextPassword = findViewById(R.id.editTextPassword);
        this.buttonValider = findViewById(R.id.buttonValider);

        this.buttonValider.setOnClickListener(this);
    }


    private void registerUser() {

        String email = this.editTextEmail.getText().toString().trim();
        String password = this.editTextPassword.getText().toString().trim();

        if(email.isEmpty()) {

            this.editTextEmail.setError("Email is required");
            this.editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            this.editTextEmail.setError("Please, enter a valid email");
            this.editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {

            this.editTextPassword.setError("Password is required");
            this.editTextPassword.requestFocus();
            return;
        }

        if(password.length() < 6 ) {

            this.editTextPassword.setError("Minimum length of password should be 6");
            this.editTextPassword.requestFocus();
            return;
        }

        mProgressDialog.setMessage("Registering User..");
        mProgressDialog.show();

        Log.wtf(TAG, "hey, before creation!");

        mFirebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mProgressDialog.dismiss();
                if(task.isSuccessful()) {

                    Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(RegisterActivity.this, "You are already registered", Toast.LENGTH_SHORT).show();
                    }else{

                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.buttonValider:

                registerUser();
                break;
        }
    }

}
