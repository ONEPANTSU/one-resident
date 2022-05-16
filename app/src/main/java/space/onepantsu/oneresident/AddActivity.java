package space.onepantsu.oneresident;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import space.onepantsu.oneresident.database.DBMS;
import space.onepantsu.oneresident.database.DataBase;

public class AddActivity extends AppCompatActivity {

    private boolean isDataError;

    private EditText addCity;
    private EditText addStreet;
    private EditText addHouse;
    private EditText addLevel;
    private EditText addFlat;
    private EditText addSurname;
    private EditText addName;
    private EditText addSecondName;
    private EditText addPhone;
    private EditText addDate;
    private EditText addPeriod;
    private EditText addPrice;
    private EditText addComment;

    private String city = "";
    private String street = "";
    private String house = "";
    private Integer level = null;
    private Integer flat = null;
    private String surname = "";
    private String name = "";
    private String secondName = "";
    private String phone = "";
    private Date date;
    private Integer period = 30;
    private Integer price = null;
    private String comment = "";

    private enum Error{
        WRONG_DATE_FORMAT, WRONG_NAME, WRONG_SURNAME,
        WRONG_STREET, WRONG_HOUSE, WRONG_PRICE, WRONG_DATE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        addCity = (EditText) findViewById(R.id.editTextAddCity);
        addStreet = (EditText) findViewById(R.id.editTextAddStreet);
        addHouse = (EditText) findViewById(R.id.editTextAddHouse);
        addLevel = (EditText) findViewById(R.id.editTextAddLevel);
        addFlat = (EditText) findViewById(R.id.editTextAddFlat);
        addSurname = (EditText) findViewById(R.id.editTextAddSurname);
        addName = (EditText) findViewById(R.id.editTextAddName);
        addSecondName = (EditText) findViewById(R.id.editTextAddSecondName);
        addPhone = (EditText) findViewById(R.id.editTextAddPhone);
        addDate = (EditText) findViewById(R.id.editTextAddDate);
        addPeriod =(EditText) findViewById(R.id.editTextAddPeriod);
        addPrice = (EditText) findViewById(R.id.editTextAddPrice);
        addComment = (EditText) findViewById(R.id.editTextAddComment);

    }

    private void tryAgain(Error error){
        String errorMessage;
        switch(error){
            case WRONG_STREET: errorMessage = "Поле \"Улица\" должно быть заполнено!"; break;
            case WRONG_HOUSE: errorMessage = "Поле \"Дом\" должно быть заполнено!"; break;
            case WRONG_SURNAME: errorMessage = "Поле \"Фамилия\" арендатора должно быть заполнено!"; break;
            case WRONG_NAME: errorMessage = "Поле \"Имя\" арендатора должно быть заполнено!"; break;
            case WRONG_DATE: errorMessage = "Поле \"Дата\" должно быть заполнено!"; break;
            case WRONG_DATE_FORMAT: errorMessage = "Неверный формат поля \"Дата!\""; break;
            case WRONG_PRICE: errorMessage = "Поле \"Стоимость арендной платы\" должно быть заполнено!"; break;
            default: errorMessage = "Ошибка!"; break;
           }
        DialogFrame warning = new DialogFrame(errorMessage);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        warning.show(transaction, "dialog");

        isDataError = true;
    }

    public void addResident(View view){

        isDataError = false;

        try {
            city = addCity.getText().toString();
        }
        catch (Exception ignored){}
        try {
            street = addStreet.getText().toString();
             if (street.equals("")){
                throw new Exception();
            }
            try {
                house = addHouse.getText().toString();
                if (house.equals("")){
                    throw new Exception();
                }
                try {
                    level = Integer.parseInt(addLevel.getText().toString());
                }
                catch (Exception ignored){}
                try {
                    flat = Integer.parseInt(addFlat.getText().toString());
                }
                catch (Exception ignored){}
                try {
                    surname = addSurname.getText().toString();
                    if (surname.equals("")){
                        throw new Exception();
                    }
                    try {
                        name = addName.getText().toString();
                        if (name.equals("")){
                            throw new Exception();
                        }
                        try {
                            secondName = addSecondName.getText().toString();
                        }
                        catch (Exception ignored){}
                        try {
                            phone = addPhone.getText().toString();
                        }
                        catch (Exception ignored){}
                        DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
                        try{
                            String stringDate = addDate.getText().toString();
                            if(stringDate.equals("")){
                                tryAgain(Error.WRONG_DATE);
                            }
                            else if (stringDate.charAt(2) != '.' || stringDate.charAt(5) != '.'
                                    || stringDate.length() != 10){
                                throw new Exception();
                            }
                            else {
                                date = format.parse(stringDate);

                                try{
                                    period = Integer.parseInt(addPrice.getText().toString());
                                    if (period <= 0){
                                        throw new Exception();
                                    }
                                }
                                catch (Exception e){
                                    period = 30;
                                }
                                try {
                                    price = Integer.parseInt(addPrice.getText().toString());
                                }
                                catch (Exception e){
                                    tryAgain(Error.WRONG_PRICE);
                                }
                                try {
                                    comment = addComment.getText().toString();
                                }
                                catch (Exception ignored){}
                            }
                        }
                        catch (Exception e){
                            tryAgain(Error.WRONG_DATE_FORMAT);
                        }
                    }
                    catch (Exception e){
                        tryAgain(Error.WRONG_NAME);
                    }

                }
                catch (Exception e){
                    tryAgain(Error.WRONG_SURNAME);
                }

            }
            catch(Exception e){
                tryAgain(Error.WRONG_HOUSE);
            }
        }
        catch (Exception e){
            tryAgain(Error.WRONG_STREET);
        }

        if(!isDataError){
            addToDB();
        }
    }

    public void addToDB(){
        System.out.println("Добавление в Базу Данных...");
        DBMS dbms = new DBMS(this);
        SQLiteDatabase db = dbms.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBase.ResidentsTable.COLUMN_CITY, city);
        values.put(DataBase.ResidentsTable.COLUMN_STREET, street);
        values.put(DataBase.ResidentsTable.COLUMN_HOUSE, house);
        values.put(DataBase.ResidentsTable.COLUMN_LEVEL, level);
        values.put(DataBase.ResidentsTable.COLUMN_FLAT, flat);
        values.put(DataBase.ResidentsTable.COLUMN_SURNAME, surname);
        values.put(DataBase.ResidentsTable.COLUMN_NAME, name);
        values.put(DataBase.ResidentsTable.COLUMN_SECONDNAME, secondName);
        values.put(DataBase.ResidentsTable.COLUMN_PHONE, phone);
        values.put(DataBase.ResidentsTable.COLUMN_DATE, date.toString());
        values.put(DataBase.ResidentsTable.COLUMN_PERIOD, period);
        values.put(DataBase.ResidentsTable.COLUMN_PRICE, price);
        values.put(DataBase.ResidentsTable.COLUMN_COMMENT, comment);

        long newRowId = db.insert(DataBase.ResidentsTable.TABLE_NAME, null, values);

        if (newRowId == -1) {
            // Если ID  -1, значит произошла ошибка
            Toast.makeText(this, "Ошибка при добавлении арендатора", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Добавление произошло успешно!\nАрендатор заведён под номером: " + newRowId, Toast.LENGTH_SHORT).show();
        }

    }

    public void goBack(View view){
        Intent intent = new Intent(this, ResidentActivity.class);
        startActivity(intent);
        closeActivity();
    }

    private void closeActivity() {
        this.finish();
    }

}