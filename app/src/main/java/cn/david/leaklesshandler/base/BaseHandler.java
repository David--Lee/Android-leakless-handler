package cn.david.leaklesshandler.base;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import cn.david.leaklesshandler.MainActivity;

/**
 * 持有 Activity 弱引用的 handler,
 * 建议写成静态类
 *
 * Created by David on 2015/11/30.
 */
public class BaseHandler<T extends BaseActivity> extends Handler {
    private final WeakReference<T> activityReference;

    public BaseHandler(T activity) {
        activityReference = new WeakReference<T>(
                activity);
    }

    @Override
    public void handleMessage(Message msg) {
        if (!isActivityAlive()) return;
    }

    public boolean isActivityAlive() {
        return AppUtils.isActivityAlive(getActivity());
    }

    public T getActivity() {
        return activityReference.get();
    }
}
