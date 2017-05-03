package com.sky.utils;

/**
 * 项目名称：com.sky.utils
 * 类描述：
 * 创建人：Sky
 * 创建时间：2017/3/16 15:18
 */
public class RequestConstant {

    /**
     * HTTP请求根路径
     */
    public static final String REQUEST_BASE_URL = "http://192.168.191.1:8080/dingdong/";

    /**
     * 注册和登录请求地址
     */
    public static final String REQUEST_LOGIN = REQUEST_BASE_URL + "login.do";

    /**
     * 新闻请求地址
     */
    public static final String REQUEST_NEWS = REQUEST_BASE_URL + "news.do";

    /**
     * 医药有关请求地址
     */
    public static final String REQUEST_MEDICINE = REQUEST_BASE_URL  + "medicine.do";

    /**
     * 匹配用户最佳模型
     */
    public static final String REQUEST_MATCH = REQUEST_BASE_URL  + "match.do";

    /**
     * 数据模型下载
     */
    public static final String REQUEST_MODEL = REQUEST_BASE_URL + "model.zip";

    public static int USER_ID = 0;
}
