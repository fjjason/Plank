package com.chen.hanlin.plank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegisterUserActivity extends AppCompatActivity {

    //setup view, firebase auth and progress bar
    EditText userEmailCreateEditText, userPassWordCreateEditText;
    LinearLayout createAccountBtn;
    ProgressDialog mProgressDialog;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set up view
        setContentView(R.layout.activity_register_user);
        userEmailCreateEditText = ( EditText ) findViewById(R.id.emailRegisterEditText);
        userPassWordCreateEditText = (EditText) findViewById(R.id.passwordRegisterEditText);
        createAccountBtn = ( LinearLayout) findViewById(R.id.createAccountSubmitBtn);

        //set progress bar and auth
        mProgressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

               //user is the current user
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if( user != null )
                {

                    Intent moveToHome = new Intent(RegisterUserActivity.this, MapsActivity.class);
                    moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity( moveToHome );

                }

            }
        };


        mAuth.addAuthStateListener(mAuthListener);

        //create account's progress bar
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog.setTitle("Create Account");
                mProgressDialog.setMessage("Wait while the account is being created..");
                mProgressDialog.show();

                createUserAccount();

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

    //creating user account
    private void createUserAccount() {

        String emailUser, passUser;

        emailUser = userEmailCreateEditText.getText().toString().trim();
        passUser = userPassWordCreateEditText.getText().toString().trim();

        //create user accounts using firebase Auth if password/email is present
        if( !TextUtils.isEmpty(emailUser) && !TextUtils.isEmpty(passUser))
        {

            mAuth.createUserWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if( task.isSuccessful() )
                    {

                        Toast.makeText(RegisterUserActivity.this, "Account created Success", Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();

                        Intent moveToHome = new Intent(RegisterUserActivity.this, MapsActivity.class);
                        moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity( moveToHome );

                    }else
                    {
                        Toast.makeText(RegisterUserActivity.this, "Account creation failed", Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();

                    }

                }
            });

        }

    }
}
