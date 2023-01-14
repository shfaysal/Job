package com.example.job.model;

public class Apply {
    String pid;
    String creator_id;
    String userID;
    String url;
    String name;

    public Apply() {

    }

    public Apply(String pid, String creator_id, String userID, String url,String name) {
        this.pid = pid;
        this.creator_id = creator_id;
        this.userID = userID;
        this.url = url;
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
