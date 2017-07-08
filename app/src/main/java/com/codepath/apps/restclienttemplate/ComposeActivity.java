package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static android.graphics.Color.DKGRAY;
import static com.codepath.apps.restclienttemplate.R.id.btnSend;
import static com.codepath.apps.restclienttemplate.R.id.ivProfileImage;
import static com.codepath.apps.restclienttemplate.R.id.tvFollowers;
import static com.codepath.apps.restclienttemplate.R.id.tvFollowing;
import static com.codepath.apps.restclienttemplate.R.id.tvTagline;


public class ComposeActivity extends AppCompatActivity {


    // instance variables
    TwitterClient client;
    EditText etTweet;
    TextView tvCount;
    Button btnSend;
    TextView tvName;
    TextView tvScreenName;
    ImageView ivProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        // TODO: ButterKnife
        client = TwitterApp.getRestClient();
        etTweet = (EditText) findViewById(R.id.etTweet);
        tvCount = (TextView) findViewById(R.id.tvCount);
        btnSend = (Button) findViewById(R.id.btnSend);
        tvName = (TextView) findViewById(R.id.tvName);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);


      //  tvName.setText(user.name);

        etTweet.addTextChangedListener(mTextEditorWatcher);


        client = TwitterApp.getRestClient();
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // deserialize the User object
                try {
                    User user = User.fromJSON(response);
                    // populate the user headline
                    populateHeadline(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
    });}


    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a text view to the current length
            int length = s.length();
            int tColor = (length>140) ? Color.RED : Color.DKGRAY;
            int bColor = (length>140 || length == 0) ? Color.parseColor("#6A00ACED") : Color.parseColor("#00aced");
            tvCount.setText(String.valueOf(140-length));
            tvCount.setTextColor(tColor);
            btnSend.setBackgroundColor(bColor);
        }

        public void afterTextChanged(Editable s) {
        }
    };

    public void onTweet(View v) {

        // Network request sent to the statuses/update endpoint
        client.sendTweet(etTweet.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
                try {
                    Tweet tweet = Tweet.fromJSON(response);
                    Intent intent = new Intent(ComposeActivity.this, TimelineActivity.class);
                    intent.putExtra(Tweet.class.getName(), Parcels.wrap(tweet));
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });

    }

    public void populateHeadline(User user) {
        tvName.setText(user.name);
        tvScreenName.setText("@"+user.screenName);
        // load profile image with Glide
        Glide.with(this)
                .load(user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(this,150,0))
                .into(ivProfileImage);

    }


}
