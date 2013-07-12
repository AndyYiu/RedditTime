package com.andrewyiu.reddittime;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 12/07/13.
 */
public class CommentsFragment extends Fragment {

    ListView commentsList;
    ArrayAdapter<Comment> adapter;
    Handler handler;

    String post;
    List<Comment> comments;
    CommentsLoader commentsLoader;

    CommentsFragment() {
        handler = new Handler();
        comments = new ArrayList<Comment>();
    }

    public static Fragment newInstance(String post) {
        CommentsFragment cf = new CommentsFragment();
        cf.post = post;
        cf.commentsLoader = new CommentsLoader(cf.post);
        return cf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.comments, container, false);
        commentsList = (ListView) v.findViewById(R.id.comments_list);
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

    private void initialize() {
        if(comments.size() == 0) {
            new Thread() {
                public void run() {
                    comments.addAll(commentsLoader.fetchComments());
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

    private void createAdapter() {
        if(getActivity() == null) {
            return;
        }
        adapter = new ArrayAdapter<Comment>(getActivity(), R.layout.comment_item, comments) {
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.comment_item, null);
                }
                convertView.setMinimumHeight(200);
                TextView commentBody, commentDetails, commentScore;
                commentBody = (TextView) convertView.findViewById(R.id.comment_body);
                commentDetails = (TextView) convertView.findViewById(R.id.comment_details);
                commentScore = (TextView) convertView.findViewById(R.id.comment_score);
                commentBody.setText(comments.get(position).htmlText);
                commentDetails.setText(comments.get(position).getDetails());
                commentScore.setText(comments.get(position).getPoints());
                return convertView;
            }
        };
        commentsList.setAdapter(adapter);
    }

}
