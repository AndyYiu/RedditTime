package com.andrewyiu.reddittime;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by andrew on 09/07/13.
 */
public class RemoteData {

    //Opens up the url connection and applies timeout/invalid url error management
    public static HttpURLConnection getConnection (String url) {
        System.out.println("URL: " + url);
        HttpURLConnection hcon = null;
        try {
            hcon = (HttpURLConnection) new URL(url).openConnection();
            hcon.setReadTimeout(30000);
            hcon.setRequestProperty("User-Agent", "RedditTime v1.0");
        } catch (MalformedURLException e) {
            Log.e("getConnection()", "Invalid URL: " + e.toString());
        } catch (IOException e) {
            Log.e("getConnection()", "Could not connect: " + e.toString());
        }
        return hcon;
    }

    //Creates a string with all the data extracted from the .json
    public static String readContents (String url) {

        byte[] t = MyCache.read(url);
        String cached = null;
        if(t != null) {
            cached = new String(t);
            t = null;
        }
        if(cached != null) {
            Log.d("MSG", "Using cache for " + url);
            return cached;
        }

        HttpURLConnection hcon = getConnection(url);
        if(hcon == null) {
            return null;
        }
        try {
            StringBuffer sb = new StringBuffer(8192);
            String tmp = "";
            BufferedReader br = new BufferedReader( new InputStreamReader(hcon.getInputStream()));
            while((tmp = br.readLine()) != null) {
                sb.append(tmp).append("\n");
            }
            br.close();
            //Write the new data to cache
            MyCache.write(url, sb.toString());
            return sb.toString();
        } catch (IOException e) {
            Log.d("READ FAILED", e.toString());
            return null;
        }
    }

}
