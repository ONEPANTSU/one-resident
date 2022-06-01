package space.onepantsu.oneresident.payment;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import space.onepantsu.oneresident.R;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        addAlarmNotification(0, "ALARM", "SOME TEXT");
    }

    private void addAlarmNotification(long startTime, String title, String text){
        AlarmManager alarmManager;

        PendingIntent alarmIntent;

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("text", text);
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), (int)startTime, intent, 0);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, startTime, alarmIntent);
    }


}