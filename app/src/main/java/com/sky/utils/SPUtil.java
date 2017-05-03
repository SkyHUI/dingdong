package com.sky.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.sky.bean.User;

/**
 * 项目名称：com.sky.utils
 * 类描述：处理  SharedPreferences  类数据
 * 创建人：Sky
 * 创建时间：2016/12/11 13:09
 */
public class SPUtil {

    private final static String DING_DONG = "dingdong";

    private final static String FIRST_IN = "firstin";

    private final static String REGISTERD = "registered";

    /**
     * 是否匹配模型
     */
    private final static String MATCH = "match";

    /**
     * 已显示引导页 设置 FIRST_IN 为false
     * @param context
     */
    public static void setFirstIn(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(DING_DONG,Context.MODE_PRIVATE).edit();
        editor.putBoolean(FIRST_IN,false);
        editor.commit();
    }

    /**
     * 获取 FIRST_IN的结果
     * @param context
     * @return
     */
    public static boolean isFirstIn(Context context){
        SharedPreferences share = context.getSharedPreferences(DING_DONG,Context.MODE_PRIVATE);
        return share.getBoolean(FIRST_IN,true);
    }



    /**
     * 保存用户注册信息
     * @param context
     * @param user
     */
    public static void setRegisterd(Context context,User user){
        SharedPreferences.Editor editor = context.getSharedPreferences(DING_DONG,Context.MODE_PRIVATE).edit();
        editor.putBoolean(REGISTERD,true);
        editor.putString("username",user.getUsername());
        editor.putString("password",user.getPassword());
        editor.putInt("age",user.getAge());
        editor.putInt("sex",user.getSex());
        editor.putInt("sbp",user.getSbp());
        editor.putInt("dbp",user.getDbp());
        editor.commit();
    }

    /**
     * 获取用户信息
     * @param context
     * @return
     */
    public static User getUser(Context context){
        SharedPreferences share = context.getSharedPreferences(DING_DONG,Context.MODE_PRIVATE);
        String username = share.getString("username","晴天");
        String passwrod = share.getString("password","password");
        int age = share.getInt("age",0);
        int sex = share.getInt("sex",0);
        int sbp = share.getInt("sbp",0);
        int dbp = share.getInt("dbp",0);
        return new User(username,passwrod,age,sex,sbp,dbp);
    }

    /**
     * 是否注册
     * @param context
     * @return
     */
    public static boolean isRegisterd(Context context){
        SharedPreferences share = context.getSharedPreferences(DING_DONG,Context.MODE_PRIVATE);
        return share.getBoolean(REGISTERD,false);
    }

    /**
     *设置模型匹配成功
     * @param context
     */
    public static void setMatchSuccessful(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(DING_DONG,Context.MODE_PRIVATE).edit();
        editor.putBoolean(MATCH,true);
    }

    /**
     * 是否匹配过模型
     * @param context
     * @return
     */
    public static boolean isMatchSuccessful(Context context){
        SharedPreferences share = context.getSharedPreferences(DING_DONG,Context.MODE_PRIVATE);
        return share.getBoolean(MATCH,false);
    }

}
