package com.example.dakashenqi;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by waj on 13-12-26.
 */
public class MyReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context,MyService.class);
        context.startService(intent1);
    }
}
