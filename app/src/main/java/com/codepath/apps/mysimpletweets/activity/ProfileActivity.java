package com.codepath.apps.mysimpletweets.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.fragments.UserTimeLineFragment;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

public class ProfileActivity extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Get the screen name from the activity that launches this

        String screen_name = getIntent().getStringExtra("screen_name");

        Log.i("DEBUG", "ProfileActivity-->onCreate-->screen_name.>" + screen_name);


        if (screen_name != null) {

            //Get other  user Details


            TwitterApplication.getRestClient().getOtherUserDetails(screen_name, new JsonHttpResponseHandler() {


                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                            user = User.fromJSON(response);
                            getSupportActionBar().setTitle("@ " + user.getScreenName());
                            populateProfileHeader(user);


                        }
                    }
            );


        } else {

            //Get authenticated  user Details


            TwitterApplication.getRestClient().getUserDetails(new JsonHttpResponseHandler() {


                                                                  @Override
                                                                  public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                                                                      user = User.fromJSON(response);
                                                                      getSupportActionBar().setTitle("@ " + user.getScreenName());
                                                                      populateProfileHeader(user);


                                                                  }
                                                              }
            );

        }


        if (savedInstanceState == null) {

            // Create the user timeline fragment
            UserTimeLineFragment userTimeLineFragment = UserTimeLineFragment.newInstance(screen_name);

            // Display user timeline  fragment within this activity

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.flContainer, userTimeLineFragment);

            ft.commit();// change the fragment

            Log.i("DEBUG", "ProfileActivity-->onCreate-->savedInstanceState.>");


        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateProfileHeader(User user) {

        Log.i("DEBUG", "ProfileActivity-->populateProfileHeader-->userName.>" + user.getName());
        Log.i("DEBUG", "ProfileActivity-->populateProfileHeader-->getScreenName.>" + user.getScreenName());


        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);


        tvName.setText(user.getName());
        tvTagline.setText(user.getTagline());

        tvFollowers.setText(user.getFollowersCount() + " Followers");

        tvFollowing.setText(user.getFollowingCount() + " Following");

        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);


    }
}
