package com.example.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chatapp.Fragments.ChatsFragment;
import com.example.chatapp.Fragments.ProfileFragment;
import com.example.chatapp.Fragments.UsersFragment;
import com.example.chatapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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

        // TabLayout provides a horizontal layout to display tabs.
        // We reference the tablayout that we have in our activity_main.
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        // The viewpager is the widget that allows the user to swipe left and right to see another page or screen.
        // Here we reference the viewpager we have in activity_main.
        ViewPager viewPager = findViewById(R.id.view_pager);

        // The viewPagerAdapter job is to supply views to the mainActivity.
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Here we add the fragments that we want to use.
        viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
        viewPagerAdapter.addFragment(new UsersFragment(), "Users");
        viewPagerAdapter.addFragment(new ProfileFragment(), "Profile");


        // here we set the adapter that we have made earlier.
        viewPager.setAdapter(viewPagerAdapter);

        // We link the tablayout and the viewpager.
        tabLayout.setupWithViewPager(viewPager);

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

    //A fragment is usually used as part of an activity's user interface and contributes its own layout to the activity.
    class ViewPagerAdapter extends FragmentPagerAdapter {

        // We make two arraylist one for fragments and another for titles.
        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();

        }


        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}
