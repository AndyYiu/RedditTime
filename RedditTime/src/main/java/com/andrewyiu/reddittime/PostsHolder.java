package com.andrewyiu.reddittime;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 09/07/13.
 */
public class PostsHolder {

    private final String URL_TEMPLATE = "http://www.reddit.com/r/SUBREDDIT_NAME/.json?after=AFTER";
    String subreddit, url, after;

    PostsHolder(String sr) {
        subreddit = sr;
        after = "";
        generateURL();
    }

    //Creates the link to the proper subreddit by replacing keywords
    private void generateURL() {
        url = URL_TEMPLATE.replace("SUBREDDIT_NAME", subreddit);
        url = url.replace("AFTER", after);
    }

    //Returns the posts found in the sub reddit
    List<Post> fetchPosts() {
        String raw = RemoteData.readContents(url);
        List<Post> list = new ArrayList<Post>();
        try {
            JSONObject data = new JSONObject(raw).getJSONObject("data");
            JSONArray children = data.getJSONArray("children");
            //This line here let's the user continue reading even if the navigate away from the page
            after = data.getString("after");
            for(int i = 0; i < children.length(); i++) {
                JSONObject cur = children.getJSONObject(i).getJSONObject("data");
                Post p = new Post();
                p.title = cur.optString("title");
                p.url = cur.optString("url");
                p.numComments = cur.optInt("num_comments");
                p.points = cur.getInt("score");
                p.author = cur.getString("author");
                p.subreddit = cur.getString("subreddit");
                p.permalink = cur.getString("permalink");
                p.domain = cur.getString("domain");
                p.id = cur.getString("id");
                if(p.title != null) {
                    list.add(p);
                }
            }
        } catch (Exception e) {
            Log.e("Fetch posts", e.toString());
        }
        return list;
    }

    //Continues getting posts afterwards
    List<Post> fetchMorePosts() {
        generateURL();
        return fetchPosts();
    }
}