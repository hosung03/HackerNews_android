package com.hosung.hackernews.Api;

import android.content.ContentValues;
import android.util.Log;
import android.util.Pair;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.hosung.hackernews.model.HackerNews;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Hosung, Lee on 2017. 7. 10..
 */

public class HackerNewsApi {
    private static final String TAG = "HackerNewsApi";

    public enum ListType {
        top_story,
        new_story,
        best_story,
        show,
        ask,
        jobs,
        bookmark
    }

    public static Firebase getNewsFirebase(HackerNewsApi.ListType listType){
        switch (listType) {
            case show:
                return new Firebase("https://hacker-news.firebaseio.com/v0/showstories");
            case ask:
                return new Firebase("https://hacker-news.firebaseio.com/v0/askstories");
            case jobs:
                return new Firebase("https://hacker-news.firebaseio.com/v0/jobstories");
            default:
                return new Firebase("https://hacker-news.firebaseio.com/v0/topstories");
        }
    }

    public static HackerNews mapNews(Map<String, Object> map, Long internalId,
                                     Integer rank, ListType listType) {
        HackerNews news = null;
        try {
            String by = (String) map.get("by");
            Long id = (Long) map.get("id");
            String type = (String) map.get("type");
            Long time = (Long) map.get("time");
            Long score = (Long) map.get("score");
            String title = (String) map.get("title");
            String url = (String) map.get("url");
            Long comments = Long.valueOf(0);
            if (map.get("descendants") != null) {
                comments = (Long) map.get("descendants");
            }

            news = new HackerNews(internalId, by, id, type, time, score.intValue(),
                    title, url, comments.intValue(), System.currentTimeMillis(), rank,
                    0, 0, 0, listType.name());

        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }

        return news;
    }
}
