package space.onepantsu.oneresident.payment;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;
import space.onepantsu.oneresident.residents.database.DBMS;
import space.onepantsu.oneresident.residents.database.DataBase;

public class DebtSearcher {

    private final Context context;

    private Calendar newAlarm;
    public boolean wasIncreased = false;

    public DebtSearcher(Context context){
        this.context = context;
    }

    public int checkDebtByPaymentInfo(PaymentActivity.PaymentInfo paymentInfo) throws ParseException {

        DBMS residentDBMS = new DBMS(context);
        SQLiteDatabase db = residentDBMS.getWritableDatabase();
        String[] projection = {DataBase.ResidentsTable._ID, DataBase.ResidentsTable.COLUMN_DATE,
                                DataBase.ResidentsTable.COLUMN_PERIOD};
        @SuppressLint("Recycle") Cursor cursor = db.query(
                DataBase.ResidentsTable.TABLE_NAME,   // таблица
                projection,            // столбцы
                DataBase.ResidentsTable._ID + " = ?",                  // столбцы для условия WHERE
                new String[] {String.valueOf(paymentInfo.currentID)},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);
        int dateColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_DATE);
        if(cursor.moveToNext()) {
            String residentsDate = cursor.getString(dateColumnIndex);
            DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTimeInMillis(System.currentTimeMillis());

            Calendar paymentDay = new GregorianCalendar();
            paymentDay.setTime(Objects.requireNonNull(format.parse(residentsDate)));

            if(currentCalendar.before(paymentDay)){
                paymentInfo.currentDebt = 0;
            }
            else{
                paymentInfo.currentDebt = 1;
            }

        }
        return paymentInfo.currentDebt;
    }

    public void changeDate(PaymentActivity.PaymentInfo paymentInfo) throws ParseException {
        DBMS residentDBMS = new DBMS(context);
        SQLiteDatabase db = residentDBMS.getWritableDatabase();
        String[] projection = {DataBase.ResidentsTable._ID, DataBase.ResidentsTable.COLUMN_DATE,
                DataBase.ResidentsTable.COLUMN_PERIOD};
        @SuppressLint("Recycle") Cursor cursor = db.query(
                DataBase.ResidentsTable.TABLE_NAME,   // таблица
                projection,            // столбцы
                DataBase.ResidentsTable._ID + " = ?",                  // столбцы для условия WHERE
                new String[] {String.valueOf(paymentInfo.currentID)},                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);
        int dateColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_DATE);
        int periodColumnIndex = cursor.getColumnIndex(DataBase.ResidentsTable.COLUMN_PERIOD);
        if(cursor.moveToNext()) {
            String residentsDate = cursor.getString(dateColumnIndex);
            int residentsPeriod = cursor.getInt(periodColumnIndex);
            DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
            Calendar paymentDay = new GregorianCalendar();
            paymentDay.setTime(Objects.requireNonNull(format.parse(residentsDate)));

            Calendar previousDay = Calendar.getInstance();
            previousDay.setTimeInMillis(paymentDay.getTimeInMillis());

            if(residentsPeriod < 30 || residentsPeriod > 31){
                paymentDay.roll(Calendar.DAY_OF_YEAR, residentsPeriod);
            }
            else{
                paymentDay.roll(Calendar.MONTH, 1);
            }

            if(previousDay.get(Calendar.MONTH) == Calendar.DECEMBER &&
                    paymentDay.get(Calendar.MONTH) == Calendar.JANUARY){
                paymentDay.roll(Calendar.YEAR, 1);
            }

            newAlarm = paymentDay;

            String newDate = dateParsing(paymentDay);

            ContentValues newValues = new ContentValues();
            newValues.put(DataBase.ResidentsTable.COLUMN_DATE, newDate);
            String where = DataBase.ResidentsTable._ID + "=" + paymentInfo.currentID;
            db.update(DataBase.ResidentsTable.TABLE_NAME, newValues, where, null);

        }
    }


    public Calendar getNewAlarm(){
        return newAlarm;
    }

    private String dateParsing(Calendar date){
        StringBuilder parsedBuilder = new StringBuilder();
        int day = date.get(Calendar.DATE);
        int month = date.get(Calendar.MONTH) + 1;
        int year = date.get(Calendar.YEAR);
        if(day < 10){
            parsedBuilder.append("0").append(day).append(".");
        }
        else{
            parsedBuilder.append(day).append(".");
        }
        if(month < 10){
            parsedBuilder.append("0").append(month).append(".");
        }
        else{
            parsedBuilder.append(month).append(".");
        }
        parsedBuilder.append(year);

        return parsedBuilder.toString();
    }

}
