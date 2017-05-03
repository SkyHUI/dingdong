package com.sky.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：com.sky.utils
 * 类描述：
 * 创建人：Sky
 * 创建时间：2016/12/12 17:32
 */
public class ActivityCollector {

    private static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
