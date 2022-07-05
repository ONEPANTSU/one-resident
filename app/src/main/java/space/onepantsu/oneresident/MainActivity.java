package space.onepantsu.oneresident;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import space.onepantsu.oneresident.history.HistoryActivity;
import space.onepantsu.oneresident.payment.PaymentActivity;
import space.onepantsu.oneresident.residents.ResidentActivity;
import space.onepantsu.oneresident.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    private static long back_pressed;
    final String ALARM_HOURS = "alarm_hours";
    final String ALARM_MINUTES = "alarm_minutes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!((OneResidentApp) getApplicationContext()).isAppForeground()){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            SharedPreferences sharedPreferences;
            sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);

            if(sharedPreferences.getString(ALARM_HOURS, "").equals("") || sharedPreferences.getString(ALARM_MINUTES, "").equals("")){
                SettingsActivity.savePreferences("12", "00", sharedPreferences);
            }
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
        this.finish();
    }

    public void onHistoryActivity(View view){
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void onSettingsActivity(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), "Нажмите еще раз для выхода", Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

}