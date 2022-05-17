package space.onepantsu.oneresident;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import space.onepantsu.oneresident.database.DBMS;
import space.onepantsu.oneresident.database.DataBase;

public class ChangeResidentActivity extends AppCompatActivity {

    ResidentActivity.ResidentInfo resident;
    EditText city, street, house, level, flat,
            surname, name, secondname, phone, date, period, price, comment;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_resident);
        Bundle arguments = getIntent().getExtras();

        city = (EditText) findViewById(R.id.textCity);
        street = (EditText) findViewById(R.id.textStreet);
        house = (EditText) findViewById(R.id.textHouse);
        level = (EditText) findViewById(R.id.textLevel);
        flat = (EditText) findViewById(R.id.textFlat);
        surname = (EditText) findViewById(R.id.textSurname);
        name = (EditText) findViewById(R.id.textName);
        secondname = (EditText) findViewById(R.id.textSecondName);
        phone = (EditText) findViewById(R.id.textPhone);
        date = (EditText) findViewById(R.id.textDate);
        period = (EditText) findViewById(R.id.textPeriod);
        price = (EditText) findViewById(R.id.textPrice);
        comment = (EditText) findViewById(R.id.textComment);

        if (arguments != null) {
            resident = (ResidentActivity.ResidentInfo)
                    arguments.getSerializable(ResidentActivity.ResidentInfo.class.getSimpleName());

            if(resident.currentCity != "" && resident.currentCity != null){
                city.setText(resident.currentCity);
            }
            if(resident.currentStreet != "" && resident.currentStreet != null){
                street.setText(resident.currentStreet);
            }
            if(resident.currentHouse != "" && resident.currentHouse != null) {
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
            if(resident.currentSurname != "" && resident.currentSurname != null) {
                surname.setText(resident.currentSurname);
            }
            if(resident.currentName != "" && resident.currentName != null) {
                name.setText(resident.currentName);
            }
            if(resident.currentSecondname != "" && resident.currentSecondname != null) {
                secondname.setText(resident.currentSecondname);
            }
            if(resident.currentPhone != "" && resident.currentPhone != null) {
                phone.setText(resident.currentPhone);
            }
            if(resident.currentDate != "" && resident.currentDate != null) {
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
            if(resident.currentComment != "" && resident.currentComment != null) {
                comment.setText(resident.currentComment);
            }
        }
    }

    public void saveChanges(View view){

        DBMS dbms = new DBMS(this);
        SQLiteDatabase db = dbms.getWritableDatabase();
        ContentValues newValues = new ContentValues();

        newValues.put(DataBase.ResidentsTable.COLUMN_CITY, city.getText().toString());
        newValues.put(DataBase.ResidentsTable.COLUMN_STREET, street.getText().toString());
        newValues.put(DataBase.ResidentsTable.COLUMN_HOUSE, house.getText().toString());
        newValues.put(DataBase.ResidentsTable.COLUMN_LEVEL, level.getText().toString());
        newValues.put(DataBase.ResidentsTable.COLUMN_FLAT, flat.getText().toString());
        newValues.put(DataBase.ResidentsTable.COLUMN_SURNAME, surname.getText().toString());
        newValues.put(DataBase.ResidentsTable.COLUMN_NAME, name.getText().toString());
        newValues.put(DataBase.ResidentsTable.COLUMN_SECONDNAME, secondname.getText().toString());
        newValues.put(DataBase.ResidentsTable.COLUMN_PHONE, phone.getText().toString());
        newValues.put(DataBase.ResidentsTable.COLUMN_DATE, date.getText().toString());
        newValues.put(DataBase.ResidentsTable.COLUMN_PERIOD, period.getText().toString());
        newValues.put(DataBase.ResidentsTable.COLUMN_PRICE, price.getText().toString());
        newValues.put(DataBase.ResidentsTable.COLUMN_COMMENT, comment.getText().toString());

        String where = DataBase.ResidentsTable._ID + "=" + resident.currentID;

        try {
            db.update(DataBase.ResidentsTable.TABLE_NAME, newValues, where, null);
            Toast.makeText(this, "Арендатор успешно изменён", Toast.LENGTH_SHORT).show();
            back();
        }
        catch (Exception e){
            Toast.makeText(this, "Ошибка при изменении арендатора", Toast.LENGTH_SHORT).show();
        }
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