package com.andrewyiu.reddittime;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * Created by andrew on 12/07/13.
 */
public class CommentsActivity extends FragmentActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Bundle extras = getIntent().getExtras();
        String url = null;
        if(extras != null) {
            url = extras.getString("perma");
            Log.e("PERMALINK: ", url);
        }
        addFragment("http://reddit.com" + url);
    }

    void addFragment(String perma) {
        getSupportFragmentManager().beginTransaction().add(R.id.comments_holder, CommentsFragment.newInstance(perma)).commit();
        Log.e("PERMALINK: ", perma);
    }

}
