package space.onepantsu.oneresident.residents;

import static space.onepantsu.oneresident.settings.SettingsActivity.ALARM_HOURS;
import static space.onepantsu.oneresident.settings.SettingsActivity.ALARM_MINUTES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
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
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import space.onepantsu.oneresident.R;
import space.onepantsu.oneresident.payment.PaymentActivity;
import space.onepantsu.oneresident.payment.database.PaymentDB;
import space.onepantsu.oneresident.payment.database.PaymentDBMS;
import space.onepantsu.oneresident.payment.database.PaymentStatus;
import space.onepantsu.oneresident.residents.database.DBMS;
import space.onepantsu.oneresident.residents.database.DataBase;
import space.onepantsu.oneresident.dialogframe.AcceptButton;
import space.onepantsu.oneresident.dialogframe.BackButtonFromChange;
import space.onepantsu.oneresident.dialogframe.ChangeResidentButton;
import space.onepantsu.oneresident.dialogframe.DialogFrame;
import space.onepantsu.oneresident.dialogframe.InfoButton;
import space.onepantsu.oneresident.service.AlarmReceiver;

public class ChangeResidentActivity extends AppCompatActivity {
    boolean isDataError = false;
    ResidentActivity.ResidentInfo resident;
    EditText object, city, street, house, level, flat,
            surname, name, secondname, phone, date, period, price, comment;

    Map<String, String> previosValues = new HashMap<>();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_resident);
        Bundle arguments = getIntent().getExtras();

        object = findViewById(R.id.textObject);
        city = findViewById(R.id.textCity);
        street = findViewById(R.id.textStreet);
        house = findViewById(R.id.textHouse);
        level = findViewById(R.id.textLevel);
        flat = findViewById(R.id.textFlat);
        surname = findViewById(R.id.textSurname);
        name = findViewById(R.id.textName);
        secondname = findViewById(R.id.textSecondName);
        phone = findViewById(R.id.textPhone);
        date = findViewById(R.id.textDate);
        period = findViewById(R.id.textPeriod);
        price = findViewById(R.id.textPrice);
        comment = findViewById(R.id.textComment);

        if (arguments != null) {
            resident = (ResidentActivity.ResidentInfo)
                    arguments.getSerializable(ResidentActivity.ResidentInfo.class.getSimpleName());

            if(!resident.currentObject.equals("")){
                object.setText(resident.currentObject);
            }
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
        previosValues.put("object", object.getText().toString());
        previosValues.put("city", city.getText().toString());
        previosValues.put("street", street.getText().toString());
        previosValues.put("house", house.getText().toString());
        previosValues.put("level", level.getText().toString());
        previosValues.put("flat", flat.getText().toString());
        previosValues.put("surname", surname.getText().toString());
        previosValues.put("name", name.getText().toString());
        previosValues.put("secondname", secondname.getText().toString());
        previosValues.put("phone", phone.getText().toString());
        previosValues.put("date", date.getText().toString());
        previosValues.put("period", period.getText().toString());
        previosValues.put("price", price.getText().toString());
        previosValues.put("comment", comment.getText().toString());
    }

    public void acceptChanges(View view) {

        AcceptButton dialogButton = new ChangeResidentButton(this);
        DialogFrame warning = new DialogFrame("Вы уверены, что хотите сохранить изменения?",  "", dialogButton);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        warning.show(transaction, "dialog");

    }

    private void tryAgain(Error error) {
        String errorMessage;
        switch(error){
            case WRONG_STREET: errorMessage = "Поле \"Улица\" должно быть заполнено!"; break;
            case WRONG_HOUSE: errorMessage = "Поле \"Дом\" должно быть заполнено!"; break;
            case WRONG_SURNAME: errorMessage = "Поле \"Фамилия\" арендатора должно быть заполнено!"; break;
            case WRONG_NAME: errorMessage = "Поле \"Имя\" арендатора должно быть заполнено!"; break;
            case WRONG_DATE: errorMessage = "Поле \"Дата\" должно быть заполнено!"; break;
            case WRONG_DATE_FORMAT: errorMessage = "Неверный формат поля \"Дата!\""; break;
            case WRONG_PRICE: errorMessage = "Поле \"Стоимость арендной платы\" должно быть заполнено!"; break;
            default: errorMessage = "Ошибка при сохранении изменений арендатора!"; break;
        }

        InfoButton dialogButton = new InfoButton();
        DialogFrame warning = new DialogFrame("Ошибка!",  errorMessage, dialogButton);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        warning.show(transaction, "dialog");

        isDataError = true;
    }

    public void saveChanges(){
        Error error = Error.OK;
        DBMS dbms = new DBMS(this);
        SQLiteDatabase db = dbms.getWritableDatabase();
        ContentValues newValues = new ContentValues();

        int day, month, year;

        try {
            String stringDate = date.getText().toString();

            if(street.getText().toString().equals("")){
                error = Error.WRONG_STREET;
                throw new IllegalArgumentException();
            }
            else if(house.getText().toString().equals("")){
                error = Error.WRONG_HOUSE;
                throw new IllegalArgumentException();
            }
            else if(surname.getText().toString().equals("")){
                error = Error.WRONG_SURNAME;
                throw new IllegalArgumentException();
            }
            else if(name.getText().toString().equals("")){
                error = Error.WRONG_NAME;
                throw new IllegalArgumentException();
            }
            else if(stringDate.equals("")){
                error = Error.WRONG_DATE;
                throw new IllegalArgumentException();
            }
            else if (stringDate.charAt(2) != '.' || stringDate.charAt(5) != '.'
                    || stringDate.length() != 10){
                throw new IllegalArgumentException();
            }
            else{
                    try {
                        System.out.println(stringDate.charAt(0) * 10);
                        day = stringDate.charAt(0) * 10 + stringDate.charAt(1) - 528;
                        month = stringDate.charAt(3)* 10 + stringDate.charAt(4) - 528;
                        year = (int) stringDate.charAt(6) * 1000 + (int) stringDate.charAt(7) * 100
                                + (int) stringDate.charAt(8) * 10 + (int) stringDate.charAt(9) - 53328;
                        if (day > 31 || day < 1 || month > 12 || month < 1) {
                            throw new Exception();
                        }

                    }
                    catch (Exception e) {
                        error = Error.WRONG_DATE_FORMAT;
                        throw new IllegalArgumentException();
                    }
                if(price.getText().toString().equals("")){
                    error = Error.WRONG_PRICE;
                    throw new IllegalArgumentException();
                }
            }

            newValues.put(DataBase.ResidentsTable.COLUMN_OBJECT, object.getText().toString());
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
            if(!period.getText().toString().equals("")){
                newValues.put(DataBase.ResidentsTable.COLUMN_PERIOD, period.getText().toString());
            }
            else{
                newValues.put(DataBase.ResidentsTable.COLUMN_PERIOD, "30");
            }
            newValues.put(DataBase.ResidentsTable.COLUMN_PRICE, price.getText().toString());
            newValues.put(DataBase.ResidentsTable.COLUMN_COMMENT, comment.getText().toString());

            String where = DataBase.ResidentsTable._ID + "=" + resident.currentID;

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

                Toast.makeText(this, "Арендатор успешно изменён", Toast.LENGTH_SHORT).show();
                back();
            } catch (Exception e) {
                InfoButton dialogButton = new InfoButton();
                DialogFrame warning = new DialogFrame("Ошибка при сохранении изменений арендатора!", "Проверьте корректность заполненных полей.", dialogButton);
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
            DialogFrame warning = new DialogFrame("Ошибка при сохранении изменений арендатора!", "Проверьте корректность заполненных полей.", dialogButton);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            warning.show(transaction, "dialog");
        }
    }

    private boolean isChanged(){
        return !Objects.equals(previosValues.get("flat"), flat.getText().toString()) ||
                !Objects.equals(previosValues.get("city"), city.getText().toString()) ||
                !Objects.equals(previosValues.get("object"), city.getText().toString()) ||
                !Objects.equals(previosValues.get("period"), period.getText().toString()) ||
                !Objects.equals(previosValues.get("level"), level.getText().toString()) ||
                !Objects.equals(previosValues.get("price"), price.getText().toString()) ||
                !Objects.equals(previosValues.get("comment"), comment.getText().toString()) ||
                !Objects.equals(previosValues.get("date"), date.getText().toString()) ||
                !Objects.equals(previosValues.get("phone"), phone.getText().toString()) ||
                !Objects.equals(previosValues.get("surname"), surname.getText().toString()) ||
                !Objects.equals(previosValues.get("name"), name.getText().toString()) ||
                !Objects.equals(previosValues.get("secondname"), secondname.getText().toString()) ||
                !Objects.equals(previosValues.get("house"), house.getText().toString()) ||
                !Objects.equals(previosValues.get("street"), street.getText().toString());
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    private void startNewAlarm(long startTime) {
        PaymentDBMS dbms = new PaymentDBMS(this);
        SQLiteDatabase db = dbms.getWritableDatabase();
        ContentValues newValues = new ContentValues();
        String selectQuery = "SELECT  * FROM " + PaymentDB.PaymentTable.TABLE_NAME +
                " WHERE " + PaymentDB.PaymentTable._ID + " = " + resident.currentID;
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToNext()) {
            @SuppressLint("Range") int debt = cursor.getInt(cursor.getColumnIndex(PaymentDB.PaymentTable.DEBT));

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(startTime);

            Calendar current = Calendar.getInstance();
            current.setTimeInMillis(System.currentTimeMillis());

            if (calendar.compareTo(current) <= 0) {
                newValues.put(PaymentDB.PaymentTable.STATUS, String.valueOf(PaymentStatus.NOT_PAID));
                debt++;
                newValues.put(PaymentDB.PaymentTable.DEBT, debt);

                String where = PaymentDB.PaymentTable._ID + "=" + resident.currentID;

                try {
                    db.update(PaymentDB.PaymentTable.TABLE_NAME, newValues, where, null);
                } catch (Exception e) {
                    InfoButton dialogButton = new InfoButton();
                    DialogFrame warning = new DialogFrame("Ошибка при изменении даты", "", dialogButton);
                    FragmentManager manager = getSupportFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    warning.show(transaction, "dialog");
                }
            }
        }
        Log.i("ALARM", "Start Alarm");
        AlarmManager alarmManager;

        PendingIntent alarmIntent;

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        intent.putExtra("text", "Узнайте, кто должен внести оплату!");
        alarmIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) startTime, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, startTime, alarmIntent);
    }

    public void goBack(View view){
        if(isChanged()){
            AcceptButton dialogButton = new BackButtonFromChange(this);
            DialogFrame warning = new DialogFrame("Вы уверены, что хотите вернуться?",
                    "Все изменения будут отменены.", dialogButton);
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            warning.show(transaction, "dialog");
        }
        else{
            back();
        }
    }

    public void back(){
        Intent intent;
        if( getIntent().getExtras().getSerializable("FROM").equals("PaymentActivity")){
            intent = new Intent(this, PaymentActivity.class);
        }
        else{
            intent = new Intent(this, ResidentActivity.class);
        }
        startActivity(intent);
        closeActivity();
    }

    private void closeActivity() {
        this.finish();
    }
}