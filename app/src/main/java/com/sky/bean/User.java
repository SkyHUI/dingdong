package com.sky.bean;

/**
 * 项目名称：com.sky.bean
 * 类描述：用户信息实体类
 * 创建人：Sky
 * 创建时间：2017/1/2 16:27
 */
public class User {

    private String username;

    private String password;

    private int age;

    private int sex;

    private int sbp;

    private int dbp;

    public User() {
    }

    public User(String username, String password, int age, int sex, int sbp, int dbp) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.sex = sex;
        this.sbp = sbp;
        this.dbp = dbp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getSbp() {
        return sbp;
    }

    public void setSbp(int sbp) {
        this.sbp = sbp;
    }

    public int getDbp() {
        return dbp;
    }

    public void setDbp(int dbp) {
        this.dbp = dbp;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", sbp=" + sbp +
                ", dbp=" + dbp +
                '}';
    }
}
