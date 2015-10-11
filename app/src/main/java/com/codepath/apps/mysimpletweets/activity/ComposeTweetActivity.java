package com.codepath.apps.mysimpletweets.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ComposeTweetActivity extends AppCompatActivity {

    private static final String MAX_FIELD_LENGTH = "140";
    public User user;
    public Tweet newTweet;
    private TextView tvUserName;
    private TextView tvCharLength;
    private ImageView ivProfileImage;
    private EditText etCompose;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_tweet);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_action_tweet);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        tvUserName = (TextView) findViewById(R.id.tvUserName);

        tvCharLength = (TextView) findViewById(R.id.tvCharLength);

        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

        etCompose = (EditText) findViewById(R.id.tvCompose);


        client = TwitterApplication.getRestClient();// Singleton

        populateUerDetails();

        tvCharLength.setText(MAX_FIELD_LENGTH);

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
                tvCharLength.setText(String.valueOf(Integer.valueOf(MAX_FIELD_LENGTH) - s.length()));
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // Fires right before text is changing
                tvCharLength.setText("140");
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Fires right after the text has changed

            }
        });


    }

    private void populateUerDetails() {

        client.getUserDetails(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                user = User.fromJSON(response);

                tvUserName.setText("@" + user.getScreenName());
                Picasso.with(getBaseContext()).load(user.getProfileImageUrl()).into(ivProfileImage);


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }

    private void postComposedTweet() {
        final String tweet = etCompose.getText().toString();
        TwitterApplication.getRestClient().postComposeTweet(tweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                newTweet = new Tweet();

                newTweet.setUser(user);
                newTweet.setBody(tweet);

                Log.i("DEBUG", "giveDate():" + giveDate());

                newTweet.setCreatedAt(giveDate());

                Intent data = new Intent();

                data.putExtra("tweet", tweet);
                data.putExtra("newTweet", newTweet);


                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

            }
        });
    }


    public String giveDate() {

        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(twitterFormat);


        return sdf.format(cal.getTime());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compose_tweet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_post_tweet) {

            postComposedTweet();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
