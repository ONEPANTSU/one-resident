package space.onepantsu.oneresident.residents;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import space.onepantsu.oneresident.R;

public class ResidentInfoActivity extends AppCompatActivity {

    ResidentActivity.ResidentInfo resident;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident_info);
        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            resident = (ResidentActivity.ResidentInfo)
                    arguments.getSerializable(ResidentActivity.ResidentInfo.class.getSimpleName());

            TextView city = findViewById(R.id.textCity);
            TextView street = findViewById(R.id.textStreet);
            TextView house = findViewById(R.id.textHouse);
            TextView level = findViewById(R.id.textLevel);
            TextView flat = findViewById(R.id.textFlat);
            TextView surname = findViewById(R.id.textSurname);
            TextView name = findViewById(R.id.textName);
            TextView secondname = findViewById(R.id.textSecondName);
            TextView phone = findViewById(R.id.textPhone);
            TextView date = findViewById(R.id.textDate);
            TextView period = findViewById(R.id.textPeriod);
            TextView price = findViewById(R.id.textPrice);
            TextView comment = findViewById(R.id.textComment);

            if(!resident.currentCity.equals("")){
                city.setText(resident.currentCity);
            }
            if(!resident.currentStreet.equals("")){
                street.setText(resident.currentStreet);
            }
            if(!resident.currentHouse.equals("")) {
                house.setText(resident.currentHouse);
            }
            if(resident.currentLevel != null && resident.currentLevel != 0) {
                String levelValue = resident.currentLevel.toString();
                level.setText(levelValue);
            }
            if(resident.currentFlat != null && resident.currentFlat != 0) {
                String flatValue = resident.currentFlat.toString();
                flat.setText(flatValue);
            }
            if(!resident.currentSurname.equals("")) {
                surname.setText(resident.currentSurname);
            }
            if(!resident.currentName.equals("")) {
                name.setText(resident.currentName);
            }
            if(!resident.currentSecondname.equals("")) {
                secondname.setText(resident.currentSecondname);
            }
            if(!resident.currentPhone.equals("")) {
                phone.setText(resident.currentPhone);
            }
            if(!resident.currentDate.equals("")) {
                date.setText(resident.currentDate);
            }
            if(resident.currentPeriod != null && resident.currentPeriod != 0) {
                String periodValue = resident.currentPeriod.toString();
                period.setText(periodValue);
            }
            if(resident.currentPrice != null && resident.currentPrice != 0) {
                String priceValue = resident.currentPrice.toString();
                price.setText(priceValue);
            }
            if(!resident.currentComment.equals("")) {
                comment.setText(resident.currentComment);
            }
        }

    }


    public void changeResident(View view){
        Intent intent = new Intent(ResidentInfoActivity.this, ChangeResidentActivity.class);
        intent.putExtra(ResidentActivity.ResidentInfo.class.getSimpleName(), resident);
        startActivity(intent);
        closeActivity();
    }


    public void goBack(View view){
        back();
    }

    public void back(){
        Intent intent = new Intent(this, ResidentActivity.class);
        startActivity(intent);
        closeActivity();
    }

    private void closeActivity() {
        this.finish();
    }
}