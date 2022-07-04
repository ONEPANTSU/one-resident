package space.onepantsu.oneresident.history;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
        public String currentResidentName;
        public String currentResidentSurname;
        public int currentResidentID;
        public HistoryType currentType;
    }


    private void checkHistory(){

        SQLiteDatabase db = dbms.getReadableDatabase();

        String[] projection = { HistoryDB.HistoryTable._ID, HistoryDB.HistoryTable.DATE,
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
        int residentNameColumnIndex = cursor.getColumnIndex(HistoryDB.HistoryTable.RESIDENT_NAME);
        int residentSurnameColumnIndex = cursor.getColumnIndex(HistoryDB.HistoryTable.RESIDENT_SURNAME);
        int typeColumnIndex = cursor.getColumnIndex(HistoryDB.HistoryTable.TYPE);

        while (cursor.moveToNext()) {
            try {
                HistoryInfo historyInfo = new HistoryInfo();

                historyInfo.currentID = cursor.getInt(idColumnIndex);
                historyInfo.currentDate = cursor.getString(dataColumnIndex);
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
            case "INCREASED_DEBT": type = "Арендатору надо внести оплату"; break;
            case "WAS_PAID": type = "Арендатор внёс оплату"; break;
            case "CHANGED_DATE": type = "Перенос даты оплаты арендатора"; break;
            case "DELETED_RESIDENT": type = "Арендатор был удалён"; break;
            case "ADDED_RESIDENT": type = "Арендатор был дабвлен"; break;
            default: break;
        }

        return historyInfo.currentResidentSurname + ' ' + historyInfo.currentResidentName
                + '\n' + type + '\n' + historyInfo.currentDate;
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

    public void goBack(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        closeActivity();
    }

    private void closeActivity() {
        this.finish();
    }

}