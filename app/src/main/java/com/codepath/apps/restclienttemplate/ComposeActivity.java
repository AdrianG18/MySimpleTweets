package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.User;

import static com.codepath.apps.restclienttemplate.TwitterApp.getRestClient;

public class ComposeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

    }

    public void onTweet() {

        // instance variables
        TwitterClient client = getRestClient();

        EditText etTweet = (EditText) findViewById(R.id.etTweet);
        // Prepare data intent
        Intent data = new Intent();
        // Pass relevant data back as a result
        data.putExtra("tweet", etTweet.getText().toString());
        // Network request sent to the statuses/update endpoint

        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish(); //closes the activity, pass data to parent
    }


}
