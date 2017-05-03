package com.beiing.leafchart.constant;

/**
 * 项目名称：com.sky.utils
 * 类描述：
 * 创建人：Sky
 * 创建时间：2016/12/27 21:31
 */
public class Constant {

    /**
     * 单例模式
     * 防止多线程引用冲突
     */
    private static Constant constant = null;

    /**
     * 私有化构造函数
     */
    private Constant(){
        super();
    }

    public synchronized static Constant getInstance(){
        if(constant == null){
            constant = new Constant();
        }
        return constant;
    }

    /**
     * 模型图宽度的倍率
     * 默认值1倍即屏幕宽度
     */
    public static int CHART_POWER = 1;

}
