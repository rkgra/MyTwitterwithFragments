package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TweetsArrayAdaptor;
import com.codepath.apps.mysimpletweets.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rgauta01 on 10/6/15.
 */
public abstract class TweetsListFragment extends Fragment {

    public TweetsArrayAdaptor aTweets;

    private ArrayList<Tweet> tweets;

    private ListView lvTweets;


    //inflation logic

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tweets_list, parent, false);

        // Find List View

        lvTweets = (ListView) view.findViewById(R.id.lvTweets);

        // Connect Adapter to List view


        lvTweets.setAdapter(aTweets);

        // Register Listener to handle ListView  click event


        //final TweetsArrayAdaptor finalTweetsArrayAdaptor = null;

        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                Log.i("DEBUG", "TweetsListFragment-->setupListViewOnClickListener");

                Tweet tweet = aTweets.getItem(pos);

                Log.i("DEBUG", "TweetsListFragment-->setupListViewOnClickListener-->getUser:" + tweet.getUser().getScreenName());


                getSelectedTweetDetails(tweet);
            }
        });

        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int max_id, int totalItemsCount) {

                customLoadMoreDataFromApi(max_id);

                Log.i("DEBUG", " in setOnScrollListener******TweetsListFragment");

                return true;
            }
        });


        return view;
    }

    protected abstract void getSelectedTweetDetails(Tweet tweet);


    /*
       To Register Listener to handle ListView  click event
       and store selected item in intent and pass to edit screen
     */

    //creation lifecycle event


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Create the arraylist (data source)

        tweets = new ArrayList<>();

        // Construct the adaptor from data sources

        aTweets = new TweetsArrayAdaptor(getActivity(), tweets);


    }

    // Abstract method to be overridden
    protected abstract void customLoadMoreDataFromApi(long maxId);

    public void addAll(List<Tweet> tweets) {

        aTweets.addAll(tweets);
    }


}
