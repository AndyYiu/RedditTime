package com.andrewyiu.reddittime;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by andrew on 12/07/13.
 */
public class CommentsLoader {

    private String url;

    CommentsLoader(String u) {
        url = u + ".json";
    }

    private Comment loadComment(JSONObject data, int level) {
        Comment comment = new Comment();
        try {
            comment.htmlText = data.getString("body");
            comment.author = data.getString("author");
            comment.points = (data.getInt("ups") - data.getInt("downs")) + "";
            comment.postedOn = new Date((long) data.getDouble("created_utc")).toString();
            comment.level = level;
        } catch(Exception e) {
            Log.d("Error", "Unable to parse comment :" + e);
        }
        return comment;
    }

    private void process(ArrayList<Comment> comments, JSONArray c, int level) throws Exception {
        for(int i = 0; i < c.length(); i++) {
            if(c.getJSONObject(i).optString("kind") == null) {
                continue;
            }
            if(c.getJSONObject(i).optString("kind").equals("t1") == false) {
                continue;
            }
            JSONObject data = c.getJSONObject(i).getJSONObject("data");
            Comment comment = loadComment(data, level);
            if(comment.author != null) {
                comments.add(comment);
                addReplies(comments, data, (level + 1));
            }
        }
    }

    private void addReplies(ArrayList<Comment> comments, JSONObject parent, int level) {
        try {
            if(parent.get("replies").equals("")) {
                return;
            }
            JSONArray r = parent.getJSONObject("replies").getJSONObject("data").getJSONArray("children");
            process(comments, r, level);
        } catch(Exception e) {
            Log.d("Error", "addReplies: " + e);
        }
    }

    ArrayList<Comment> fetchComments() {
        ArrayList<Comment> comments = new ArrayList<Comment>();
        try {
            String raw = RemoteData.readContents(url);
            JSONArray r = new JSONArray(raw).getJSONObject(1).getJSONObject("data").getJSONArray("children");
            process(comments, r, 0);
        } catch(Exception e) {
            Log.d("Error", "Could not connect: " + e);
        }
        return comments;
    }

}
