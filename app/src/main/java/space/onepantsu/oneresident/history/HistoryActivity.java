package space.onepantsu.oneresident.history;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import space.onepantsu.oneresident.MainActivity;
import space.onepantsu.oneresident.R;
import space.onepantsu.oneresident.history.database.HistoryDB;
import space.onepantsu.oneresident.history.database.HistoryDBMS;
import space.onepantsu.oneresident.history.database.HistoryType;

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
        public String currentResidentObject;
        public String currentResidentCity;
        public String currentResidentStreet;
        public String currentResidentHouse;
        public Integer currentResidentFlat;
        public String currentResidentName;
        public String currentResidentSurname;
        public int currentResidentID;
        public HistoryType currentType;
    }


    private void checkHistory(){

        SQLiteDatabase db = dbms.getReadableDatabase();

        String[] projection = { HistoryDB.HistoryTable._ID,
                                HistoryDB.HistoryTable.DATE, HistoryDB.HistoryTable.RESIDENT_OBJECT,
                                HistoryDB.HistoryTable.RESIDENT_CITY, HistoryDB.HistoryTable.RESIDENT_STREET,
                                HistoryDB.HistoryTable.RESIDENT_HOUSE, HistoryDB.HistoryTable.RESIDENT_FLAT,
                                HistoryDB.HistoryTable.RESIDENT_ID, HistoryDB.HistoryTable.RESIDENT_NAME,
                                HistoryDB.HistoryTable.RESIDENT_SURNAME, HistoryDB.HistoryTable.TYPE };

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
        int residentObjectColumnIndex = cursor.getColumnIndex(HistoryDB.HistoryTable.RESIDENT_OBJECT);
        int residentCityColumnIndex = cursor.getColumnIndex(HistoryDB.HistoryTable.RESIDENT_CITY);
        int residentStreetColumnIndex = cursor.getColumnIndex(HistoryDB.HistoryTable.RESIDENT_STREET);
        int residentHouseColumnIndex = cursor.getColumnIndex(HistoryDB.HistoryTable.RESIDENT_HOUSE);
        int residentFLatColumnIndex = cursor.getColumnIndex(HistoryDB.HistoryTable.RESIDENT_FLAT);
        int residentNameColumnIndex = cursor.getColumnIndex(HistoryDB.HistoryTable.RESIDENT_NAME);
        int residentSurnameColumnIndex = cursor.getColumnIndex(HistoryDB.HistoryTable.RESIDENT_SURNAME);
        int typeColumnIndex = cursor.getColumnIndex(HistoryDB.HistoryTable.TYPE);

        while (cursor.moveToNext()) {
            try {
                HistoryInfo historyInfo = new HistoryInfo();

                historyInfo.currentID = cursor.getInt(idColumnIndex);
                historyInfo.currentDate = cursor.getString(dataColumnIndex);

                historyInfo.currentResidentObject = cursor.getString(residentObjectColumnIndex);
                historyInfo.currentResidentCity = cursor.getString(residentCityColumnIndex);
                historyInfo.currentResidentStreet = cursor.getString(residentStreetColumnIndex);
                historyInfo.currentResidentHouse = cursor.getString(residentHouseColumnIndex);
                historyInfo.currentResidentFlat = Integer.valueOf(cursor.getString(residentFLatColumnIndex));

                historyInfo.currentResidentName = cursor.getString(residentNameColumnIndex);
                historyInfo.currentResidentSurname = cursor.getString(residentSurnameColumnIndex);
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

        switch (historyInfo.currentType.toString()){
            case "INCREASED_DEBT": type = "Арендатору необходимо внести оплату"; break;
            case "WAS_PAID": type = "Арендатор внёс оплату"; break;
            case "CHANGED_DATE": type = "Перенос даты оплаты арендатора"; break;
            case "DELETED_RESIDENT": type = "Арендатор был удалён"; break;
            case "ADDED_RESIDENT": type = "Арендатор был дабавлен"; break;
            default: break;
        }

        StringBuilder historyInfoTextBuilder = new StringBuilder();

        int maxLength = 20;

        if(!historyInfo.currentResidentObject.equals("")){
            historyInfoTextBuilder.append("\"").append(historyInfo.currentResidentObject).append("\"");

            int currentLenght = historyInfoTextBuilder.toString().length();
            if (currentLenght > maxLength) {
                historyInfoTextBuilder.delete(maxLength - 3, currentLenght);
                historyInfoTextBuilder.append("...\"");
            }
        }
        else{
            if (!historyInfo.currentResidentStreet.equals("")) {
                historyInfoTextBuilder.append("ул.").append(historyInfo.currentResidentStreet);

                int currentLenght = historyInfoTextBuilder.toString().length();

                if (currentLenght > maxLength) {
                    historyInfoTextBuilder.delete(maxLength - 3, currentLenght);
                    historyInfoTextBuilder.append("...");
                }
                historyInfoTextBuilder.append("\n");
            }
            if (!historyInfo.currentResidentHouse.equals("")) {
                historyInfoTextBuilder.append("д.").append(historyInfo.currentResidentHouse);
            }
            if (historyInfo.currentResidentFlat > 0) {
                historyInfoTextBuilder.append(",\t кв.").append(historyInfo.currentResidentFlat);
            }
        }

        historyInfoTextBuilder.append('\n');
        historyInfoTextBuilder.append(type);
        historyInfoTextBuilder.append('\n');
        historyInfoTextBuilder.append(historyInfo.currentDate);

        return historyInfoTextBuilder.toString();
    }

    public void clearHistory(){
        SQLiteDatabase db = dbms.getReadableDatabase();
        db.delete(HistoryDB.HistoryTable.TABLE_NAME, null, null);
        Toast.makeText(this, "История оплаты была очищена", Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
    public void deleteDialog(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Вы уверены, что хотите удалить историю оплаты?")
                .setPositiveButton("ОК", (dialog, id) -> clearHistory())
                .setNegativeButton("Отмена", (dialog, id) -> {});
        builder.create().show();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(this, MainActivity.class);
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