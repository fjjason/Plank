package com.chen.hanlin.plank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.chen.hanlin.plank.MapsActivity;


public class MainActivity extends AppCompatActivity {
    //set up firebase auth, progress bar, view
    EditText userEmailEditText, userPasswordEditText;
    LinearLayout loginLayoutBtn, createAccountLayoutBtn;
    ProgressDialog mProgressDialog;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        userEmailEditText = (EditText) findViewById(R.id.emailLoginEditText);
        userPasswordEditText = (EditText) findViewById(R.id.passwordLoginEditText);
        loginLayoutBtn = (LinearLayout) findViewById(R.id.loginButtonMain);
        createAccountLayoutBtn = (LinearLayout) findViewById(R.id.createAccountButtonMain);

        mProgressDialog = new ProgressDialog(this);

        //make firebase auth
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //CHECKING USER PRESENCE
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if( user != null )
                {

                    //after log in, go to maps
                    Intent moveToHome = new Intent(MainActivity.this, MapsActivity.class);
                    moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(moveToHome);

                }

            }
        };

        mAuth.addAuthStateListener(mAuthListener);


        //set on click listener for register activity
        createAccountLayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, RegisterUserActivity.class));

            }
        });

        //make progress bar
        loginLayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog.setTitle("Loging in the user");
                mProgressDialog.setMessage("Please wait....");
                mProgressDialog.show();
                loginUser();

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void loginUser() {

       String userEmail, userPassword;

        userEmail = userEmailEditText.getText().toString().trim();
        userPassword = userPasswordEditText.getText().toString().trim();

        //log in and make sure email/password is present/valid
        if( !TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPassword))
        {

            mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if( task.isSuccessful())
                    {

                        mProgressDialog.dismiss();
                        Intent moveToHome = new Intent(MainActivity.this, MapsActivity.class);
                        moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(moveToHome);

                    }else
                    {

                        Toast.makeText(MainActivity.this, "Unable to login user", Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();

                    }

                }
            });

        }else
        {

            //null string entries
            Toast.makeText(MainActivity.this, "Please enter user email and password", Toast.LENGTH_LONG).show();
            mProgressDialog.dismiss();

        }

    }




}
