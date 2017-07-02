package com.chen.hanlin.plank.Firebase;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.view.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseDatabaseHelper {

    private static final String TAG = FirebaseDatabaseHelper.class.getSimpleName();

    public static DatabaseReference databaseReference;

    public FirebaseDatabaseHelper(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    /*
    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI
            FirebaseUserEntity FirebaseUserEntity = dataSnapshot.getValue(FirebaseUserEntity.class);
            // ...
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            // ...
        }
    };
    //ref.mPostReference.addValueEventListener(postListener);
*/
    public void createUserInFirebaseDatabase(String userId, FirebaseUserEntity firebaseUserEntity){


        databaseReference.child("users").child(userId).child("name").setValue(firebaseUserEntity.getName());
        databaseReference.child("users").child(userId).child("note").setValue(firebaseUserEntity.getCountry());
    }


}

