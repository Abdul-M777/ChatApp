package com.example.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // The toolbar name and set it to be enabled.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        // Toolbar shit ends here.

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // here we show da way to the thing we want from the database, which in this case is User id.
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());


        // addValueEventListener can be used to receive events about data change.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            // onDataChange will be called with a snapshot of the data at this location. It will also be called each time the data changes.
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // dataSnapshot.getValue is used to marshall the data contained in this snapshot into the class that we chose.
                // First the class must have a default constructor that takes no argument.
                // Second the class must define public getters for the properties to be assigned.
                User user = dataSnapshot.getValue(User.class);
                // We set the username to the username we get from the User class which we get from the database.
                username.setText(user.getUsername());
                // We check if the user doesn't have an image profile.
                // if not we will set the default image as the profile image for that user.
                if (user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {
                    // if the user have a profile image, we will set that instead.
                    Glide.with(MainActivity.this).load(user.getImageURL()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    // To specify the options menu for an activity, override onCreateOptionsMenu().
    public boolean onCreateOptionsMenu(Menu menu) {
        // Here we reference the menu we use.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    // This method passes the MenuItem selected.
    public boolean onOptionsItemSelected(MenuItem item) {
        // We make a switch statement for each choice in the menu.
        switch (item.getItemId()) {
            // Here we have our logout case. If the user select the logout option from the menu this code will be activated.
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
                finish();
                return true;
        }

        return false;
    }
}
