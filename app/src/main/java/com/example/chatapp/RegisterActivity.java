package com.example.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText username, email, password;
    Button btn_register;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // The toolbar name and set it to be enabled.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Toolbar shit ends here.

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        btn_register = findViewById(R.id.btn_register);

        // We get an instance of this class by calling getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        // When the user clicks on the register button these things will happen.
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // First we will get the username, email and password texts in string form.
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                // Here we check if the username, email or password are empty.
                if(TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    // if at least one of them are, we will show them a message, that tells them what is wrong.
                    Toast.makeText(RegisterActivity.this, "Fill the required fields", Toast.LENGTH_SHORT).show();
                    // If the password length is less than 6.
                } else if(txt_password.length() < 6) {
                    // We will show the user a message that tells them what is wrong.
                    Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    // if there are no problem, we will register the username, email and password in the database.
                    register(txt_username,txt_email,txt_password);
                }
            }
        });
    }

    // This method will be used to register username, email and password in the database.
    // The method accepts three parameters in String type.
    private void register(final String username, String email, String password){

        // Here we firebaseAuth that we want the authentication for our users to be email and password.
        // We insert email and password as parameters.
        // To handle success and failure in the same listener, attach an OnCompleteListener.
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // if the task is successful.
                if(task.isSuccessful()){
                    // We get the current user to get a firebaseuser object, which contains information about the signed-in user.
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    // We make an assertion which always checks if the firebase user is not null.
                    assert firebaseUser != null;
                    // userid will be the userid in the firebase.
                    String userid = firebaseUser.getUid();

                    // Here we choose the path where we want to save our data.
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                    // We make a hashmap that contains two Strings.
                    HashMap<String, String> hashMap = new HashMap<>();
                    // In the hashmap we put id as userid, username as username and imageURL as default.
                    // the first String is the key and the second String is the content for that key.
                    hashMap.put("id",userid);
                    hashMap.put("username",username);
                    hashMap.put("imageURL", "default");

                    // We set the values of the hashmap in the database.
                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // If it is successful.
                            if(task.isSuccessful()){
                                // We transition from this activity to the MainActivity
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

                                // FLAG_ACTIVITY_CLEAR_TASK is used to clear any activity associated with the activity before the activity is started.
                                // FLAG_ACTIVITY_NEW_TASK this activity will become the start of a new task on this history.
                                // So basically this will be used so the user can't go back to the previous page.
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                // We start the new activity.
                                startActivity(intent);
                                // finish() closes this activity.
                                finish();

                            }
                        }
                    });
                } else{
                    // If we could'nt save the information in the database, we will show the user a message.
                    Toast.makeText(RegisterActivity.this, "You can't register with this email or password, please try again",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
