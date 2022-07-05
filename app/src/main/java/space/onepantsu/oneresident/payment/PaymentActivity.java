package space.onepantsu.oneresident.payment;

import static space.onepantsu.oneresident.settings.SettingsActivity.ALARM_HOURS;
import static space.onepantsu.oneresident.settings.SettingsActivity.ALARM_MINUTES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;

import space.onepantsu.oneresident.MainActivity;
import space.onepantsu.oneresident.R;
import space.onepantsu.oneresident.dialogframe.DialogFrame;
import space.onepantsu.oneresident.dialogframe.InfoButton;
import space.onepantsu.oneresident.history.database.HistoryDB;
import space.onepantsu.oneresident.history.database.HistoryDBMS;
import space.onepantsu.oneresident.history.database.HistoryType;
import space.onepantsu.oneresident.payment.database.PaymentDB;
import space.onepantsu.oneresident.payment.database.PaymentDBMS;
import space.onepantsu.oneresident.payment.database.PaymentStatus;
import space.onepantsu.oneresident.residents.Error;
import space.onepantsu.oneresident.residents.ResidentActivity;
import space.onepantsu.oneresident.residents.ResidentInfoActivity;
import space.onepantsu.oneresident.residents.database.DBMS;
import space.onepantsu.oneresident.residents.database.DataBase;
import space.onepantsu.oneresident.service.AlarmReceiver;

public class PaymentActivity extends AppCompatActivity {

    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        linear = findViewById(R.id.residentLinear);
        checkPayment();
    }

    PaymentDBMS dbms = new PaymentDBMS(this);

    public static class PaymentInfo implements Serializable {
        public int currentID;
        public String currentStatus;
        public int currentDebt;
    }

    public void checkPayment(){
        SQLiteDatabase db = dbms.getReadableDatabase();

        String[] projection = {
                PaymentDB.PaymentTable._ID,  PaymentDB.PaymentTable.STATUS, PaymentDB.PaymentTable.DEBT};

        @SuppressLint("Recycle") Cursor cursor = db.query(
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

                DebtSearcher debtSearcher = new DebtSearcher(this);
                paymentInfo.currentDebt = debtSearcher.checkDebtByPaymentInfo(paymentInfo);

                addPaymentView(paymentInfo);

            }
            catch (Exception e){
                System.out.println("Ошибка при чтении строки");
            }
        }

    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables", "ResourceAsColor"})
    private void addPaymentView(PaymentInfo paymentInfo) {
        @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.custom_payment_layout, null);

        ResidentActivity.ResidentInfo newResident = getResidentInfo(paymentInfo.currentID);

        Button residentInfoBttn = view.findViewById(R.id.paymentInfo);
        StringBuilder residentInfoBttnTextBuilder = new StringBuilder();

        int maxLength = 15;

        if (newResident != null) {
            if (!newResident.currentStreet.equals("")) {
                residentInfoBttnTextBuilder.append("ул.").append(newResident.currentStreet);

                int currentLenght = residentInfoBttnTextBuilder.toString().length();

                if (currentLenght > maxLength) {
                    residentInfoBttnTextBuilder.delete(maxLength - 3, currentLenght - 1);
                    residentInfoBttnTextBuilder.append("...");
                }
                residentInfoBttnTextBuilder.append("\n");
            }
            if (!newResident.currentHouse.equals("")) {
                residentInfoBttnTextBuilder.append("д.").append(newResident.currentHouse);
            }
            if (newResident.currentFlat > 0) {
                residentInfoBttnTextBuilder.append(",\t кв.").append(newResident.currentFlat);
            }

            residentInfoBttnTextBuilder.append("\n");
            residentInfoBttnTextBuilder.append(newResident.currentPrice).append(" р");

            if (!newResident.currentDate.equals("")) {
                residentInfoBttnTextBuilder.append("\n");
                residentInfoBttnTextBuilder.append(newResident.currentDate);
            }
            residentInfoBttn.setText(residentInfoBttnTextBuilder.toString());

            residentInfoBttn.setOnClickListener(view1 -> {
                Intent intent = new Intent(PaymentActivity.this, ResidentInfoActivity.class);
                intent.putExtra(ResidentActivity.ResidentInfo.class.getSimpleName(), newResident);
                intent.putExtra("FROM", "PaymentActivity");
                startActivity(intent);
                closeActivity();
            });

            Button toPayButton = view.findViewById(R.id.toPayButton);
            toPayButton.setOnClickListener(v -> toPayDialog(this, paymentInfo));
            if (paymentInfo.currentDebt == 0) {
                toPayButton.setClickable(false);
                toPayButton.setBackground(getDrawable(R.drawable.background_paybutton_payed));
                toPayButton.setText("Оплачено");
            } else {
                toPayButton.setBackground(getDrawable(R.drawable.background_paybutton));
                toPayButton.setText("Оплатить");

            }

            Button changeDateButton = view.findViewById(R.id.changeDateButton);
            changeDateButton.setOnClickListener(v -> changeDate(paymentInfo));

            linear.addView(view);
        }
    }

    public void changeDate(PaymentInfo paymentInfo){
        ResidentActivity.ResidentInfo resident = new ResidentActivity.ResidentInfo();

        DBMS residentDBMS = new DBMS(this);
        SQLiteDatabase db = residentDBMS.getWritableDatabase();
        String[] projection = {
                DataBase.ResidentsTable._ID, DataBase.ResidentsTable.COLUMN_CITY,
                DataBase.ResidentsTable.COLUMN_STREET, DataBase.ResidentsTable.COLUMN_HOUSE,
                DataBase.ResidentsTable.COLUMN_LEVEL, DataBase.ResidentsTable.COLUMN_FLAT,
                DataBase.ResidentsTable.COLUMN_SURNAME, DataBase.ResidentsTable.COLUMN_NAME,
                DataBase.ResidentsTable.COLUMN_SECONDNAME, DataBase.ResidentsTable.COLUMN_PHONE,
                DataBase.ResidentsTable.COLUMN_DATE, DataBase.ResidentsTable.COLUMN_PERIOD,
                DataBase.ResidentsTable.COLUMN_PRICE, DataBase.ResidentsTable.COLUMN_COMMENT};

        @SuppressLint("Recycle") Cursor cursor = db.query(
                DataBase.ResidentsTable.TABLE_NAME,   // таблица
                projection,            // столбцы
                DataBase.ResidentsTable._ID + " = ?",                  // столбцы для условия WHERE
                new String[] {String.valueOf(paymentInfo.currentID)},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);

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
                resident.currentID = cursor.getInt(idColumnIndex);
                resident.currentCity = cursor.getString(cityColumnIndex);
                resident.currentStreet = cursor.getString(streetColumnIndex);
                resident.currentHouse = cursor.getString(houseColumnIndex);
                resident.currentLevel = cursor.getInt(levelColumnIndex);
                resident.currentFlat = cursor.getInt(flatColumnIndex);
                resident.currentSurname = cursor.getString(surnameColumnIndex);
                resident.currentName = cursor.getString(nameColumnIndex);
                resident.currentSecondname = cursor.getString(secondnameColumnIndex);
                resident.currentPhone = cursor.getString(phoneColumnIndex);
                resident.currentDate = cursor.getString(dateColumnIndex);
                resident.currentPeriod = cursor.getInt(periodColumnIndex);
                resident.currentPrice = cursor.getInt(priceColumnIndex);
                resident.currentComment = cursor.getString(commentColumnIndex);

            }
            catch (Exception e){
                System.out.println("Ошибка при чтении строки");
            }
        }
        dateDialog(PaymentActivity.this, resident);
    }

    public void dateDialog(Activity activity, ResidentActivity.ResidentInfo residentInfo) {
        final EditText edittext = new EditText(activity);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        edittext.setHint(residentInfo.currentDate);
        builder.setTitle("Изменение даты")
                .setMessage("Введите следующую дату оплаты (дд.мм.гггг):")
                .setView(edittext)
                .setPositiveButton("ОК", (dialog, id) -> saveNewDate(residentInfo, edittext.getText().toString()))
                .setNegativeButton("Отмена", (dialog, id) -> {});
        builder.create().show();
    }

    public void toPayDialog(Activity activity, PaymentInfo paymentInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Подтвержение")
                .setMessage("Вы уверены, что арендатор произвёл оплату?")
                .setPositiveButton("ОК", (dialog, id) -> wasPaid(paymentInfo))
                .setNegativeButton("Отмена", (dialog, id) -> {});
        builder.create().show();
    }


    private void saveNewDate(ResidentActivity.ResidentInfo residentInfo, String stringDate) {
        space.onepantsu.oneresident.residents.Error error = space.onepantsu.oneresident.residents.Error.OK;
        DBMS dbms = new DBMS(this);
        SQLiteDatabase db = dbms.getWritableDatabase();
        ContentValues newValues = new ContentValues();

        int day, month, year;
        try{
            if (stringDate.equals("")) {
                stringDate = residentInfo.currentDate;
                day = stringDate.charAt(0) * 10 + stringDate.charAt(1) - 528;
                month = stringDate.charAt(3) * 10 + stringDate.charAt(4) - 528;
                year = (int) stringDate.charAt(6) * 1000 + (int) stringDate.charAt(7) * 100
                        + (int) stringDate.charAt(8) * 10 + (int) stringDate.charAt(9) - 53328;
            }
            else if (stringDate.charAt(2) != '.' || stringDate.charAt(5) != '.'
                    || stringDate.length() != 10) {
                throw new IllegalArgumentException();
            } else {
                try {
                    System.out.println(stringDate.charAt(0) * 10);
                    day = stringDate.charAt(0) * 10 + stringDate.charAt(1) - 528;
                    month = stringDate.charAt(3) * 10 + stringDate.charAt(4) - 528;
                    year = (int) stringDate.charAt(6) * 1000 + (int) stringDate.charAt(7) * 100
                            + (int) stringDate.charAt(8) * 10 + (int) stringDate.charAt(9) - 53328;
                    if (day > 31 || day < 1 || month > 12 || month < 1) {
                        throw new Exception();
                    }

                } catch (Exception e) {
                    error = Error.WRONG_DATE_FORMAT;
                    throw new IllegalArgumentException();

                }
            }
            newValues.put(DataBase.ResidentsTable.COLUMN_DATE, stringDate);

            String where = DataBase.ResidentsTable._ID + "=" + residentInfo.currentID;

            try {
                db.update(DataBase.ResidentsTable.TABLE_NAME, newValues, where, null);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month - 1);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                SharedPreferences sharedPreferences;
                sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
                String stringHours = sharedPreferences.getString(ALARM_HOURS, "");
                int hours;
                if(stringHours.charAt(0) == '0'){
                    hours = (int) stringHours.charAt(1) - 528;
                }
                else{
                    hours = (int) stringHours.charAt(0) * 10 + (int) stringHours.charAt(1) - 528;
                }
                String stringMinutes = sharedPreferences.getString(ALARM_MINUTES, "");
                int minutes;
                if(stringHours.charAt(0) == '0'){
                    minutes = (int) stringMinutes.charAt(1) - 528;
                }
                else{
                    minutes = (int) stringMinutes.charAt(0) * 10 + (int) stringMinutes.charAt(1) - 528;
                }

                calendar.set(Calendar.HOUR_OF_DAY, hours);
                calendar.set(Calendar.MINUTE, minutes);

                startNewAlarm(calendar.getTimeInMillis());

                changedDateToHistoryDB(residentInfo.currentID);

                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);

                Toast.makeText(this, "Дата успешно изменена", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            InfoButton dialogButton = new InfoButton();
            DialogFrame warning = new DialogFrame("Ошибка при сохранении изменений!", "Проверьте корректность заполненных полей.", dialogButton);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            warning.show(transaction, "dialog");
        }
    }
        catch (IllegalArgumentException e){
        tryAgain(error);
    }
        catch (Exception e) {
        InfoButton dialogButton = new InfoButton();
        DialogFrame warning = new DialogFrame("Ошибка при сохранении изменений!", "Проверьте корректность заполненных полей.", dialogButton);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        warning.show(transaction, "dialog");
    }
    }

    private void tryAgain(Error error) {
        String errorMessage;
        switch(error){
            case WRONG_DATE: errorMessage = "Поле \"Дата\" должно быть заполнено!"; break;
            case WRONG_DATE_FORMAT: errorMessage = "Неверный формат поля \"Дата!\""; break;
            default: errorMessage = "Ошибка при сохранении изменений!"; break;
        }

        InfoButton dialogButton = new InfoButton();
        DialogFrame warning = new DialogFrame("Ошибка!",  errorMessage, dialogButton);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        warning.show(transaction, "dialog");
    }

    private ResidentActivity.ResidentInfo getResidentInfo(int id){

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

                return newResident;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void wasPaid(PaymentInfo paymentInfo){
        PaymentDBMS dbms = new PaymentDBMS(this);
        SQLiteDatabase db = dbms.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        DebtSearcher debtSearcher = new DebtSearcher(this);
        try {
            debtSearcher.changeDate(paymentInfo);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String selectQuery = "SELECT  * FROM " + PaymentDB.PaymentTable.TABLE_NAME +
                " WHERE " + PaymentDB.PaymentTable._ID + " = " + paymentInfo.currentID;
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToNext()) {
            @SuppressLint("Range") int debt = cursor.getInt(cursor.getColumnIndex(PaymentDB.PaymentTable.DEBT));

            if (debt > 0) {
                debt -= 1;
                try {
                    paymentInfo.currentDebt = debtSearcher.checkDebtByPaymentInfo(paymentInfo);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(debtSearcher.wasIncreased) {
                    debtToHistoryDB(paymentInfo.currentID);
                }
                Calendar newAlarm = debtSearcher.getNewAlarm();
                startNewAlarm(newAlarm.getTimeInMillis());

            }
            if (debt == 0) {
                newValues.put(PaymentDB.PaymentTable.STATUS, String.valueOf(PaymentStatus.PAID));
            }

            newValues.put(PaymentDB.PaymentTable.DEBT, String.valueOf(debt));

            String where = PaymentDB.PaymentTable._ID + "=" + paymentInfo.currentID;

            try {
                db.update(PaymentDB.PaymentTable.TABLE_NAME, newValues, where, null);

                payToHistoryDB(paymentInfo.currentID);

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
    }

    public void debtToHistoryDB(int id){

        DBMS residentDBMS = new DBMS(this);
        SQLiteDatabase residentDB = residentDBMS.getWritableDatabase();
        String[] projection = {DataBase.ResidentsTable._ID, DataBase.ResidentsTable.COLUMN_NAME,
                DataBase.ResidentsTable.COLUMN_SURNAME};
        @SuppressLint("Recycle") Cursor cursor = residentDB.query(
                DataBase.ResidentsTable.TABLE_NAME,   // таблица
                projection,            // столбцы
                DataBase.ResidentsTable._ID + " = ?",                  // столбцы для условия WHERE
                new String[] {String.valueOf(id)},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);
        int nameColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_NAME);
        int surnameColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_SURNAME);
        if(cursor.moveToNext()) {
            String name = cursor.getString(nameColumnIndex);
            String surname = cursor.getString(surnameColumnIndex);


            HistoryDBMS dbms = new HistoryDBMS(this);
            SQLiteDatabase db = dbms.getWritableDatabase();
            ContentValues values = new ContentValues();

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            String month;
            if (calendar.get(Calendar.MONTH) + 1 < 10) {
                month = "0" + (calendar.get(Calendar.MONTH) + 1);
            } else {
                month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
            }
            String day;
            if (calendar.get(Calendar.DATE) < 10) {
                day = "0" + calendar.get(Calendar.DATE);
            } else {
                day = String.valueOf(calendar.get(Calendar.DATE));
            }
            String date = day + "." + month + "." + calendar.get(Calendar.YEAR);

            values.put(HistoryDB.HistoryTable.DATE, date);
            values.put(HistoryDB.HistoryTable.RESIDENT_ID, id);
            values.put(HistoryDB.HistoryTable.RESIDENT_NAME, name);
            values.put(HistoryDB.HistoryTable.RESIDENT_SURNAME, surname);
            values.put(HistoryDB.HistoryTable.TYPE, String.valueOf(HistoryType.INCREASED_DEBT));

            db.insert(HistoryDB.HistoryTable.TABLE_NAME, null, values);
        }
    }

    public void payToHistoryDB(int id){

        DBMS residentDBMS = new DBMS(this);
        SQLiteDatabase residentDB = residentDBMS.getWritableDatabase();
        String[] projection = {DataBase.ResidentsTable._ID, DataBase.ResidentsTable.COLUMN_NAME,
                DataBase.ResidentsTable.COLUMN_SURNAME};
        @SuppressLint("Recycle") Cursor cursor = residentDB.query(
                DataBase.ResidentsTable.TABLE_NAME,   // таблица
                projection,            // столбцы
                DataBase.ResidentsTable._ID + " = ?",                  // столбцы для условия WHERE
                new String[] {String.valueOf(id)},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);
        int nameColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_NAME);
        int surnameColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_SURNAME);
        if(cursor.moveToNext()) {
            String name = cursor.getString(nameColumnIndex);
            String surname = cursor.getString(surnameColumnIndex);

            HistoryDBMS dbms = new HistoryDBMS(this);
            SQLiteDatabase db = dbms.getWritableDatabase();
            ContentValues values = new ContentValues();

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            String month;
            if (calendar.get(Calendar.MONTH) + 1 < 10) {
                month = "0" + (calendar.get(Calendar.MONTH) + 1);
            } else {
                month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
            }
            String day;
            if (calendar.get(Calendar.DATE) < 10) {
                day = "0" + calendar.get(Calendar.DATE);
            } else {
                day = String.valueOf(calendar.get(Calendar.DATE));
            }
            String date = day + "." + month + "." + calendar.get(Calendar.YEAR);

            values.put(HistoryDB.HistoryTable.DATE, date);
            values.put(HistoryDB.HistoryTable.RESIDENT_ID, id);
            values.put(HistoryDB.HistoryTable.RESIDENT_NAME, name);
            values.put(HistoryDB.HistoryTable.RESIDENT_SURNAME, surname);
            values.put(HistoryDB.HistoryTable.TYPE, String.valueOf(HistoryType.WAS_PAID));

            db.insert(HistoryDB.HistoryTable.TABLE_NAME, null, values);
        }
    }

    public void changedDateToHistoryDB(int id){

        DBMS residentDBMS = new DBMS(this);
        SQLiteDatabase residentDB = residentDBMS.getWritableDatabase();
        String[] projection = {DataBase.ResidentsTable._ID, DataBase.ResidentsTable.COLUMN_NAME,
                DataBase.ResidentsTable.COLUMN_SURNAME};
        @SuppressLint("Recycle") Cursor cursor = residentDB.query(
                DataBase.ResidentsTable.TABLE_NAME,   // таблица
                projection,            // столбцы
                DataBase.ResidentsTable._ID + " = ?",                  // столбцы для условия WHERE
                new String[] {String.valueOf(id)},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);
        int nameColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_NAME);
        int surnameColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_SURNAME);
        if(cursor.moveToNext()) {
            String name = cursor.getString(nameColumnIndex);
            String surname = cursor.getString(surnameColumnIndex);


            HistoryDBMS dbms = new HistoryDBMS(this);
            SQLiteDatabase db = dbms.getWritableDatabase();
            ContentValues values = new ContentValues();

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            String month;
            if (calendar.get(Calendar.MONTH) + 1 < 10) {
                month = "0" + (calendar.get(Calendar.MONTH) + 1);
            } else {
                month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
            }
            String day;
            if (calendar.get(Calendar.DATE) < 10) {
                day = "0" + calendar.get(Calendar.DATE);
            } else {
                day = String.valueOf(calendar.get(Calendar.DATE));
            }
            String date = day + "." + month + "." + calendar.get(Calendar.YEAR);

            values.put(HistoryDB.HistoryTable.DATE, date);
            values.put(HistoryDB.HistoryTable.RESIDENT_ID, id);
            values.put(HistoryDB.HistoryTable.RESIDENT_NAME, name);
            values.put(HistoryDB.HistoryTable.RESIDENT_SURNAME, surname);
            values.put(HistoryDB.HistoryTable.TYPE, String.valueOf(HistoryType.CHANGED_DATE));

            db.insert(HistoryDB.HistoryTable.TABLE_NAME, null, values);
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private void startNewAlarm(long startTime){

        Log.i("ALARM", "Start Alarm");
        AlarmManager alarmManager;

        PendingIntent alarmIntent;

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("text", "Узнайте, кто должен внести оплату!");
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), (int)startTime, intent, 0);

        alarmManager.set(AlarmManager.RTC_WAKEUP, startTime, alarmIntent);
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