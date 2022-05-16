package space.onepantsu.oneresident;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import space.onepantsu.oneresident.database.DBMS;
import space.onepantsu.oneresident.database.DataBase;

public class ResidentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resident);
        checkResidents();
    }

    public void checkResidents(){
        DBMS dbms = new DBMS(this);
        SQLiteDatabase db = dbms.getReadableDatabase();

        String[] projection = {DataBase.ResidentsTable._ID,
                DataBase.ResidentsTable.COLUMN_CITY, DataBase.ResidentsTable.COLUMN_STREET, DataBase.ResidentsTable.COLUMN_HOUSE,
                DataBase.ResidentsTable.COLUMN_LEVEL, DataBase.ResidentsTable.COLUMN_FLAT, DataBase.ResidentsTable.COLUMN_SURNAME,
                DataBase.ResidentsTable.COLUMN_NAME, DataBase.ResidentsTable.COLUMN_SECONDNAME, DataBase.ResidentsTable.COLUMN_PHONE,
                DataBase.ResidentsTable.COLUMN_DATE, DataBase.ResidentsTable.COLUMN_PRICE, DataBase.ResidentsTable.COLUMN_COMMENT};

        Cursor cursor = db.query(
                DataBase.ResidentsTable.TABLE_NAME,   // таблица
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        int idColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable._ID);
        int cityColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_CITY);
        int streetColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_STREET);
        int houseColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_HOUSE);
        int levelColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_LEVEL);
        int flatColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_FLAT);
        int surnameColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_SURNAME);
        int nameColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_NAME);
        int secondnameColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_SECONDNAME);
        int phoneColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_PHONE);
        int dateColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_DATE);
        int priceColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_PRICE);
        int commentColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_COMMENT);


        while (cursor.moveToNext()) {
            try {
                // Используем индекс для получения строки или числа
                int currentID = cursor.getInt(idColumnIndex);
                String currentCity = cursor.getString(cityColumnIndex);
                String currentStreet = cursor.getString(streetColumnIndex);
                String currentHouse = cursor.getString(houseColumnIndex);
                Integer currentLevel = cursor.getInt(levelColumnIndex);
                Integer currentFlat = cursor.getInt(flatColumnIndex);
                String currentSurname = cursor.getString(surnameColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentSecondname = cursor.getString(secondnameColumnIndex);
                String currentPhone = cursor.getString(phoneColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);
                Integer currentPrice = cursor.getInt(priceColumnIndex);
                String currentComment = cursor.getString(dateColumnIndex);


            }
            catch (Exception e){
                System.out.println("Ошибка при чтении строки");
            }
        }

    }


    public void onAddActivity(View view){
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
        closeActivity();
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