package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by adrian18 on 6/26/17.
 */

public class Tweet {

    // list out the attributes
    public String body;
    public long uid; //database ID for the tweet
    public User user;
    public String createdAt;
    public String relativeDate;

    // deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.relativeDate = TimeFormatter.getTimeDifference(tweet.createdAt);
        return tweet;
    }
}
