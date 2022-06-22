package space.onepantsu.oneresident.service;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import space.onepantsu.oneresident.MainActivity;
import space.onepantsu.oneresident.R;
import space.onepantsu.oneresident.payment.PaymentActivity;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String[] randomTitles = new String[] {"Час расплаты!", "Пора платить по счетам!",
                "Кажется, вам кто-то должен кругленькую сумму!", "Пора собирать дань!"};

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("ALARM", "On RECIEVE");

        try {
            int NOTIFICATION_ID = 234;
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String CHANNEL_ID = "channel_01";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                CharSequence name = "my_channel";
                String Description = "This is my channel";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mChannel.setDescription(Description);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setShowBadge(false);
                notificationManager.createNotificationChannel(mChannel);
            }

            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context, CHANNEL_ID)
                            .setContentTitle(randomTitles[(int) (Math.random() * randomTitles.length)])
                            .setContentText(intent.getStringExtra("text"))
                            .setSmallIcon(R.drawable.house_block);

            Intent resultIntent = new Intent(context, PaymentActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}