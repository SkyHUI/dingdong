package com.sky.bean;

import java.io.Serializable;

/**
 * 项目名称：com.sky.bean
 * 类描述：新闻实体类
 * 创建人：Sky
 * 创建时间：2017/3/22 14:01
 */
public class News implements Serializable{

    private int id;

    private String title;

    private String content;

    private String picture_url;

    private String date;

    public News() {
    }

    public News(String title, String content, String picture_url, String date) {
        this.title = title;
        this.content = content;
        this.picture_url = picture_url;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content.replace("/n","\n").replace("/t","\t").replace("\\n","\n").replace("\\t","\t");
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
