package com.andrewyiu.reddittime;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by andrew on 12/07/13.
 */
public class CommentsActivity extends FragmentActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        addFragment();
    }

    void addFragment() {
        getSupportFragmentManager().beginTransaction().add(R.id.comments_holder, CommentsFragment.newInstance("http://www.reddit.com/r/gunpla/comments/1i49n1/my_gunpla_haul_from_my_japan_trip/")).commit();
    }

}
