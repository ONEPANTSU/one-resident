package space.onepantsu.oneresident.payment;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import space.onepantsu.oneresident.R;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.drawable.logo)
                    .setContentTitle(intent.getStringExtra("title"))
                    .setContentText(intent.getStringExtra("text"))
                    .setAutoCancel(true)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setLights(0x0000FF, 3000, 2000);
            notificationManager.notify(56, builder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}