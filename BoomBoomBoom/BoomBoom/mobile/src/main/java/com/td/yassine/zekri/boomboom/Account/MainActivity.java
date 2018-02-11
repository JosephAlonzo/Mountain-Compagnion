package com.td.yassine.zekri.boomboom.Account;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Patterns;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.td.yassine.zekri.boomboom.ControlPanelActivity;
import com.td.yassine.zekri.boomboom.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button buttonLogin;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignUp;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {

            finish();
            Intent intent = new Intent(this, ControlPanelActivity.class);
            startActivity(intent);
        }

        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignUp = (TextView) findViewById(R.id.textViewSignUp);

        progressDialog = new ProgressDialog(this);


        buttonLogin.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);
    }


    private void userLogin() {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()) {

            //email is empty
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
            //stop the function
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {

            //password is empty
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            //stop the function
            return;
        }

        if (password.length()<6){
            editTextPassword.setError("Minimum lenght of password should be 6");
            editTextPassword.requestFocus();
            return;
        }

        progressDialog.setMessage("Login..");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {

                    // Login success
                    Toast.makeText(MainActivity.this, "Login Success !", Toast.LENGTH_SHORT).show();
                    finish();
                    Intent intent= new Intent(MainActivity.this,ControlPanelActivity.class);
                    startActivity(intent);
                }else{

                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(firebaseAuth.getCurrentUser() != null) {

            finish();
            startActivity(new Intent(this, ControlPanelActivity.class));
        }
    }

    @Override
    public void onClick(View view) {

        if (view == buttonLogin) {

            userLogin();
        }

        if (view == textViewSignUp) {

            finish();
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }
}
