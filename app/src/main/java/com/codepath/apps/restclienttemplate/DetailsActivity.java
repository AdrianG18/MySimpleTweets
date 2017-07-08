package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.codepath.apps.restclienttemplate.R.drawable.ic_vector_heart;
import static com.codepath.apps.restclienttemplate.R.drawable.ic_vector_heart_stroke;
import static com.codepath.apps.restclienttemplate.R.drawable.ic_vector_retweet;
import static com.codepath.apps.restclienttemplate.R.drawable.ic_vector_retweet_stroke;
import static com.codepath.apps.restclienttemplate.R.id.Likes;
import static com.codepath.apps.restclienttemplate.R.id.ibRetweet;
import static com.codepath.apps.restclienttemplate.R.id.ivImage;
import static com.codepath.apps.restclienttemplate.R.id.tvCreatedAt;
import static com.codepath.apps.restclienttemplate.R.id.tvScreenName;
import static com.codepath.apps.restclienttemplate.R.id.tvUsername;

public class DetailsActivity extends AppCompatActivity {

    // instance fields
    AsyncHttpClient clientA;
    TwitterClient client;
    // the tweet to display
    Tweet tweet;

    Context context;

    @Nullable@BindView(R.id.ivProfileImage) ImageView ivProfileImage;
    @Nullable@BindView(R.id.ivImage) ImageView ivImage;
    @BindView(R.id.tvBody) TextView tvBody;
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvTagline) TextView tvTagline;
    @BindView(R.id.tvCreatedAt) TextView tvCreatedAt;
    @BindView(R.id.tvRetweet) TextView tvRetweet;
    @BindView(R.id.tvLike) TextView tvLike;
    @BindView(R.id.ibRetweet) ImageButton ibRetweet;
    @BindView(R.id.ibLike) ImageButton ibLike;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        // initialize the client
        clientA = new AsyncHttpClient();
        client = TwitterApp.getRestClient();
        context = getApplicationContext();

        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));
        Log.d("DetailsActivity", String.format("Showing details for %s's tweet",tweet.user.screenName));


        int heart = (tweet.favorited) ? ic_vector_heart : ic_vector_heart_stroke;
        int arrows = (tweet.retweeted) ? ic_vector_retweet : ic_vector_retweet_stroke;
        final String likes = (tweet.favoritesCount.equals("0")) ? "" : tweet.favoritesCount;
        String retweets = (tweet.retweetCount.equals("0")) ? "" : tweet.retweetCount;


        // set the title and overview
        tvBody.setText(tweet.body);
        tvCreatedAt.setText(tweet.createdAt);
        tvTagline.setText("@"+ tweet.user.screenName);
        tvName.setText(tweet.user.name);
        tvRetweet.setText(retweets);
        tvLike.setText(likes);
        ibLike.setImageResource(heart);
        ibRetweet.setImageResource(arrows);

        String imageUrl = tweet.user.profileImageUrl;
        String url = tweet.imageUrl;

        Glide.with(getApplicationContext())
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(getApplicationContext(), 150, 0))
                .into(ivProfileImage);

        Glide.with(getApplicationContext())
                .load(url)
                .bitmapTransform(new RoundedCornersTransformation(getApplicationContext(), 150, 0))
                .into(ivImage);

        ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tweet.favorited) {

                    // Network request sent to the statuses/update endpoint
                    client.unfavoriteTweet(tweet.uid, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("TwitterClient", response.toString());
                            tweet.favorited = false;
                            //tvLike.setTextColor(getColor(context,R.color.button_gray));
                            ibLike.setImageResource(ic_vector_heart_stroke);
                            tweet.favoritesCount = Integer.toString(Integer.parseInt(tweet.favoritesCount)-1);
                            tvLike.setText(
                                    (tweet.favoritesCount.equals("0"))? "" : tweet.favoritesCount
                            );
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
                else {
                    // Network request sent to the statuses/update endpoint
                    client.favoriteTweet(tweet.uid, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("TwitterClient", response.toString());
                            tweet.favorited = true;
                            //tvLike.setTextColor(getColor(context,R.color.favorited));
                            ibLike.setImageResource(ic_vector_heart);
                            tweet.favoritesCount = Integer.toString(Integer.parseInt(tweet.favoritesCount)+1);
                            tvLike.setText(tweet.favoritesCount);
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
            }
        });

        ibRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tweet.retweeted) {
                    tweet.retweeted = false;
                    //tvLike.setTextColor(context.getColor(context,R.color.button_gray));
                    ibRetweet.setImageResource(ic_vector_retweet_stroke);
                    tweet.retweetCount = Integer.toString(Integer.parseInt(tweet.retweetCount)+1);
                    tvRetweet.setText(tweet.retweetCount);
                }
                else {
                    tweet.retweeted = true;
                    //tvLike.setTextColor(context.getColor(context,R.color.retweeted));
                    ibRetweet.setImageResource(ic_vector_retweet);
                    tweet.retweetCount = Integer.toString(Integer.parseInt(tweet.retweetCount)+1);
                    tvRetweet.setText(tweet.retweetCount);
                }
            }
        });

        // set media image
        if (tweet.imageUrl != null) {
            ivImage.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(tweet.imageUrl)
                    .bitmapTransform(new RoundedCornersTransformation(context,20, 0))
                    .into(ivImage);
        } else {
            ivImage.setVisibility(View.GONE);
        }

        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 150, 0))
                .into(ivProfileImage);
    }
}
