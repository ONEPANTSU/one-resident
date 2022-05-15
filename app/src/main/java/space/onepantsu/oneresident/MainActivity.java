package space.onepantsu.oneresident;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!((OneResidentApp) getApplicationContext()).isAppForeground()){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        }
    }

    public void onResidentActivity(View view){
        Intent intent = new Intent(this, ResidentActivity.class);
        startActivity(intent);
        this.finish();
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