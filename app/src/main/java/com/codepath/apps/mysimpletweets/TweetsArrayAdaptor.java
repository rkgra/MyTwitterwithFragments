package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by rgauta01 on 9/30/15.
 */
public class TweetsArrayAdaptor extends ArrayAdapter<Tweet> {


    public TweetsArrayAdaptor(Context context, List<Tweet> tweets) {

        super(context, 0, tweets);


    }
// TODO -View holder patter

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            relativeDate = relativeDate.replace("minutes", "m").replace("minutes", "m").replace("weeks", "w").replace("hours", "h").replace("seconds", "s").replace("minute", "m")
                    .replace("days", "d").replace("day", "d")
                    .replace("week", "w").replace("hour", "h")
                    .replace("second", "s").replace("ago", "").replace(" ", "").replace("in", "");
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1.Get tweet

        Tweet tweet = getItem(position);
        //2.Find or inflate tweet

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }
        //3.find the subview to fill with data in the template

        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);

        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);

        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);

        TextView tvCreatedAt = (TextView) convertView.findViewById(R.id.tvCreatedAt);


        //4.Populate data into the subviews

        tvUserName.setText(tweet.getUser().getScreenName());

        Log.d("DEBUG", "tvUserName ===>" + tvUserName.getText());

        tvBody.setText(tweet.getBody());

        Log.d("DEBUG", "tvBody ===>" + tvBody.getText());


        ivProfileImage.setImageResource(android.R.color.transparent);// clear out olage image from Recycle view

        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);

        Log.d("DEBUG", "ivProfileImage ===>" + ivProfileImage.toString());

        tvCreatedAt.setText(getRelativeTimeAgo(tweet.getCreatedAt()));

        Log.d("DEBUG", "tvCreatedAt ===>" + tvCreatedAt.getText() + "getRelativeTimeAgo-->" + getRelativeTimeAgo(tweet.getCreatedAt()));

        //5. Return the view to be inserted into the list

        return convertView;
    }
}
