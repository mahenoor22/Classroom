package com.example.mprojects.classplus;

public class gradesitem {
    private String name;
    private String post,url;
    private String aid;
    public gradesitem(String name,String date,String url,String aid)
    {
        this.setName(name);
         this.setPost(date);
         this.setUrl(url);
         this.setAid(aid);

    }
    //Getter and Setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }
}
