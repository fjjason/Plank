package com.chen.hanlin.plank;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReadActivity extends AppCompatActivity {

        //private Firebase mRef;
        private ArrayList<String> mUsernames = new ArrayList<>();
        private ListView mListView;
        private ValueEventListener mPostListener;

        private DatabaseReference mDatabase;

        private FirebaseAuth.AuthStateListener authStateListener;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.content_read);

            //dbName and dbNote are strings that need to be extracted for that particular note. Or at least
            //reach the name and note part of the database.

            //THE PROBLEM IS THE DAMN XML FILE IS POINTING TO POST
            String dbName, dbNote;
            dbName = "Default Man";
            dbNote = "Here is my defaultiest of notes.";

            setTitle("Time to Drop the Plank");
            //mRef = new Firebase("https://planknew-94081.firebaseio.com/users");
            TextView nameTextView = (TextView) findViewById(R.id.read_name);
            nameTextView.setText(dbName);
            TextView noteTextView = (TextView) findViewById(R.id.read_note);
            noteTextView.setText(dbNote);

            //mDatabase = FirebaseDatabase.getInstance().getReference();
        }

}
