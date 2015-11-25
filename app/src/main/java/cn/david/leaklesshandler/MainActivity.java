package cn.david.leaklesshandler;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

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

    //
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
                    if (activity != null && !activity.isFinishing()) {
                        activity.textView.setText("......");
                    }
                    break;

                default:
                    break;
            }
        }
    }

    private static final class DemoTask implements Runnable {

        private final WeakReference<MainActivity> activityReference;

        public DemoTask( MainActivity activity ) {
            activityReference = new WeakReference<MainActivity>(
                    activity);
        }

        @Override
        public void run() {
            // ... 耗时操作
            final MainActivity activity = activityReference.get();
            if ( activity != null && !activity.isFinishing()) {
                activity.mHandler.obtainMessage(MSG_OK).sendToTarget();
            }
        }
    };
}
