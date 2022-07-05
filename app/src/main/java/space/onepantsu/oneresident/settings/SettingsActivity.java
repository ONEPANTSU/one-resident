package space.onepantsu.oneresident.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import space.onepantsu.oneresident.MainActivity;
import space.onepantsu.oneresident.R;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    EditText timeSettings;
    static final public String ALARM_HOURS = "alarm_hours";
    static final public String ALARM_MINUTES = "alarm_minutes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);

        if(sharedPreferences.getString(ALARM_HOURS, "").equals("") || sharedPreferences.getString(ALARM_MINUTES, "").equals("")){
            savePreferences("12", "00", this.sharedPreferences);
        }

        timeSettings = findViewById(R.id.settingsEditTime);
        timeSettings.setHint(sharedPreferences.getString(ALARM_HOURS, "") + ":" + sharedPreferences.getString(ALARM_MINUTES, ""));
    }

    public void acceptChanges(View view) {
        if (!timeSettings.getText().toString().equals("")){
            if(checkTime()){
                String hours, minutes;
                String time = timeSettings.getText().toString();
                hours = time.charAt(0) + String.valueOf(time.charAt(1));
                minutes = time.charAt(3) + String.valueOf(time.charAt(4));
                savePreferences(hours, minutes, this.sharedPreferences);

                Toast.makeText(this, "Настройки сохранены",  Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(this, "Задан неверный формат времени!",  Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Задан неверный формат времени!",  Toast.LENGTH_SHORT).show();
        }
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

    public static void savePreferences(String hours, String minutes, SharedPreferences sharedPreferences){
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(ALARM_HOURS, hours);
        editor.putString(ALARM_MINUTES, minutes);
        editor.apply();
        }

    private boolean checkTime(){
        String time = timeSettings.getText().toString();
        int hours, minutes;
        if(time.length() == 5 && time.charAt(2) == ':'){
            hours = (int) time.charAt(0) * 10 + (int) time.charAt(1) - 528;
            minutes = (int) time.charAt(3) * 10 + (int) time.charAt(4) - 528;
            return hours >= 0 && hours < 24 && minutes >= 0 && minutes < 60;
        }
        return false;
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        closeActivity();
    }

    public void goBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        closeActivity();
    }

    private void closeActivity() {
        this.finish();
    }

}