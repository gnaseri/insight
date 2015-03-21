package com.ubiqlog.ubiqlogwear.Util;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by User on 3/7/15.
 */
public class PkgLookupUtil {
    private static final String TAG = PkgLookupUtil.class.getSimpleName();

    public static class HtmlGrab extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            String pkgName = fetchContent(params[0]);
            return pkgName;
        }
    }

    private static String fetchContent (String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            return "NA";
        }

        Elements genre = document.select ("span[itemprop=genre]");
        String genreString = genre.text();
        Log.d(TAG, "String:" + genreString);
        return genreString;

    }
}
