package com.example.mprojects.classplus;

public class Item {
    private String name;
    private String degree;
    private String noofstuds;
    private String post;
    private String code;

    public Item(String name,String degree,String noofstuds,String code,String date)
    {
        this.setName(name);
        this.setDegree(degree);
        this.setNoofstuds(noofstuds);
        this.setCode(code);
        this.setPost(date);
    }
    //Getter and Setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getNoofstuds() {
        return noofstuds;
    }

    public void setNoofstuds(String noofstuds) {
        this.noofstuds = noofstuds;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
