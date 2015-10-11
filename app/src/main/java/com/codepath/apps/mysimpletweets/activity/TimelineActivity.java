package com.codepath.apps.mysimpletweets.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.SmartFragmentStatePagerAdapter;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.fragments.HomeTimeLineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;

public class TimelineActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 25;
    public TweetsPagerAdapter adapterViewPager;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        getSupportActionBar().setTitle(R.string.home);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_action_tweet);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        //Get Viewpager

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Set the viewpager adopter for the pager

        adapterViewPager = new TweetsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapterViewPager);

        //Find the pager sliding tabstrip

        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);

        //Attach the tabstrip to the viewpager
        tabStrip.setViewPager(viewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    public void compostTweet(MenuItem item) {

        Toast.makeText(this, "Setting CLICKED", Toast.LENGTH_SHORT).show();

        // create an intent

        Intent intent = new Intent(this, ComposeTweetActivity.class);
        //start the new Activity

        startActivityForResult(intent, REQUEST_CODE);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (RESULT_OK == resultCode && requestCode == REQUEST_CODE) {


            Toast.makeText(getBaseContext(), "Tweet Posted", Toast.LENGTH_SHORT).show();

            String tweet = data.getExtras().getString("tweet");


            Tweet newTweet = (Tweet) data.getSerializableExtra("newTweet");


            Log.i("DEBUG", "onActivityResult  newTweet:" + tweet);

            // returns first Fragment item within the pager

            Log.i("DEBUG", "onActivityResult" + adapterViewPager.getRegisteredFragment(0).toString());


            HomeTimeLineFragment homeTimeLineFragment = (HomeTimeLineFragment) adapterViewPager.getRegisteredFragment(0);

            homeTimeLineFragment.aTweets.insert(newTweet, 0);

            homeTimeLineFragment.populateTimeline(TwitterClient.DEFAULT_MAX_ID, true);


        }

    }

    public void onProfileViewClick(MenuItem item) {

        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }


    // Return the order fo the fragment in the view pager

    public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter {


        private String tabTitles[] = {"Home", "Mentions"};


        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {

            if (position == 0) {
                return new HomeTimeLineFragment();

            } else if (position == 1) {
                return new MentionsTimelineFragment();
            } else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }


}
