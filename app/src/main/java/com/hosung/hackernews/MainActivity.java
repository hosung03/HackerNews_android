package com.hosung.hackernews;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.hosung.hackernews.Api.HackerNewsApi;
import com.hosung.hackernews.model.HackerNews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_VIEWNEWS = 0;

    public static ArrayList<HackerNews> mNewsList = new ArrayList<>();
    public static HashMap<Long, HackerNews> mBookMarkList = new HashMap<Long, HackerNews>();
    HackerNewsApi.ListType curlistType = HackerNewsApi.ListType.top_story;
    private RecyclerView mListView;
    HackerNewsAdapter mAdapter;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mListView=(RecyclerView)findViewById(R.id.listNews);
        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(mProgressDialog.isShowing()){
                    mProgressDialog.dismiss();
                }
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this);
        mListView.setLayoutManager(llm);
        mListView.setHasFixedSize(true);

        setTitle("HackerNews' Top Stroies");
        initializeData(HackerNewsApi.ListType.top_story);
        initializeAdapter();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        int size = this.mNewsList.size();
        if (id == R.id.nav_topstories) {
            setTitle("HackerNews' Top Stroies");
            if(size>0){
                mNewsList.clear();
                mAdapter.mNewsList.clear();
                mAdapter.notifyDataSetChanged();
            }
            curlistType = HackerNewsApi.ListType.top_story;
            initializeData(HackerNewsApi.ListType.top_story);
        } else if (id == R.id.nav_newest) {
            setTitle("HackerNews' Newst");
            if(size>0){
                mNewsList.clear();
                mAdapter.mNewsList.clear();
                mAdapter.notifyDataSetChanged();
            }
            curlistType = HackerNewsApi.ListType.new_story;
            initializeData(HackerNewsApi.ListType.new_story);
        } else if (id == R.id.nav_best) {
            setTitle("HackerNews' Best");
            if(size>0){
                mNewsList.clear();
                mAdapter.mNewsList.clear();
                mAdapter.notifyDataSetChanged();
            }
            curlistType = HackerNewsApi.ListType.best_story;
            initializeData(HackerNewsApi.ListType.best_story);
        } else if (id == R.id.nav_showHN) {
            setTitle("HackerNews' ShowHN");
            if(size>0){
                mNewsList.clear();
                mAdapter.mNewsList.clear();
                mAdapter.notifyDataSetChanged();
            }
            curlistType = HackerNewsApi.ListType.show;
            initializeData(HackerNewsApi.ListType.show);
        } else if (id == R.id.nav_askHN) {
            setTitle("HackerNews' AskHN");
            if(size>0){
                mNewsList.clear();
                mAdapter.mNewsList.clear();
                mAdapter.notifyDataSetChanged();
            }
            curlistType = HackerNewsApi.ListType.ask;
            initializeData(HackerNewsApi.ListType.ask);
        } else if (id == R.id.nav_jobs) {
            setTitle("HackerNews' Jobs");
            if(size>0){
                mNewsList.clear();
                mAdapter.mNewsList.clear();
                mAdapter.notifyDataSetChanged();
            }
            curlistType = HackerNewsApi.ListType.jobs;
            initializeData(HackerNewsApi.ListType.jobs);
        } else if (id == R.id.nav_bookmark) {
            setTitle("HackerNews' Bookmark");
            if(size>0){
                mNewsList.clear();
                mAdapter.mNewsList.clear();
                mAdapter.notifyDataSetChanged();
            }
            curlistType = HackerNewsApi.ListType.bookmark;
            initializeData(HackerNewsApi.ListType.bookmark);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializeData(final HackerNewsApi.ListType listType){
        mProgressDialog = new ProgressDialog(MainActivity.this, R.style.AppTheme_Dark_Dialog);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        if (listType == HackerNewsApi.ListType.bookmark) {
            for ( Long internalId : mBookMarkList.keySet() ) {
                mNewsList.add(mBookMarkList.get(internalId));
            }
            if(mProgressDialog.isShowing()){
                mProgressDialog.dismiss();
            }
        } else {
            Firebase news = HackerNewsApi.getNewsFirebase(listType);
            news.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null && listType == curlistType) {
                        for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                            if(listType != curlistType) break;
                            Integer rank = Integer.valueOf(dataSnapshot.child(String.valueOf(i)).getKey());
                            Long internalId = (Long) dataSnapshot.child(String.valueOf(i)).getValue();
                            getNewsItemsFirebase(rank, internalId, listType);
                        }
                    } else {
                        Log.d(TAG, "News List is empty for " + listType.name() + ",(" + curlistType.name() +")");
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    Log.d(TAG, firebaseError.getMessage());
                }
            });
        }
    }

    public void getNewsItemsFirebase(final Integer rank, final Long internalId, final HackerNewsApi.ListType listType) {
        final Firebase story = new Firebase("https://hacker-news.firebaseio.com/v0/item/" + internalId);
        story.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> newItem = (Map<String, Object>) dataSnapshot.getValue();
                if (newItem != null && listType == curlistType) {
                    HackerNews curNews = HackerNewsApi.mapNews(newItem, internalId, rank, listType);
                    if(mBookMarkList.containsKey(curNews.getInternalId())) curNews.setBookmark(1);
                    mNewsList.add(curNews);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d(TAG, firebaseError.getMessage());
            }
        });
    }

    private void initializeAdapter(){
        if(mAdapter!=null) mAdapter = null;
        mAdapter = new HackerNewsAdapter(mNewsList);
        mListView.setAdapter(mAdapter);
    }

    class HackerNewsAdapter extends RecyclerView.Adapter<HackerNewsAdapter.HackerNewsViewHolder> {
        ArrayList<HackerNews> mNewsList;
        HackerNewsAdapter(ArrayList<HackerNews> newslist){
            this.mNewsList = newslist;
        }

        public class HackerNewsViewHolder extends RecyclerView.ViewHolder {
            View mRootView;

            RelativeLayout article_main;
            TextView article_title;
            TextView article_time;
            TextView article_user;
            TextView article_points;
            TextView article_comments;
            ImageButton article_bookmark;

            public HackerNewsViewHolder(View view, int position) {
                super(view);
                mRootView = view;

                article_main = (RelativeLayout) view.findViewById(R.id.article_main);
                article_title = (TextView) view.findViewById(R.id.article_title);
                article_time = (TextView) view.findViewById(R.id.article_time);
                article_user = (TextView) view.findViewById(R.id.article_user);
                article_points = (TextView) view.findViewById(R.id.article_points);
                article_comments = (TextView) view.findViewById(R.id.article_comments);

                article_bookmark = (ImageButton) view.findViewById(R.id.article_bookmark);

                article_main.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = (int) view.getTag();
                        HackerNews clickNews = mNewsList.get(position);
                        Intent intent = new Intent(MainActivity.this, NewsViewActivity.class);
                        intent.putExtra("acticle_position",position);
                        intent.putExtra("acticle_title",clickNews.getTitle());
                        intent.putExtra("acticle_url",clickNews.getUrl());
                        if(clickNews.getBookmark()==1) intent.putExtra("acticle_bookmark", true);
                        else intent.putExtra("acticle_bookmark", false);
                        startActivityForResult(intent, REQUEST_VIEWNEWS );
                    }
                });
            }
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public HackerNewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_list_item, viewGroup, false);
            HackerNewsViewHolder nvh = new HackerNewsViewHolder(v, position);
            return nvh;
        }

        @Override
        public void onBindViewHolder(HackerNewsViewHolder hvholder, final int position) {
            hvholder.article_main.setTag(position);

            hvholder.article_title.setText(mNewsList.get(position).getTitle());
            hvholder.article_time.setText(time(mNewsList.get(position).getTimestamp()));
            hvholder.article_user.setText("by "+mNewsList.get(position).getBy());
            hvholder.article_points.setText(String.valueOf(mNewsList.get(position).getScore() + " points"));
            hvholder.article_comments.setText(String.valueOf(mNewsList.get(position).getComments() + " comments"));
            hvholder.article_bookmark.setImageResource(R.drawable.bookmark);

            final HackerNews curNews = mNewsList.get(position);
            if(mNewsList.get(position).getBookmark()==1){
                hvholder.article_bookmark.setVisibility(hvholder.mRootView.GONE);
            } else {
                hvholder.article_bookmark.setVisibility(hvholder.mRootView.VISIBLE);
            }
            hvholder.article_bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addBookmark(MainActivity.this,position);
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            if(mNewsList!=null) return mNewsList.size();
            else return 0;
        }

        public String time(Long distanceMillis) {
            double seconds = distanceMillis / 1000;
            double minutes = seconds / 60;
            double hours = minutes / 60;
            double days = hours / 24;
            double years = days / 365;

            String time;
            if (seconds < 45) {
                time = "less than a minute";
            } else if (seconds < 90 || minutes < 45) {
                if(minutes < 2)
                    time = String.format("about %d minute", Math.round(minutes));
                else
                    time = String.format("%d minute", Math.round(minutes));
            } else if (minutes < 90 || hours < 24) {
                if(minutes < 2)
                    time = String.format("about %d hour", Math.round(hours));
                else
                    time = String.format("%d hour", Math.round(hours));
            } else if (hours < 48 || days < 30) {
                if(minutes < 2)
                    time = String.format("%d day", Math.round(days));
                else
                    time = String.format("%d days", Math.round(days));
            } else if (days < 60 || days < 365) {
                if((days / 30) < 2)
                    time = String.format("about %d month",Math.round(days / 30));
                else
                    time = String.format("%d month",Math.round(days / 30));
            } else {
                if((days / 30) < 2)
                    time = String.format("about %d year",Math.round(years));
                else
                    time = String.format("%d years",Math.round(years));
            }

            return time + " " + "ago";
        }
    }

    public static void addBookmark(Context context, int position){
        HackerNews curNews = mNewsList.get(position);
        curNews.setBookmark(1);
        mBookMarkList.put(curNews.getInternalId(),curNews);
        Toast.makeText(context, "It is bookmarked!", Toast.LENGTH_SHORT).show();
    }
}
