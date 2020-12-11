package com.example.huaweitestproject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;

public class DemoHmsMessageService extends HmsMessageService {
    private static final String TAG = "PushDemoLog";
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.i(TAG, "receive token:" + token);
        sendTokenToDisplay(token);
        //Master change
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage != null){
            if (!remoteMessage.getData().isEmpty()) {
                Log.d("HMS", "Payload" + remoteMessage.getData());
            }

            if (remoteMessage.getNotification() != null) {
                Log.d("HMS", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }
        }
    }

    private void sendTokenToDisplay(String token) {
        Intent intent = new Intent("com.huawei.push.codelab.ON_NEW_TOKEN");
        intent.putExtra("token", token);
        sendBroadcast(intent);
    }
}

