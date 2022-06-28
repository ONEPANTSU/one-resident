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

public class HistoryActivity extends AppCompatActivity {

    LinearLayout linear;
    HistoryDBMS dbms = new HistoryDBMS(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        linear = findViewById(R.id.residentLinear);
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

        String[] projection = { HistoryDB.HistoryTable._ID, HistoryDB.HistoryTable.DATA,
                                HistoryDB.HistoryTable.RESIDENT_ID, HistoryDB.HistoryTable.TYPE };

        @SuppressLint("Recycle") Cursor cursor = db.query(
                HistoryDB.HistoryTable.TABLE_NAME,   // таблица
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        int idColumnIndex = cursor.getColumnIndex(HistoryDB.HistoryTable._ID);
        int dataColumnIndex = cursor.getColumnIndex(HistoryDB.HistoryTable.DATA);
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

        String paymentTextBuilder = getHistoryInfo(historyInfo.currentID);

        paymentText.setText(paymentTextBuilder);


        linear.addView(view);
    }

    private String getHistoryInfo(int id){
        return null;
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