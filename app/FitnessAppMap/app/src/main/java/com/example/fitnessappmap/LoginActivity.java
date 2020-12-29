/**
 * This class is for the activity that shows the login page.
 */
package com.example.fitnessappmap;


import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener {


    Button loginButton;
    TextView signUpText;
    EditText userNameText;
    EditText passwordText;

    /**
     * The onCreate() method initializes the screen of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        loginButton = (Button) findViewById(R.id.login_button);
        signUpText = (TextView) findViewById(R.id.sign_up_text_link);
        userNameText = (EditText) findViewById(R.id.login_username);
        passwordText = (EditText) findViewById(R.id.login_password);
        loginButton.setOnClickListener(this);
        signUpText.setOnClickListener(this);
    }

    /**
     * The onClick() method sets the functionality of the buttons on the screen
     */
    public void onClick(View v) {
        Intent intent = null;
        if (v.getId() == R.id.login_button) {
            String jsonString =
                    API.getAttribute(userNameText.getText().toString(), "user");
            if (jsonString != null) {
                User user = JsonParser.stringToUser(jsonString);
                if (user != null) {
                    if (passwordText.getText().toString().equals(user
                            .getPassword())) {
                        intent = new Intent(LoginActivity.this,
                                HomeScreenActivity.class);
                        intent.putExtra("userObject", user);
                        Toast.makeText(getApplicationContext(),
                                NotificationMessages.LOGIN_SUCCESS,
                                Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                NotificationMessages.LOGIN_ERROR,
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            NotificationMessages.LOGIN_ERROR,
                            Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(),
                        NotificationMessages.LOGIN_ERROR, Toast.LENGTH_LONG)
                        .show();
            }
        } else if (v.getId() == R.id.sign_up_text_link) {
            intent = new Intent(LoginActivity.this,
                    SignUpActivity.class);
            startActivity(intent);
        }
    }
}
