package com.andrewyiu.reddittime;

/**
 * Created by andrew on 12/07/13.
 */
public class Comment {

    String htmlText, author, points, postedOn;
    int level;

    String getDetails() {
        String details = author + " posted " + postedOn + " ago.";
        return details;
    }

    String getHtmlText() {
        return htmlText;
    }

    String getPoints() {
        return points;
    }

}
