package space.onepantsu.oneresident;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class ResidentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!((OneResidentApp) getApplicationContext()).isAppForeground()) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_resident);
        }
    }

    public void onAddActivity(View view){
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    public void goBack(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        closeActivity();
    }

    private void closeActivity() {
        this.finish();
    }
}