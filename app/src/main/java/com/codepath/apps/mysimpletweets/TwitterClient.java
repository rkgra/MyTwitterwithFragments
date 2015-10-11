package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "zfxEKaKvL6PPoX2pUsJvBJ8js";       // Change this
    public static final String REST_CONSUMER_SECRET = "ASOEG53tR0y0Ig7ycxqEMeEWTL0YfnpkTtg5LKjV6tvFxmtoZC"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://rkgsimpletweets"; // Change this (here and in manifest)
    public static final String DEFAULT_MAX_ID = "-1";


    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }

	/*

	Get the home timeline for User

	GET statuses/home_timeline

	statuses/home_timeline.json

			count=25
	        since_id=1
	*/
    /* 1. Define the endjava.lang.Stringpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */

    public void getTimeline(String max_id, AsyncHttpResponseHandler handler) {

        String apiUrl = getApiUrl("statuses/home_timeline.json");

        RequestParams params = new RequestParams();
        params.put("count", "25");
        params.put("since_id", "1");
        if (max_id != DEFAULT_MAX_ID) {

            params.put("max_id", max_id);

        }

        Log.d("DEBUG", " getTimeline--max_id-->" + max_id);
        Log.d("DEBUG", " getTimeline--params.toString()-->" + params.toString());

        getClient().get(apiUrl, params, handler);


    }

    public void getMentionsTimeline(String max_id, AsyncHttpResponseHandler handler) {

        String apiUrl = getApiUrl("statuses/mentions_timeline.json");

        RequestParams params = new RequestParams();
        params.put("count", "25");

        if (max_id != DEFAULT_MAX_ID) {

            params.put("max_id", max_id);

        }

        Log.d("DEBUG", " getMentionsTimeline--max_id-->" + max_id);
        Log.d("DEBUG", " getMentionsTimeline--apiUrl-->" + apiUrl);

        Log.d("DEBUG", " getMentionsTimeline--params.toString()-->" + params.toString());

        getClient().get(apiUrl, params, handler);
    }

    public void getUserTimeline(String screen_name, String max_id, AsyncHttpResponseHandler handler) {

        String apiUrl = getApiUrl("statuses/user_timeline.json");

        RequestParams params = new RequestParams();
        params.put("count", "25");
        params.put("screen_name", screen_name);

        if (max_id != DEFAULT_MAX_ID) {

            params.put("max_id", max_id);

        }

        Log.d("DEBUG", " getMentionsTimeline--max_id-->" + max_id);
        Log.d("DEBUG", " getUserTimeline--apiUrl-->" + apiUrl);

        Log.d("DEBUG", " getUserTimeline--params.toString()-->" + params.toString());

        getClient().get(apiUrl, params, handler);
    }

    public void getOtherUserDetails(String screen_name, AsyncHttpResponseHandler handler) {

        String apiUrl = getApiUrl("users/show.json");

        RequestParams params = new RequestParams();

        params.put("screen_name", screen_name);


        Log.d("DEBUG", " getUserTimeline--apiUrl-->" + apiUrl);

        Log.d("DEBUG", " getUserTimeline--params.toString()-->" + params.toString());

        getClient().get(apiUrl, params, handler);
    }

    public void getUserDetails(AsyncHttpResponseHandler handler) {

        String apiUrl = getApiUrl("account/verify_credentials.json");

        RequestParams params = new RequestParams();

        params.put("skip_status", 1);

        params.put("include_entities", "false");

        getClient().get(apiUrl, params, handler);


    }


    public void postComposeTweet(String composeText, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");

        Log.d("DEBUG", "postComposeTweet apiUrl-->:" + apiUrl + "<<<composeText>>>:" + composeText);

        RequestParams params = new RequestParams();

        params.put("status", composeText.toString());

        getClient().post(apiUrl, params, handler);


    }


}