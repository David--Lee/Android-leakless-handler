package cn.david.leaklesshandler.base;

import android.os.Message;

import java.lang.ref.WeakReference;

/**
 *  Activity 内部的任务实现类，该类内部含有所在Activity 的弱引用
 *  继承该类的任务建议写成静态类
 *
 * Created by David on 2015/11/30.
 */
public abstract class InnerTask<T extends BaseActivity> implements Runnable {
    private final WeakReference<T> activityReference;

    public InnerTask(T activity) {
        activityReference = new WeakReference<T>(
                activity);
    }

    public boolean isActivityAlive() {
        return AppUtils.isActivityAlive(getActivity());
    }

    public T getActivity() {
        return activityReference.get();
    }

}
