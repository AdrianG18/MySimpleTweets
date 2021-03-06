package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.support.v4.content.ContextCompat.getColor;
import static com.codepath.apps.restclienttemplate.R.color.button_gray;
import static com.codepath.apps.restclienttemplate.R.color.favorited;
import static com.codepath.apps.restclienttemplate.R.drawable.ic_vector_heart;
import static com.codepath.apps.restclienttemplate.R.drawable.ic_vector_heart_stroke;
import static com.codepath.apps.restclienttemplate.R.drawable.ic_vector_retweet;
import static com.codepath.apps.restclienttemplate.R.drawable.ic_vector_retweet_stroke;
import static com.codepath.apps.restclienttemplate.R.id.etTweet;
import static com.codepath.apps.restclienttemplate.R.id.ivImage;

/**
 * Created by adrian18 on 6/26/17.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Tweet> mTweets;
    Context context;
    TwitterClient client;
    private TweetAdapterListener mListener;

    // define an interface required by the ViewHolder
    public interface TweetAdapterListener {
        void onItemSelected(View view, int position, boolean b);
    }
    // pass in the Tweets array in the constructor
    public TweetAdapter(List<Tweet> tweets, TweetAdapterListener listener) {
        mTweets = tweets;
        mListener = listener;
    }

    // for each row, inflate the layout and cache references into ViewHolder

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        client = TwitterApp.getRestClient();

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    // bind the values based on the position of the element

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // get the data according to position
        final Tweet tweet = mTweets.get(position);
        int heart = (tweet.favorited) ? ic_vector_heart : ic_vector_heart_stroke;
        int arrows = (tweet.retweeted) ? ic_vector_retweet : ic_vector_retweet_stroke;
        final String likes = (tweet.favoritesCount.equals("0")) ? "" : tweet.favoritesCount;
        String retweets = (tweet.retweetCount.equals("0")) ? "" : tweet.retweetCount;

        // populate the views according to this data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvHandle.setText("@"+tweet.user.screenName);
        holder.tvBody.setText(tweet.body);
        holder.tvRelativeDate.setText(tweet.relativeDate);
        holder.tvRetweet.setText(retweets);
        holder.tvLike.setText(likes);
        holder.ibLike.setImageResource(heart);
        holder.ibRetweet.setImageResource(arrows);

        // set on click listeners
        //TODO: set on click listener for reply
        holder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemSelected(view,position,true);
            }
        });
        holder.ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tweet.favorited) {

                    // Network request sent to the statuses/update endpoint
                    client.unfavoriteTweet(tweet.uid, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("TwitterClient", response.toString());
                                tweet.favorited = false;
                                holder.tvLike.setTextColor(getColor(context,R.color.button_gray));
                                holder.ibLike.setImageResource(ic_vector_heart_stroke);
                                tweet.favoritesCount = Integer.toString(Integer.parseInt(tweet.favoritesCount)-1);
                                holder.tvLike.setText(
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
                            holder.tvLike.setTextColor(getColor(context,R.color.favorited));
                            holder.ibLike.setImageResource(ic_vector_heart);
                            tweet.favoritesCount = Integer.toString(Integer.parseInt(tweet.favoritesCount)+1);
                            holder.tvLike.setText(tweet.favoritesCount);
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

        holder.ibRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tweet.retweeted) {
                    tweet.retweeted = false;
                    holder.tvLike.setTextColor(getColor(context,button_gray));
                    holder.ibRetweet.setImageResource(ic_vector_retweet_stroke);
                    tweet.retweetCount = Integer.toString(Integer.parseInt(tweet.retweetCount)+1);
                    holder.tvRetweet.setText(tweet.retweetCount);
                }
                else {
                    tweet.retweeted = true;
                    holder.tvLike.setTextColor(getColor(context,R.color.retweeted));
                    holder.ibRetweet.setImageResource(ic_vector_retweet);
                    tweet.retweetCount = Integer.toString(Integer.parseInt(tweet.retweetCount)+1);
                    holder.tvRetweet.setText(tweet.retweetCount);
                }
            }
        });

        // set media image
        if (tweet.imageUrl != null) {
            holder.ivImage.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(tweet.imageUrl)
                    .bitmapTransform(new RoundedCornersTransformation(context,20, 0))
                    .into(holder.ivImage);
        } else {
            holder.ivImage.setVisibility(View.GONE);
        }

        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 150, 0))
                .into(holder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    // create ViewHolder class

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public ImageView ivImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvHandle;
        public TextView tvRelativeDate;
        public TextView tvRetweet;
        public TextView tvLike;
        public ImageButton ibLike;
        public ImageButton ibRetweet;
        public ViewHolder(View itemView) {
            super(itemView);

            // perform findViewById lookups
            // TODO: ButterKnife(?)
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvHandle = (TextView) itemView.findViewById(R.id.tvHandle);
            tvRelativeDate = (TextView) itemView.findViewById(R.id.tvRelativeDate);
            tvRetweet = (TextView) itemView.findViewById(R.id.tvRetweet);
            tvLike = (TextView) itemView.findViewById(R.id.tvLike);
            ibLike = (ImageButton) itemView.findViewById(R.id.ibLike);
            ibRetweet = (ImageButton)itemView.findViewById(R.id.ibRetweet);

            // handle row click event
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        // get the position of row element
                        int position = getAdapterPosition();
                        // fire the listener callback
                        mListener.onItemSelected(view, position, false);
                    }
                }
            });

        }
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

}
