package space.onepantsu.oneresident.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import space.onepantsu.oneresident.MainActivity;
import space.onepantsu.oneresident.R;
import space.onepantsu.oneresident.dialogframe.AcceptButton;
import space.onepantsu.oneresident.dialogframe.DeleteResidentButton;
import space.onepantsu.oneresident.dialogframe.DialogFrame;
import space.onepantsu.oneresident.dialogframe.InfoButton;
import space.onepantsu.oneresident.payment.database.PaymentDB;
import space.onepantsu.oneresident.payment.database.PaymentDBMS;
import space.onepantsu.oneresident.payment.database.PaymentStatus;
import space.onepantsu.oneresident.residentManagement.AddActivity;
import space.onepantsu.oneresident.residentManagement.ResidentActivity;
import space.onepantsu.oneresident.residentManagement.ResidentInfoActivity;
import space.onepantsu.oneresident.residentManagement.database.DBMS;
import space.onepantsu.oneresident.residentManagement.database.DataBase;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        linear = (LinearLayout) findViewById(R.id.residentLinear);
        checkPayment();
    }

    LinearLayout linear;

    PaymentDBMS dbms = new PaymentDBMS(this);

    public static class PaymentInfo implements Serializable {
        public int currentID;
        public String currentStatus;
    }

    public void checkPayment(){
        SQLiteDatabase db = dbms.getReadableDatabase();

        String[] projection = {
                PaymentDB.PaymentTable._ID,  PaymentDB.PaymentTable.STATUS};

        Cursor cursor = db.query(
                PaymentDB.PaymentTable.TABLE_NAME,   // таблица
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        int idColumnIndex = cursor.getColumnIndex(PaymentDB.PaymentTable._ID);
        int statusColumnIndex = cursor.getColumnIndex(PaymentDB.PaymentTable.STATUS);

        while (cursor.moveToNext()) {
            try {
                PaymentInfo paymentInfo = new PaymentInfo();

                paymentInfo.currentID = cursor.getInt(idColumnIndex);
                paymentInfo.currentStatus = cursor.getString(statusColumnIndex);

                addPaymentView(paymentInfo);

            }
            catch (Exception e){
                System.out.println("Ошибка при чтении строки");
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private void addPaymentView(PaymentInfo paymentInfo){
        @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.custom_payment_layout, null);

        TextView paymentText = (TextView) view.findViewById(R.id.paymentInfo);



        StringBuilder paymentTextBuilder = new StringBuilder();

        paymentTextBuilder.append(getResidentInfo(paymentInfo.currentID) + "\t||\tSTATUS:\t" + paymentInfo.currentStatus);
        paymentText.setText(paymentTextBuilder.toString());

        Button paidButton = (Button) view.findViewById(R.id.paidButton);
        paidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wasPaid(paymentInfo, v);
            }
        });
        if(paymentInfo.currentStatus.equals(String.valueOf(PaymentStatus.PAID))){
            paidButton.setClickable(false);
        }

        linear.addView(view);
    }

    private String getResidentInfo(int id){

        DBMS dbms = new DBMS(this);
        SQLiteDatabase db = dbms.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + DataBase.ResidentsTable.TABLE_NAME +
                " WHERE " + DataBase.ResidentsTable._ID + " = " + id;
        Cursor cursor = db.rawQuery(selectQuery, null);

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

        while (cursor.moveToNext()) {
            try {
                ResidentActivity.ResidentInfo newResident = new ResidentActivity.ResidentInfo();

                newResident.currentID = cursor.getInt(idColumnIndex);
                newResident.currentCity = cursor.getString(cityColumnIndex);
                newResident.currentStreet = cursor.getString(streetColumnIndex);
                newResident.currentHouse = cursor.getString(houseColumnIndex);
                newResident.currentLevel = cursor.getInt(levelColumnIndex);
                newResident.currentFlat = cursor.getInt(flatColumnIndex);
                newResident.currentSurname = cursor.getString(surnameColumnIndex);
                newResident.currentName = cursor.getString(nameColumnIndex);
                newResident.currentSecondname = cursor.getString(secondnameColumnIndex);
                newResident.currentPhone = cursor.getString(phoneColumnIndex);
                newResident.currentDate = cursor.getString(dateColumnIndex);
                newResident.currentPeriod = cursor.getInt(periodColumnIndex);
                newResident.currentPrice = cursor.getInt(priceColumnIndex);
                newResident.currentComment = cursor.getString(commentColumnIndex);

                StringBuilder residentInfoBttnTextBuilder = new StringBuilder();
                if(!newResident.currentCity.equals("")){
                    residentInfoBttnTextBuilder.append("г." + newResident.currentCity + ",\t ");
                }
                if(!newResident.currentStreet.equals("")){
                    residentInfoBttnTextBuilder.append("ул." + newResident.currentStreet + ",\t ");
                }
                if(!newResident.currentHouse.equals("")){
                    residentInfoBttnTextBuilder.append("д." + newResident.currentHouse);
                }
                if(newResident.currentFlat > 0){
                    residentInfoBttnTextBuilder.append(",\t кв." + newResident.currentFlat);
                }
                if(!newResident.currentDate.equals("")){
                    residentInfoBttnTextBuilder.append("\n" + newResident.currentDate);
                }
                return residentInfoBttnTextBuilder.toString();

            }
            catch (Exception e){
                System.out.println("Ошибка при чтении строки");
            }
        }
        return "";
    }


    public void wasPaid(PaymentInfo paymentInfo, View view){
        PaymentDBMS dbms = new PaymentDBMS(this);
        SQLiteDatabase db = dbms.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        newValues.put(PaymentDB.PaymentTable.STATUS, String.valueOf(PaymentStatus.PAID));
        String where = PaymentDB.PaymentTable._ID + "=" + paymentInfo.currentID;

        try {
            db.update(PaymentDB.PaymentTable.TABLE_NAME, newValues, where, null);
            Toast.makeText(this, "Оплата успешно произведена", Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        } catch (Exception e) {
            InfoButton dialogButton = new InfoButton();
            DialogFrame warning = new DialogFrame("Ошибка при проведении оплаты", "", dialogButton);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            warning.show(transaction, "dialog");
        }
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