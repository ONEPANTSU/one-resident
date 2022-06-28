package space.onepantsu.oneresident.history;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;

import space.onepantsu.oneresident.MainActivity;
import space.onepantsu.oneresident.R;
import space.onepantsu.oneresident.history.database.HistoryDB;
import space.onepantsu.oneresident.history.database.HistoryDBMS;
import space.onepantsu.oneresident.history.database.HistoryType;
import space.onepantsu.oneresident.residents.ResidentActivity;
import space.onepantsu.oneresident.residents.database.DBMS;
import space.onepantsu.oneresident.residents.database.DataBase;

public class HistoryActivity extends AppCompatActivity {

    LinearLayout linear;
    HistoryDBMS dbms = new HistoryDBMS(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        linear = findViewById(R.id.historyLinear);
        checkHistory();
    }

    public static class HistoryInfo implements Serializable {
        public int currentID;
        public String currentDate;
        public int currentResidentID;
        public HistoryType currentType;
    }


    private void checkHistory(){

        SQLiteDatabase db = dbms.getReadableDatabase();

        String[] projection = { HistoryDB.HistoryTable._ID, HistoryDB.HistoryTable.DATE,
                                HistoryDB.HistoryTable.RESIDENT_ID, HistoryDB.HistoryTable.TYPE };

        @SuppressLint("Recycle") Cursor cursor = db.query(
                HistoryDB.HistoryTable.TABLE_NAME,   // таблица
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                HistoryDB.HistoryTable._ID + " DESC");                   // порядок сортировки

        int idColumnIndex = cursor.getColumnIndex(HistoryDB.HistoryTable._ID);
        int dataColumnIndex = cursor.getColumnIndex(HistoryDB.HistoryTable.DATE);
        int residentIDColumnIndex = cursor.getColumnIndex(HistoryDB.HistoryTable.RESIDENT_ID);
        int typeColumnIndex = cursor.getColumnIndex(HistoryDB.HistoryTable.TYPE);

        while (cursor.moveToNext()) {
            try {
                HistoryInfo historyInfo = new HistoryInfo();

                historyInfo.currentID = cursor.getInt(idColumnIndex);
                historyInfo.currentDate = cursor.getString(dataColumnIndex);
                historyInfo.currentResidentID = cursor.getInt(residentIDColumnIndex);
                historyInfo.currentType = HistoryType.valueOf(cursor.getString(typeColumnIndex));

                addHistoryView(historyInfo);

            }
            catch (Exception e){
                System.out.println("Ошибка при чтении строки");
            }
        }

    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void addHistoryView(HistoryInfo historyInfo) {
        @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.custom_history_layout, null);

        TextView paymentText = view.findViewById(R.id.historyInfo);

        String paymentTextBuilder = getHistoryInfo(historyInfo);

        paymentText.setText(paymentTextBuilder);

        linear.addView(view);
    }

    private String getHistoryInfo(HistoryInfo historyInfo){

        String type = "";

        ResidentActivity.ResidentInfo residentInfo = getResidentInfoByID(historyInfo.currentResidentID);

        switch (historyInfo.currentType.toString()){
            case "INCREASED_DEBT": type = "Арендатору необходимо внести оплату"; break;
            case "WAS_PAID": type = "Арендатор внес оплата"; break;
            case "CHANGED_DATE": type = "Перенос даты оплаты арендатора на " + residentInfo.currentDate; break;
            case "DELETED_RESIDENT": type = "Арендатор был удалён"; break;
            case "ADDED_RESIDENT": type = "Арендатор был дабвлен"; break;
            default: break;
        }

        return historyInfo.currentDate + "\t" + residentInfo.currentSurname + " "
                + residentInfo.currentName + "\n" + type;
    }

    private ResidentActivity.ResidentInfo getResidentInfoByID(int id){
        DBMS dbms = new DBMS(this);
        SQLiteDatabase db = dbms.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + DataBase.ResidentsTable.TABLE_NAME +
                " WHERE " + DataBase.ResidentsTable._ID + " = " + id;
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

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
        int periodColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_PERIOD);
        int priceColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_PRICE);
        int commentColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_COMMENT);

        ResidentActivity.ResidentInfo residentInfo = new ResidentActivity.ResidentInfo();

        while (cursor.moveToNext()) {
            try {
                residentInfo.currentID = cursor.getInt(idColumnIndex);
                residentInfo.currentCity = cursor.getString(cityColumnIndex);
                residentInfo.currentStreet = cursor.getString(streetColumnIndex);
                residentInfo.currentHouse = cursor.getString(houseColumnIndex);
                residentInfo.currentLevel = cursor.getInt(levelColumnIndex);
                residentInfo.currentFlat = cursor.getInt(flatColumnIndex);
                residentInfo.currentSurname = cursor.getString(surnameColumnIndex);
                residentInfo.currentName = cursor.getString(nameColumnIndex);
                residentInfo.currentSecondname = cursor.getString(secondnameColumnIndex);
                residentInfo.currentPhone = cursor.getString(phoneColumnIndex);
                residentInfo.currentDate = cursor.getString(dateColumnIndex);
                residentInfo.currentPeriod = cursor.getInt(periodColumnIndex);
                residentInfo.currentPrice = cursor.getInt(priceColumnIndex);
                residentInfo.currentComment = cursor.getString(commentColumnIndex);

            }
            catch (Exception e){
                System.out.println("Ошибка при чтении строки");
            }
        }

        return residentInfo;
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