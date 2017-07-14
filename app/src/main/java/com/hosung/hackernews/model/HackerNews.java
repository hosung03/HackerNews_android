package com.hosung.hackernews.model;

/**
 * Created by Hosung, Lee on 2017. 7. 8..
 */

public class HackerNews {
    public static final String ITEM_ID = "item_id";
    public static final String TYPE = "type";
    public static final String BY = "by";
    public static final String COMMENTS = "comments";
    public static final String URL = "url";
    public static final String SCORE = "score";
    public static final String TITLE = "title";
    public static final String TIME_AGO = "time_ago";
    public static final String RANK = "rank";
    public static final String TIMESTAMP = "timestamp";
    public static final String BOOKMARK = "bookmark";
    public static final String READ = "read";
    public static final String VOTED = "voted";
    public static final String FILTER = "filter";

    private Long internalId;
    private String by;
    private Long id;
    private Long timeAgo;
    private int score;
    private String title;
    private String url;
    private int comments;
    private String type;
    private Long timestamp;
    private int rank;
    private int read;
    private int bookmark;
    private int voted;
    private String filter;

    public HackerNews(Long internalId, String by, Long id, String type, Long timeAgo, int score, String title, String url, int comments, Long timestamp, int rank, int bookmark, int read, int voted, String filter) {
        this.internalId = internalId;
        this.by = by;
        this.id = id;
        this.timeAgo = timeAgo;
        this.type = type;
        this.score = score;
        this.title = title;
        this.url = url;
        this.comments = comments;
        this.timestamp = timestamp;
        this.rank = rank;
        this.bookmark = bookmark;
        this.read = read;
        this.voted = voted;
        this.filter = filter;
    }


    public Long getInternalId() {
        return internalId;
    }

    public void setInternalId(Long internalId) {
        this.internalId = internalId;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTimeAgo() {
        return timeAgo;
    }

    public void setTimeAgo(Long timeAgo) {
        this.timeAgo = timeAgo;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public int getBookmark() {
        return bookmark;
    }

    public void setBookmark(int bookmark) {
        this.bookmark = bookmark;
    }

    public int getVoted() {
        return voted;
    }

    public void setVoted(int voted) {
        this.voted = voted;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
