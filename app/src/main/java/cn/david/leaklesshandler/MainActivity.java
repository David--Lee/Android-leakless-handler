package cn.david.leaklesshandler;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * @author lyw
 * @date 2015-11-25
 */
public class MainActivity extends AppCompatActivity {

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
    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> activityReference;

        public MyHandler(MainActivity activity) {
            activityReference = new WeakReference<MainActivity>(
                    activity);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_OK:
                    final MainActivity activity = activityReference
                            .get();
                    if (activity != null && !activity.isFinishing()) {// 判断该页面是否还在， 在的话更新UI
                        activity.textView.setText("......");
                    }
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
    private static final class DemoTask implements Runnable {

        private final WeakReference<MainActivity> activityReference;

        public DemoTask( MainActivity activity ) {
            activityReference = new WeakReference<MainActivity>(
                    activity);
        }

        @Override
        public void run() {
            /****************************
             *
             *  ... 耗时操作
             *
             ****************************/


            final MainActivity activity = activityReference.get();
            if ( activity != null && !activity.isFinishing()) { // 判断该页面是否还在， 在的话发送消息
                activity.mHandler.obtainMessage(MSG_OK).sendToTarget();
            }
        }
    }
}
