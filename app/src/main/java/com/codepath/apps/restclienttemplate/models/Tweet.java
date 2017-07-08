package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by adrian18 on 6/26/17.
 */
@Parcel
public class Tweet {

    // list out the attributes
    public String body;
    public long uid; //database ID for the tweet
    public User user;
    public String createdAt;
    public String handle;
    public String relativeDate;
    public String replyCount;
    public String retweetCount;
    public String favoritesCount;
    public boolean favorited;
    public boolean retweeted;
    public String idString;
    public String imageUrl;
    public String url;
    public String displayUrl;

    // deserialize the JSON
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        // extract the values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.relativeDate = TimeFormatter.getTimeDifference(tweet.createdAt);
        tweet.retweetCount = jsonObject.getString("retweet_count");
        tweet.favoritesCount = jsonObject.getString("favorite_count");
        tweet.favorited = jsonObject.getBoolean("favorited");
        tweet.retweeted = jsonObject.getBoolean("retweeted");

        if (jsonObject.has("entities")) {
            JSONObject object = jsonObject.getJSONObject("entities");
            if (object.has("media")) {
                JSONArray array = object.getJSONArray("media");
                if (array.length()>0) {
                    JSONObject object2 = array.getJSONObject(0);
                    if (object2.has("media_url_https")) {
                        tweet.imageUrl = object2.getString("media_url_https");
                    }
                }
            }
            if (object.has("urls")) {
                JSONArray urlArray = object.getJSONArray("urls");
                if (urlArray.length() > 0) {
                    JSONObject urlObject = urlArray.getJSONObject(0);
                    if (urlObject.has("url") && urlObject.has("display_url")) {
                        tweet.url = urlObject.getString("url");
                        tweet.displayUrl = urlObject.getString("display_url");
                    }
                }
            }
        }

        return tweet;
    }
}
