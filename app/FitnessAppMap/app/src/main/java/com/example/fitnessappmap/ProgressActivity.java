/**
 * This class is for the activity that displays the users previous runs.
 */
package com.example.fitnessappmap;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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


public class ProgressActivity extends AppCompatActivity implements
        View.OnClickListener {


    ListView listView;
    String mNames[];
    String mProgress[];
    ArrayAdapter adapter;
    Button awardsButton;
    TextView progressText;
    User user;

    /**
     * The onCreate() method initializes the screen of the app
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);
        user = (User) getIntent().getSerializableExtra("userObject");
        progressText = (TextView) findViewById(R.id.progress_text);
        String jsonDataProgress = API.getAttribute(user.getUserName(),
                "progress");
        if (jsonDataProgress != null) {
            Double progress = JsonParser.stringToProgress(jsonDataProgress);
            if (progress != null) {
                progressText.setText(progress + " Kilometers!");
            }
        }
        String jsonDataRuns = API.getAttribute(user.getUserName(), "runs");
        if (jsonDataRuns != null) {
            System.out.println(jsonDataRuns);
            Double[] runs = JsonParser.stringToRuns(jsonDataRuns);
            if (runs != null) {
                System.out.println("h2");
                mNames = new String[runs.length];
                mProgress = new String[runs.length];
                for (int index = 0; index < runs.length; index++) {
                    System.out.println("h3");
                    mNames[index] = "Run " + (index + 1) + " Distance:";
                    mProgress[index] = runs[index] + " Kilometers!";
                }
            }
        }
        if (mNames == null) {
            System.out.println("here");
            mNames = new String[0];
            mProgress = new String[0];
        }
        listView = (ListView) findViewById(R.id.progress_list);
        ProgressActivity.MyAdapter adapter =
                new ProgressActivity.MyAdapter(this, mNames, mProgress);
        listView.setAdapter(adapter);
        awardsButton = (Button) findViewById(R.id.awards_button);
        awardsButton.setOnClickListener(this);
    }

    /**
     * The onClick() method sets the functionality of the buttons on the screen
     */
    public void onClick(View v) {
        Intent intent = new Intent(ProgressActivity.this,
                AwardsActivity.class);
        intent.putExtra("userObject", user);
        startActivity(intent);
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
