/**
 * This class is the Activity for the screen that allows the user to add a
 * friend.
 */
package com.example.fitnessappmap;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;


public class AddActivity extends AppCompatActivity implements
        View.OnClickListener {


    ListView listView;
    String mNames[];
    String mProgress[];
    ArrayList<String> friendsList;
    ArrayAdapter adapter;
    Button searchButton;
    EditText findUserText;
    User user;

    /**
     * The onCreate() method initializes the screen of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        user = (User) getIntent().getSerializableExtra("userObject");
        findUserText = (EditText) findViewById(R.id.find_user);
        searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.find_friend_list);
        if (user.getFriends().length != 0) {
            friendsList = new ArrayList<>(Arrays.asList(user.getFriends()));
        } else {
            String[] temp = {"A", "B"};
            friendsList = new ArrayList<>(Arrays.asList(temp));
        }
    }

    /**
     * The onClick() method sets the functionality of the buttons on the screen
     */
    public void onClick(View v) {
        String userNameFragment = findUserText.getText().toString();
        if (!userNameFragment.equals("")) {
            String jsonString = API.getAttribute(userNameFragment,
                    "users");
            if (jsonString != null) {
                User[] searchList =
                        JsonParser.stringToMultipleUsers(jsonString);
                if (searchList != null) {
                    mNames = new String[searchList.length];
                    mProgress = new String[searchList.length];
                    for (int index = 0; index < searchList.length; index++) {
                        mNames[index] = searchList[index].getUserName();
                        mProgress[index] = searchList[index].getName();
                    }
                    MyAdapter adapter = new MyAdapter(this, mNames,
                            mProgress);
                    listView.setAdapter(adapter);
                }
            }
        }
        if (mNames == null) {
            mNames = new String[0];
            mProgress = new String[0];
        }
    }


    /**
     * Custom Adapter class is used to create a scrollable list on screen
     */
    class MyAdapter extends ArrayAdapter<String> {


        Context context;
        String rNames[];
        String rProgress[];

        MyAdapter(Context c, String names[], String progress[]) {
            super(c, R.layout.list_row_button, R.id.list_name_ext, names);
            this.context = c;
            this.rNames = names;
            this.rProgress = progress;
        }

        /**
         * This method populates the scrollable list on screen.
         */
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView,
                            @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) getApplicationContext().
                            getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list_row_button,
                    parent, false);
            TextView myName = row.findViewById(R.id.list_name_ext);
            TextView myProgress = row.findViewById(R.id.list_progress_ext);
            Button addButton = row.findViewById(R.id.select_button_ext);
            if (friendsList.contains(rNames[position])) {
                addButton.setText("Added!");
            }
            myName.setText(rNames[position]);
            myProgress.setText(rProgress[position]);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String jsonDataFriend =
                            JsonParser.friendToString(rNames[position]);
                    if (jsonDataFriend != null) {
                        boolean status = API.addAttribute(jsonDataFriend,
                                user.getUserName(), "friend");
                        if (status) {
                            Toast.makeText(getApplicationContext(),
                                    NotificationMessages.ADD_SUCCESS,
                                    Toast.LENGTH_LONG).show();
                            addButton.setText("Added!");
                            System.out.println(friendsList);
                            friendsList.add(rNames[position]);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    NotificationMessages.ADD_ERROR,
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                }
            });
            return row;
        }
    }
}
