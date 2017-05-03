package com.sky.bean;

/**
 * 项目名称：com.sky.bean
 * 类描述：菜单实体类
 * 创建人：Sky
 * 创建时间：2017/1/3 9:35
 */
public class Menu {

    private String name;

    private int resId;

    public Menu() {
    }

    public Menu(String name, int resId) {
        this.name = name;
        this.resId = resId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
