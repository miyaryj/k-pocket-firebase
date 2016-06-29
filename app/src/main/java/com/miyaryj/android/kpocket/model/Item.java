package com.miyaryj.android.kpocket.model;

import java.util.HashMap;
import java.util.Map;

public class Item {
    private String mTitle;

    private String mUri;

    private long mCreatedAt;

    public Item() {
    }

    public Item(String title, String uri) {
        mTitle = title;
        mUri = uri;
        mCreatedAt = System.currentTimeMillis();
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setUri(String uri) {
        mUri = uri;
    }

    public void setCreatedAt(long createdAt) {
        mCreatedAt = createdAt;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUri() {
        return mUri;
    }

    public long getCreatedAt() {
        return mCreatedAt;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("title", mTitle);
        ret.put("uri", mUri);
        ret.put("created_at", mCreatedAt);
        return ret;
    }
}
