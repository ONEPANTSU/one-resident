package space.onepantsu.oneresident;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import space.onepantsu.oneresident.history.HistoryActivity;
import space.onepantsu.oneresident.payment.PaymentActivity;
import space.onepantsu.oneresident.residents.ResidentActivity;
import space.onepantsu.oneresident.service.OneService;
import space.onepantsu.oneresident.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!((OneResidentApp) getApplicationContext()).isAppForeground()){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            startService(new Intent(this, OneService.class));
        }
    }

    public void onResidentActivity(View view){
        Intent intent = new Intent(this, ResidentActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void onPaymentActivity(View view){
        Intent intent = new Intent(this, PaymentActivity.class);
        startActivity(intent);
    }

    public void onHistoryActivity(View view){
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    public void onSettingsActivity(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

}