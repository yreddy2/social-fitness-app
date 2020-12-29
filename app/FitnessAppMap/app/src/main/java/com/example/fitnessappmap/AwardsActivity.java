/**
 * This class is the activity for the screen that displays a users milestones.
 */
package com.example.fitnessappmap;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class AwardsActivity extends AppCompatActivity {


    ListView listView;
    String mNames[];
    String mProgress[];
    ArrayAdapter adapter;
    User user;

    /**
     * The onCreate() method initializes the screen of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awards);
        user = (User) getIntent().getSerializableExtra("userObject");
        String jsonDataMilestones = API.getAttribute(user.getUserName(),
                "milestones");
        if (jsonDataMilestones != null) {
            String[] milestones =
                    JsonParser.stringListToArray(jsonDataMilestones);
            if (milestones != null) {
                mNames = new String[milestones.length];
                mProgress = new String[milestones.length];
                for (int index = 0; index < milestones.length; index++) {
                    mNames[index] = "Achievement " + (index + 1) + ":";
                    mProgress[index] = milestones[index];
                }
            }
        }
        if (mNames == null) {
            mNames = new String[0];
            mProgress = new String[0];
        }
        listView = (ListView) findViewById(R.id.awards_list);
        AwardsActivity.MyAdapter adapter = new AwardsActivity.MyAdapter(this,
                mNames, mProgress);
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
            super(c, R.layout.list_row, R.id.list_name, names);
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
            View row = layoutInflater.inflate(R.layout.list_row, parent, false);
            TextView myName = row.findViewById(R.id.list_name);
            TextView myProgress = row.findViewById(R.id.list_progress);
            myName.setText(rNames[position]);
            myProgress.setText(rProgress[position]);
            return row;
        }
    }
}