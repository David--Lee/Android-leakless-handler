package cn.david.leaklesshandler.base;

import android.app.Activity;

/**
 * Created by David on 2015/11/30.
 */
public class AppUtils {

    /**
     *  判断该页面是否还在
     * @param activity
     * @return
     */
    public static boolean isActivityAlive(Activity activity) {
        return activity != null && !activity.isFinishing();
    }
}
