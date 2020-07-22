package app.consulto.seller;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import app.consulto.MainActivity;

public class boot_listener extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i =new Intent(context,customer_service.class);
        context.startService(i);
        }
}