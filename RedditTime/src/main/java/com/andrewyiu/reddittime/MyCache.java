package com.andrewyiu.reddittime;

import android.os.Environment;
import android.util.Log;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;

public class MyCache {

    //Setting up the cache directory
    static private String cacheDirectory = "/Android/data/com.andrewyiu.reddittime/cache/";

    //If there is an SD card that I can write to, write to it instead of phone memory
    static {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheDirectory = Environment.getDownloadCacheDirectory() + cacheDirectory;
            File file = new File(cacheDirectory);
            file.mkdirs();
        }
    }

    static public String convertToCacheName(String url) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(url.getBytes());
            byte[] b = digest.digest();
            BigInteger bi = new BigInteger(b);
            return "mycache_" + bi.toString(16) + ".cac";
        } catch (Exception e) {
            Log.e("MD5", e.toString());
            return null;
        }
    }

    private static boolean tooOld(long time) {
        long now = new Date().getTime();
        long diff = now - time;
        if(diff > 1000*60*5) {
            return true;
        }
        return false;
    }

    public static byte[] read(String url) {
        try {
            String file = cacheDirectory + "/" + convertToCacheName(url);
            File f = new File(file);
            if(!f.exists() || f.length() < 1) {
                return null;
            }
            if(f.exists() && tooOld(f.lastModified())) {
                f.delete();
            }
            byte data[] = new byte[(int) f.length()];
            DataInputStream dis = new DataInputStream(new FileInputStream(f));
            dis.readFully(data);
            dis.close();
            return data;
        } catch (Exception e) {
            return null;
        }
    }

    public static void write(String url, String data) {
        try {
            String file = cacheDirectory + "/" + convertToCacheName(url);
            PrintWriter pw = new PrintWriter(new FileWriter(file));
            pw.print(data);
            pw.close();
        } catch(Exception e) {
            Log.e("Error", "Could not write to cache");
        }
    }

}
