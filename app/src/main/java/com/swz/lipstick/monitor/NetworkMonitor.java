package com.swz.lipstick.monitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.swz.lipstick.LSApp;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author nemoqjzhang
 * @date 2018/12/28 21:05.
 */
public class NetworkMonitor extends BroadcastReceiver {

    private static final String TAG = "NetworkMonitor_";

    private static volatile NetworkMonitor sInstance = null;

    private static final int MSG_NET_CHANGE = 0x01;

    private NetMsgHandler mNetworkChangedHandler;


    private List<WeakReference<INetInterface>> mListeners = new CopyOnWriteArrayList<>();

    private NetworkMonitor() {
        registerReceiver();
    }

    public static NetworkMonitor getInstance() {
        if (sInstance == null) {
            synchronized (NetworkMonitor.class) {
                if (sInstance == null) {
                    sInstance = new NetworkMonitor();
                }
            }
        }
        return sInstance;
    }


    /**
     * 注册receiver
     */
    private void registerReceiver() {
        Log.i(TAG, "registerReceiver enter");
        Context context = LSApp.me;
        if (null == context) {
            Log.i(TAG, "registerReceiver context = null");
            return;
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        try {
            context.registerReceiver(this, filter);
            Log.i(TAG, "registerReceiver isRegisterReceiver = true");
        } catch (Throwable t) {
            Log.e(TAG, "registerReceiver isRegisterReceiver = false" + t.getMessage());
        }
        Log.i(TAG, "registerReceiver exit");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //因为网络变化会有多次通知，所以这里做了个过滤,等待3秒，3秒后如果没有网络变化通知，则真正通知到下载网络有变
        Log.i(TAG, "network changed!");
        if (mNetworkChangedHandler == null) {
            mNetworkChangedHandler = new NetMsgHandler(this);
        }
        mNetworkChangedHandler.removeMessages(MSG_NET_CHANGE);
        Message msg = Message.obtain();
        msg.what = MSG_NET_CHANGE;
        mNetworkChangedHandler.sendMessageDelayed(msg, 3000);
    }


    /**
     * 弱引用，自己保存应用持有问题。
     *
     * @param l
     */
    public void registerNetListener(INetInterface l) {
        if (l == null) {
            return;
        }
        for (WeakReference<INetInterface> wL : mListeners) {
            if (wL != null && wL.get() == l) {
                return;
            }
        }
        mListeners.add(new WeakReference<INetInterface>(l));
    }


    /**
     * 使用弱引用，防止内存泄露。
     */
    private static class NetMsgHandler extends Handler {
        private WeakReference<NetworkMonitor> mOuter;

        public NetMsgHandler(NetworkMonitor outer) {
            mOuter = new WeakReference<>(outer);
        }

        @Override
        public void handleMessage(Message msg) {
            NetworkMonitor outer = mOuter.get();
            if (msg.what == MSG_NET_CHANGE && outer != null) {
                outer.notifyNetworkChanged();
            }
        }
    }

    private void notifyNetworkChanged() {

        boolean isNetAvailable = isNetworkConnected();
        for (WeakReference<INetInterface> wL : mListeners) {
            if (wL != null) {
                INetInterface l = wL.get();
                if (l != null) {
                    if (isNetAvailable) {
                        l.connected();
                    } else {
                        l.disConnected();
                    }
                }
            }
        }
    }

    public static boolean isNetworkConnected() {
        ConnectivityManager manager = (ConnectivityManager) LSApp.me
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            return false;
        }

        NetworkInfo networkinfo = manager.getActiveNetworkInfo();

        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }

        return true;
    }
}
