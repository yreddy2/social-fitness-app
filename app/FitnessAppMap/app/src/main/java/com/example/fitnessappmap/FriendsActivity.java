/**
 * This class is for the activity that displays the users friends in a
 * leaderboard.
 */
package com.example.fitnessappmap;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.Collections;


public class FriendsActivity extends AppCompatActivity {


    ListView listView;
    String mNames[];
    String mProgress[];
    User userNames[];
    ArrayAdapter adapter;
    User user;
    Pair<Pair<String, Double>, User>[] friendPairs;

    /**
     * The onCreate() method initializes the screen of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        user = (User) getIntent().getSerializableExtra("userObject");
        String jsonDataFriends = API.getAttribute(user.getUserName(),
                "friends");
        if (jsonDataFriends != null) {
            String[] friendsUserNames =
                    JsonParser.stringListToArray(jsonDataFriends);
            if (friendsUserNames != null) {
                friendPairs = getFriendsData(friendsUserNames);
                if (friendPairs != null) {
                    mNames = new String[friendsUserNames.length];
                    mProgress = new String[friendsUserNames.length];
                    userNames = new User[friendsUserNames.length];
                    Arrays.sort(friendPairs, Collections.reverseOrder((p1, p2)
                            -> p1.first.second.compareTo(p2.first.second)));
                    for (int index = 0; index < friendPairs.length; index++) {
                        userNames[index] = friendPairs[index].second;
                        mNames[index] = "Rank " + (index + 1) + ":";
                        mProgress[index] = friendPairs[index].first.first +
                                " has ran " + friendPairs[index].first.second +
                                " Kilometers!";
                    }
                }
            }
        }
        if (mNames == null || mProgress == null) {
            mNames = new String[0];
            mProgress = new String[0];
            userNames = new User[0];
        }
        listView = (ListView) findViewById(R.id.friends_list);
        MyAdapter adapter = new MyAdapter(this, mNames, mProgress);
        listView.setAdapter(adapter);
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
                    (LayoutInflater) getApplicationContext()
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list_row_button,
                    parent, false);
            TextView myName = row.findViewById(R.id.list_name_ext);
            TextView myProgress = row.findViewById(R.id.list_progress_ext);
            Button progressButton = row.findViewById(R.id.select_button_ext);
            progressButton.setText("See Progress");
            progressButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(
                            FriendsActivity.this,
                            ProgressActivity.class);
                    intent.putExtra("userObject", userNames[position]);
                    startActivity(intent);
                }
            });
            myName.setText(rNames[position]);
            myProgress.setText(rProgress[position]);
            return row;
        }
    }

    private Pair<Pair<String, Double>, User>[] getFriendsData(
            String[] friendsUserName) {
        Pair<Pair<String, Double>, User>[] pairs =
                new Pair[friendsUserName.length];
        for (int index = 0; index < friendsUserName.length; index++) {
            String jsonStringFriendName =
                    API.getAttribute(friendsUserName[index], "user");
            String jsonStringFriendProgress =
                    API.getAttribute(friendsUserName[index],
                            "progress");
            if (jsonStringFriendName != null && jsonStringFriendProgress !=
                    null) {
                User userFriend = JsonParser.stringToUser(jsonStringFriendName);
                Double progressFriend =
                        JsonParser.stringToProgress(jsonStringFriendProgress);
                if (userFriend != null && progressFriend != null) {
                    pairs[index] =
                            new Pair<Pair<String, Double>, User>((
                                    new Pair<String, Double>(userFriend
                                            .getName() + " (" + userFriend
                                            .getUserName() + ")",
                                            progressFriend)), userFriend);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        return pairs;
    }
}
