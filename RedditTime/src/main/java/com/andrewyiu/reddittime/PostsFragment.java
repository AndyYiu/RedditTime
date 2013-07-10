package com.andrewyiu.reddittime;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 09/07/13.
 */
public class PostsFragment extends Fragment {

    ListView postsList;
    ArrayAdapter<Post> adapter;
    Handler handler;

    String subreddit;
    List<Post> posts;
    PostsHolder postsHolder;

    PostsFragment() {
        handler = new Handler();
        posts = new ArrayList<Post>();
    }

    //Makes a fragment with posts for the activity
    public static Fragment newInstance(String subreddit) {
        PostsFragment pf = new PostsFragment();
        pf.subreddit = subreddit;
        pf.postsHolder = new PostsHolder(pf.subreddit);
        return pf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {
        View v = inflater.inflate(R.layout.posts, container, false);
        postsList = (ListView) v.findViewById(R.id.posts_list);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialize();
    }

    //Starts the fragment and makes the custom list adapter
    private void initialize() {
        if(posts.size() == 0) {
            new Thread() {
                public void run() {
                    posts.addAll(postsHolder.fetchPosts());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            createAdapter();
                        }
                    });
                }
            }.start();
        } else {
            createAdapter();
        }
    }

    //Makes the custom list adapter to show the score, title, and details
    private void createAdapter() {
        if(getActivity() == null) {
            return;
        }
        adapter = new ArrayAdapter<Post>(getActivity(), R.layout.post_item, posts) {
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.post_item, parent, false);
                }
                convertView.setMinimumHeight(200);
                TextView postTitle, postDetails, postScore;
                postTitle = (TextView) convertView.findViewById(R.id.post_title);
                postDetails = (TextView) convertView.findViewById(R.id.post_details);
                postScore = (TextView) convertView.findViewById(R.id.post_score);
                postTitle.setText(posts.get(position).title);
                postDetails.setText(posts.get(position).getDetails());
                postScore.setText(posts.get(position).getScore());
                return convertView;
            }
        };
        postsList.setAdapter(adapter);
    }

}
