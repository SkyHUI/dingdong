package com.sky.utils;

/**
 * 项目名称：com.sky.utils
 * 类描述：请求Command
 * 创建人：Sky
 * 创建时间：2017/3/17 15:16
 */
public class CommandOrder {

    /**
     * 注册请求
     */
    public static final int COMMAND_REGISTER = 0;

    /**
     * 登录请求
     */
    public static final int COMMAND_LOGIN = 1;

    /**
     * 医药父菜单请求
     */
    public static final int COMMAND_MEDICINE_PARENT = 2;

    /**
     * 医药子菜单请求
     */
    public static final int COMMAND_MEDICINE_CHILD = 3;

    /**
     * 疾病库请求
     */
    public static final int COMMAND_MEDICINE_DISEASE = 4;

    /**
     * 药品指导请求
     */
    public static final int COMMAND_MEDICINE_DRUG = 5;

    /**
     * 检查结果请求
     */
    public static final int COMMAND_MEDICINE_INSPECTION = 6;

    /**
     * 请求新闻
     */
    public static final int COMMAND_NEWS = 7;

    /**
     * 请求收藏
     */
    public static final int COMMAND_NEWS_LOVE = 8;

    /**
     * 问题反馈
     */
    public static final int COMMAND_FEED_BACK = 9;
}
