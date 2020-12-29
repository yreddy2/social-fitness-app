package com.example.fitnessappmap;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class SignUpActivity extends AppCompatActivity implements
        View.OnClickListener {


    private static final String SIGN_ERROR = "Error Creating User!";
    private static final String SIGN_SUCCESS = "Created User!";
    Button signUpButton;
    EditText userNameText;
    EditText nameText;
    EditText passwordText;

    /**
     * The onCreate() method initializes the screen of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signUpButton = (Button) findViewById(R.id.signup_button);
        userNameText = (EditText) findViewById(R.id.signup_username);
        nameText = (EditText) findViewById(R.id.signup_name);
        passwordText = (EditText) findViewById(R.id.signup_password);
        signUpButton.setOnClickListener(this);
    }

    /**
     * The onClick() method sets the functionality of the buttons on the screen
     */
    public void onClick(View v) {
        String userName = userNameText.getText().toString();
        String password = passwordText.getText().toString();
        String name = nameText.getText().toString();
        if (!userName.equals("") && !password.equals("") && !name.equals("")) {
            String jsonString = API.getAttribute(userName, "user");
            if (jsonString != null) {
                User user = JsonParser.stringToUser(jsonString);
                if (user == null) {
                    User newUser = new User(userName, password, name, null,
                            null, null);
                    String jsonData = JsonParser.userToString(newUser);
                    if (jsonData != null) {
                        boolean status = API.postUser(jsonData);
                        if (status) {
                            Intent intent = new Intent(
                                    SignUpActivity.this,
                                    LoginActivity.class);
                            Toast.makeText(getApplicationContext(),
                                    NotificationMessages.SIGN_SUCCESS,
                                    Toast.LENGTH_LONG).show();
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    NotificationMessages.SIGN_ERROR,
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                NotificationMessages.SIGN_ERROR,
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            NotificationMessages.SIGN_ERROR,
                            Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(),
                        NotificationMessages.SIGN_ERROR,
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    NotificationMessages.SIGN_ERROR, Toast.LENGTH_LONG).show();
        }
    }
}
