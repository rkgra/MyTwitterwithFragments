package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rgauta01 on 10/7/15.
 */
public class UserTimeLineFragment extends TweetsListFragment {


    ArrayList<Tweet> tweets;
    private TwitterClient client;

    // Creates a new fragment given an int and title
    //
    public static UserTimeLineFragment newInstance(String screen_name) {
        UserTimeLineFragment userTimeLineFragment = new UserTimeLineFragment();

        Log.i("DEBUG", "UserTimeLineFragment newInstance --screen_name-->" + screen_name);

        Bundle args = new Bundle();

        args.putString("screen_name", screen_name);

        userTimeLineFragment.setArguments(args);
        return userTimeLineFragment;
    }


    @Override
    protected void getSelectedTweetDetails(Tweet tweet) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();// Singleton

        populateUserTimeline(TwitterClient.DEFAULT_MAX_ID, true);

    }

    @Override
    protected void customLoadMoreDataFromApi(long maxId) {

        Log.d("DEBUG", "customLoadMoreDataFromApi---MentionsTimelineFragment--max_id--before>" + maxId);


        populateUserTimeline(getMax_id(), false);


        Log.d("DEBUG", "customLoadMoreDataFromApi---MentionsTimelineFragment--max_id--after>" + maxId);

    }

    // Send an API request to get timeline Json
    //Fill the Listview by creating the tweet objects from json

    private void populateUserTimeline(String max_id, final Boolean clearResults) {

        String screen_name = getArguments().getString("screen_name");

        Log.d("DEBUG", "populateUserTimeline-----max_id-->" + max_id + "clearResults>" + clearResults + "   <<<screen_name:" + screen_name);

        client.getUserTimeline(screen_name, max_id, new JsonHttpResponseHandler() {


            // Success

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray json) {

                Log.d("DEBUG", "populateUserTimelineonSuccess----->" + json.toString());
                Log.d("DEBUG", "populateUserTimeline onSuccess-statusCode---->" + statusCode);

                //JSON here
                //deSerialize JSON
                //Create Models and add them to adaptor
                //Load Models into ListView

                tweets = Tweet.fromJSONArray(json);

                if (clearResults) {
                    // Clear out the old data, because this is a different search.


                    //  fragmentTweetsList=null;

                }


                addAll(tweets);

            }


            //Failure


            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {

                Log.d("DEBUG", "populateMentionsTimeline-->errorResponse--->" + errorResponse.toString());
                Log.d("DEBUG", "populateMentionsTimeline -->onFailure------statusCode>>>>>>>" + statusCode);


            }


        });

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
