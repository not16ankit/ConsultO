package app.consulto.seller;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.Calendar;

public class customer_service extends Service {
    public customer_service() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel;
        Notification.Builder nb = new Notification.Builder(this);
        nb.setSmallIcon(app.consulto.R.drawable.logo);
        nb.setContentText("New user waiting");
        nb.setSubText("Tap Here");
        nb.setOngoing(true);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            channel = new NotificationChannel("gm_channel","Great Minds",NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("GM notis");
            channel.enableLights(true);
            channel.setLightColor(Color.CYAN);
            nm.createNotificationChannel(channel);
            nb.setChannelId("gm_channel");
        }
        FirebaseApp.initializeApp(this);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference("live1");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.getValue().toString().isEmpty())
                {
                    nb.setContentText("New user waiting");
                    startForeground(66,nb.build());
                }
                else
                {
                    nb.setContentText("No Pending Requests");
                    startForeground(66,nb.build());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return Service.START_STICKY;
    }
}
