package com.codepath.apps.mysimpletweets.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.activity.ProfileActivity;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rgauta01 on 10/6/15.
 */
public class HomeTimeLineFragment extends TweetsListFragment {

    ArrayList<Tweet> tweets;
    private TwitterClient client = TwitterApplication.getRestClient();// Singleton'
    private ListView lvTweets;


    @Override
    protected void getSelectedTweetDetails(Tweet tweet) {

        Log.i("DEBUG", " HomeTimeLineFragment-->getSelectedTweetDetails-->getScreenName-->" + tweet.getUser().getScreenName());

        // Create the user timeline fragment

        Intent intent;
        intent = new Intent(getActivity(), ProfileActivity.class);

        intent.putExtra("screen_name", tweet.getUser().getScreenName());

        startActivity(intent);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        populateTimeline(TwitterClient.DEFAULT_MAX_ID, true);

    }

    @Override
    protected void customLoadMoreDataFromApi(long maxId) {

        Log.d("DEBUG", "customLoadMoreDataFromApi---HomeTimeLineFragment--max_id--before>" + maxId);


        populateTimeline(getMax_id(), false);


        Log.d("DEBUG", "customLoadMoreDataFromApi---HomeTimeLineFragment--max_id--after>" + maxId);

    }

    // Send an API request to get timeline Json
    //Fill the Listview by creating the tweet objects from json


    public void populateTimeline(String max_id, final Boolean clearResults) {

        Log.d("DEBUG", "populateTimeline-----max_id-->" + max_id + "clearResults>" + clearResults);

        client.getTimeline(max_id, new JsonHttpResponseHandler() {
            // Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {

                Log.d("DEBUG", "onSuccess----->" + json.toString());

                //JSON here
                //deSerialize JSON
                //Create Models and add them to adaptor
                //Load Models into ListView

                // ArrayList<Tweet> tweets = Tweet.fromJSONArray(json);
                tweets = Tweet.fromJSONArray(json);


                if (clearResults) {


                }


                addAll(tweets);


            }


            //Failure


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                Log.d("DEBUG", "errorResponse--->" + errorResponse.toString());
                Log.d("DEBUG", "onFailure------statusCode>>>>>>>" + statusCode);


            }


        });

    }


    public void addNewTweet(Tweet newTweet) {


        aTweets.add(newTweet);
        aTweets.notifyDataSetChanged();


    }

    private String getMax_id() {
        String max_id = TwitterClient.DEFAULT_MAX_ID;


        if (tweets != null) {

            int lastTweet = tweets.size() - 1;
            max_id = String.valueOf(tweets.get(lastTweet).getuId());


        }

        Log.d("DEBUG", "max_id-------->" + max_id);


        return max_id;

    }
}
