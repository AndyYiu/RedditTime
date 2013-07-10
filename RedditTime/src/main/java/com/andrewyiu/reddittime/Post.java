package com.andrewyiu.reddittime;

/**
 * Created by andrew on 09/07/13.
 */
public class Post {

    String subreddit, title, author, permalink, url, domain, id;
    int points, numComments;

    //Returns info about the post
    String getDetails() {
        String details = "Posted by " + author + " | " + numComments + " replies";
        return details;
    }

    //Returns the title of the post
    String getTitle() {
        return title;
    }

    //Shows the overall score of the post
    String getScore(){
        return Integer.toString(points);
    }

}
