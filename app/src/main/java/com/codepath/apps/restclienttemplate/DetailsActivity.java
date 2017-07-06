package com.codepath.apps.restclienttemplate;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpClient;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.codepath.apps.restclienttemplate.R.id.tvCreatedAt;
import static com.codepath.apps.restclienttemplate.R.id.tvScreenName;
import static com.codepath.apps.restclienttemplate.R.id.tvUsername;

public class DetailsActivity extends AppCompatActivity {

    // instance fields
    AsyncHttpClient client;
    // the tweet to display
    Tweet tweet;

    @Nullable@BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @BindView(R.id.tvBody) TextView tvBody;
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvTagline) TextView tvTagline;
    @BindView(R.id.tvCreatedAt) TextView tvCreatedAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        // initialize the client
        client = new AsyncHttpClient();

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        Log.d("DetailsActivity", String.format("Showing details for %s's tweet",tweet.user.screenName));

        // set the title and overview
        tvBody.setText(tweet.body);
        tvCreatedAt.setText(tweet.createdAt);
        tvTagline.setText("@"+ tweet.user.screenName);
        tvName.setText(tweet.user.name);

        String imageUrl = tweet.user.profileImageUrl;

        Glide.with(getApplicationContext())
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(getApplicationContext(), 150, 0))
                .into(ivProfileImage);

    }
}
