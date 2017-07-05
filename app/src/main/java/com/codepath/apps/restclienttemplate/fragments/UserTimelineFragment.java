package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.restclienttemplate.TwitterApp;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by adrian18 on 7/5/17.
 */

public class UserTimelineFragment extends TweetsListFragment {

        TwitterClient client;

        public static UserTimelineFragment newInstance(String screenName) {
            UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
            Bundle args = new Bundle();
            args.putString("screen_name", screenName);
            userTimelineFragment.setArguments(args);
            return userTimelineFragment;
        }
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            client = TwitterApp.getRestClient();
            populateTimeline();

        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        @Override
        public void populateTimeline() {
            // comes from the activity
            String screenName = getArguments().getString("screen_name");
//        showProgressBar();
            client.getUserTimeline(screenName, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    Log.d("TwitterClient", response.toString());
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                Log.d("TwitterClient", response.toString());

                    tweetAdapter.clear();

                    addItems(response);

                    swipeContainer.setRefreshing(false);
//                hideProgressBar();
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

