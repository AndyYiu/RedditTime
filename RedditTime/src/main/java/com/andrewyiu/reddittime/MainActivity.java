package com.andrewyiu.reddittime;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * Created by andrew on 09/07/13.
 */
public class MainActivity extends FragmentActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFragment();
    }

    void addFragment() {
        getSupportFragmentManager().beginTransaction().add(R.id.fragments_holder, PostsFragment.newInstance("All")).commit();
    }

}
