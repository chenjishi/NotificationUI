package com.android.server;
import android.app.INotificationManager;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.os.*;
import android.os.ITestService;
import android.os.Process;
import android.service.notification.INotificationListener;
import android.service.notification.IStatusBarNotificationHolder;
import android.service.notification.NotificationRankingUpdate;
import android.service.notification.StatusBarNotification;
import android.util.Log;
/**
 * Created by chenjishi on 16/4/7.
 */
public class TestService extends ITestService.Stub {
    private static final String TAG = "TestService";
    private TestWorkerThread mWorker;
    private TestWorkerHandler mHandler;
    private Context mContext;
    private ComponentName mComponentName;
    private int userId;

    private INotificationManager mNoMan;

    public TestService(Context context) {
        super();
        mContext = context;
        mWorker = new TestWorkerThread("TestServiceWorker");
        mWorker.start();
        Log.i(TAG, "Spawned worker thread");
    }

    public void setValue(int val) {
        Log.i(TAG, "setValue " + val);
        Message msg = Message.obtain();
        msg.what = TestWorkerHandler.MESSAGE_SET;
        msg.arg1 = val;
        mHandler.sendMessage(msg);
    }

    public void registerNotification(ComponentName name, int user) {
        mComponentName = name;
        userId = user;
        Message msg = Message.obtain();
        msg.what = TestWorkerHandler.MESSAGE_REGISTER;
        msg.arg1 = 0;
        mHandler.sendMessage(msg);
    }

    private class INotificationListenerWrapper extends INotificationListener.Stub {
        @Override
        public void onNotificationPosted(IStatusBarNotificationHolder sbnHolder,
                                         NotificationRankingUpdate update) {
            Log.i("test", "##onNotificationPosted");
        }
        @Override
        public void onNotificationRemoved(IStatusBarNotificationHolder sbnHolder,
                                          NotificationRankingUpdate update) {
        }
        @Override
        public void onListenerConnected(NotificationRankingUpdate update) {
        }
        @Override
        public void onNotificationRankingUpdate(NotificationRankingUpdate update)
                throws RemoteException {
        }
        @Override
        public void onListenerHintsChanged(int hints) throws RemoteException {
        }

        @Override
        public void onInterruptionFilterChanged(int interruptionFilter) throws RemoteException {
        }
    }

    private final INotificationManager getNotificationInterface() {
        if (mNoMan == null) {
            mNoMan = INotificationManager.Stub.asInterface(
                    ServiceManager.getService(Context.NOTIFICATION_SERVICE));
        }
        return mNoMan;
    }

    private class TestWorkerThread extends Thread {
        public TestWorkerThread(String name) {
            super(name);
        }
        public void run() {
            Looper.prepare();
            mHandler = new TestWorkerHandler();
            Looper.loop();
        }
    }

    private class TestWorkerHandler extends Handler {
        private static final int MESSAGE_SET = 0;
        private static final int MESSAGE_REGISTER = 1;
        @Override
        public void handleMessage(Message msg) {
            try {
                if (msg.what == MESSAGE_SET) {
                    Log.i(TAG, "set message received: " + msg.arg1);
                } else {
                    INotificationManager noMan = getNotificationInterface();
                    noMan.registerListener(new INotificationListenerWrapper(), mComponentName, userId);
                }
            } catch (Exception e) {
                // Log, don't crash!
                Log.e(TAG, "Exception in TestWorkerHandler.handleMessage:", e);
            }
        }
    }
}
