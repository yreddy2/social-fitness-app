/**
 * This class is for the activity that displays the users home screen.
 */
package com.example.fitnessappmap;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class HomeScreenActivity extends AppCompatActivity implements
        View.OnClickListener {


    Button progressButton;
    Button friendsButton;
    Button runButton;
    Button addButton;
    TextView displayName;
    User user;

    /**
     * The onCreate() method initializes the screen of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        user = (User) getIntent().getSerializableExtra("userObject");
        progressButton = (Button) findViewById(R.id.progress_button);
        friendsButton = (Button) findViewById(R.id.friends_button);
        runButton = (Button) findViewById(R.id.run_button);
        addButton = (Button) findViewById(R.id.add_friend_button);
        displayName = (TextView) findViewById(R.id.home_name);
        progressButton.setOnClickListener(this);
        friendsButton.setOnClickListener(this);
        runButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
        displayName.setText(user.getName());
    }

    /**
     * The onClick() method sets the functionality of the buttons on the screen
     */
    public void onClick(View v) {
        Intent intent = null;
        if (v.getId() == R.id.progress_button) {
            intent = new Intent(HomeScreenActivity.this,
                    ProgressActivity.class);
        } else if (v.getId() == R.id.friends_button) {
            intent = new Intent(HomeScreenActivity.this,
                    FriendsActivity.class);
        } else if (v.getId() == R.id.run_button) {
            intent = new Intent(HomeScreenActivity.this,
                    MapsActivity.class);
        } else if (v.getId() == R.id.add_friend_button) {
            intent = new Intent(HomeScreenActivity.this,
                    AddActivity.class);
        }
        intent.putExtra("userObject", user);
        startActivity(intent);
    }
}
