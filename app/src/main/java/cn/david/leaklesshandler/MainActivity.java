package cn.david.leaklesshandler;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import cn.david.leaklesshandler.base.BaseActivity;
import cn.david.leaklesshandler.base.BaseHandler;
import cn.david.leaklesshandler.base.InnerTask;

/**
 *  非静态内部类会隐式的持有外部类的引用，
 *  这时如果内部类的生命周期大于外部类（Activity）的生命周期
 *  就可能会发生内存泄露
 *
 * @author lyw
 * @date 2015-11-25
 */
public class MainActivity extends BaseActivity {

    private static final int MSG_OK = 888;
    MyHandler mHandler = new MyHandler(this);
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.main_text);

        // 开启线程
        new Thread(new DemoTask(this)).start();
    }

    /**
     * Handler 应使用静态内部类
     *
     * 将作为外部类的 Activity 作为 参数传进去，
     * 用弱引用持有其实例
     */
    private static class MyHandler extends BaseHandler<MainActivity> {

        public MyHandler(MainActivity activity) {
            super(activity);
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case MSG_OK:
                    getActivity().textView.setText("...data...");
                    break;

                default:
                    break;
            }
        }
    }
    /**
     * 继承自Runnable 的耗时操作也 应使用静态内部类
     *
     * 将作为外部类的 Activity 作为 参数传进去，
     * 用弱引用持有其实例
     */
    private static final class DemoTask extends InnerTask<MainActivity> {

        public DemoTask( MainActivity activity ) {
            super(activity);
        }

        @Override
        public void run() {
            /****************************
             *
             *  ... 耗时操作
             *
             ****************************/

            if ( isActivityAlive()) { // 判断该页面是否还在， 在的话发送消息
                getActivity().mHandler.obtainMessage(MSG_OK).sendToTarget();
            }
        }
    }

    @Override
    protected void onDestroy() {
        // 在Activity摧毁时清空 handler 里面的消息
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

}
