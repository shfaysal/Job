package com.example.job.model;

public class PostModel {
    String category;
    String company_name;
    String post;
    String post_details;
    String user_id;
    String pid;

    public PostModel() {

    }

    public PostModel(String category, String company_name, String post, String post_details, String user_id, String pid) {
        this.category = category;
        this.company_name = company_name;
        this.post = post;
        this.post_details = post_details;
        this.user_id = user_id;
        this.pid = pid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCompany_Name() {
        return company_name;
    }

    public void setCompany_Name(String company_Name) {
        this.company_name = company_Name;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPost_details() {
        return post_details;
    }

    public void setPost_details(String post_details) {
        this.post_details = post_details;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
}
