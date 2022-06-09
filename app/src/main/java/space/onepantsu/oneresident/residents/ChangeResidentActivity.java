package space.onepantsu.oneresident.residents;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import space.onepantsu.oneresident.R;
import space.onepantsu.oneresident.residents.database.DBMS;
import space.onepantsu.oneresident.residents.database.DataBase;
import space.onepantsu.oneresident.dialogframe.AcceptButton;
import space.onepantsu.oneresident.dialogframe.BackButtonFromChange;
import space.onepantsu.oneresident.dialogframe.ChangeResidentButton;
import space.onepantsu.oneresident.dialogframe.DialogFrame;
import space.onepantsu.oneresident.dialogframe.InfoButton;

public class ChangeResidentActivity extends AppCompatActivity {
    boolean isDataError = false;
    ResidentActivity.ResidentInfo resident;
    EditText city, street, house, level, flat,
            surname, name, secondname, phone, date, period, price, comment;

    Map<String, String> previosValues = new HashMap<String, String>();

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
            default: errorMessage = "Ошибка при добавлении арендатора!"; break;
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
                        int day, month, year;
                        System.out.println(stringDate.charAt(0) * 10);
                        day = stringDate.charAt(0) * 10 + stringDate.charAt(1) - 528;
                        month = stringDate.charAt(3)* 10 + stringDate.charAt(4) - 528;
                        year = stringDate.charAt(6) * 1000 + stringDate.charAt(7) * 100
                                + stringDate.charAt(8) * 10 + stringDate.charAt(9) - 53328;
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
            if(period.getText().toString() != ""){
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
        Intent intent = new Intent(this, ResidentActivity.class);
        startActivity(intent);
        closeActivity();
    }

    private void closeActivity() {
        this.finish();
    }
}