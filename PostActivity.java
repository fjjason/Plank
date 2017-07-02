package com.chen.hanlin.plank;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.chen.hanlin.plank.Firebase.FirebaseDatabaseHelper;
import com.chen.hanlin.plank.Firebase.FirebaseUserEntity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import static com.chen.hanlin.plank.Firebase.FirebaseDatabaseHelper.databaseReference;
import static com.chen.hanlin.plank.MapsActivity.current;


public class PostActivity extends AppCompatActivity {


    //set up view, eventlisteners and lists
    private EditText editProfileName;

    private EditText editProfileCountry;

    //private Firebase mRef;
    public static final List<String> list_notes = new ArrayList<String>();
    public static final List<String> list_locations = new ArrayList<String>();
    private ListView mListView;
    private ValueEventListener mPostListener;

    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_post);

        setTitle("Time to Drop the Plank");
        //mRef = new Firebase("https://planknew-94081.firebaseio.com/users");

        editProfileName = (EditText) findViewById(R.id.profile_name);
        editProfileCountry = (EditText) findViewById(R.id.profile_note);
        //mListView = (ListView) findViewById(R.id.list1);



        /*
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mUsernames);
        mListView.setAdapter(arrayAdapter);

        mListView.setAdapter(arrayAdapter);
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getValue(String.class);
                mUsernames.add(value);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

*/

        //pull strings out of edittext, calls the userEntity class and store it in Firebase
        Button saveEditButton = (Button) findViewById(R.id.save_edit_button);
        saveEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String profileName = editProfileName.getText().toString();
                String profileCountry = editProfileCountry.getText().toString();
                //String profileCountry = editProfileCountry.getText().toString();
                //String profileCountry = editProfileCountry.getText().toString();


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    Intent firebaseUserIntent = new Intent(PostActivity.this, MainActivity.class);
                    startActivity(firebaseUserIntent);
                    finish();
                } else {
                    String userId = user.getProviderId();
                    String id = user.getUid();
                    String profileEmail = user.getEmail();

                    FirebaseUserEntity userEntity = new FirebaseUserEntity(id, profileEmail, profileName, profileCountry);
                    FirebaseDatabaseHelper firebaseDatabaseHelper = new FirebaseDatabaseHelper();
                    firebaseDatabaseHelper.createUserInFirebaseDatabase(id, userEntity);

                    editProfileName.setText("");
                    editProfileCountry.setText("");
                }


            }

        });







    }

    public void load(View V) {
        //String firstname;


        //loads database using Valueeventlistener and go to Maps

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                 //post = dataSnapshot.getValue(Post.class);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                String temp = dataSnapshot.child("users").child(user.getUid()).child("name").getValue(String.class);
                String temp2 = dataSnapshot.child("users").child(user.getUid()).child("note").getValue(String.class);
                list_notes.add(temp);
                list_locations.add(temp2);





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        };
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        FirebaseDatabaseHelper firebaseDatabaseHelper = new FirebaseDatabaseHelper();
        databaseReference.addValueEventListener(postListener);
        // [END post_value_event_listener]

        // Keep copy of post listener so we can remove it when app stops
        mPostListener = postListener;

        Intent moveToMaps = new Intent(PostActivity.this, MapsActivity.class);
        moveToMaps.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(moveToMaps);
    }


}