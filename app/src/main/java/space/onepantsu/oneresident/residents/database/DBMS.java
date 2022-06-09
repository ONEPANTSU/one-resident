package space.onepantsu.oneresident.residents.database;

import static space.onepantsu.oneresident.residents.database.DataBase.ResidentsTable.TABLE_NAME;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBMS extends SQLiteOpenHelper {

    public static final String LOG_TAG = DBMS.class.getSimpleName();

    /**
     * Имя файла базы данных
     */
    private static final String DATABASE_NAME = "residents.db";

    /**
     * Версия базы данных. При изменении схемы увеличить на единицу
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Конструктор {@link DBMS}.
     *
     * @param context Контекст приложения
     */
    public DBMS(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Вызывается при создании базы данных
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Строка для создания таблицы
        String SQL_CREATE_RESIDENTS_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + DataBase.ResidentsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DataBase.ResidentsTable.COLUMN_CITY + " TEXT, "
                + DataBase.ResidentsTable.COLUMN_STREET + " TEXT NOT NULL, "
                + DataBase.ResidentsTable.COLUMN_HOUSE + " TEXT NOT NULL, "
                + DataBase.ResidentsTable.COLUMN_LEVEL + " INTEGER, "
                + DataBase.ResidentsTable.COLUMN_FLAT + " INTEGER, "
                + DataBase.ResidentsTable.COLUMN_SURNAME + " TEXT NOT NULL, "
                + DataBase.ResidentsTable.COLUMN_NAME + " TEXT NOT NULL, "
                + DataBase.ResidentsTable.COLUMN_SECONDNAME + " TEXT , "
                + DataBase.ResidentsTable.COLUMN_PHONE + " TEXT , "
                + DataBase.ResidentsTable.COLUMN_DATE + " DATE NOT NULL , "
                + DataBase.ResidentsTable.COLUMN_PERIOD + " INTEGER NOT NULL , "
                + DataBase.ResidentsTable.COLUMN_PRICE + " INTEGER NOT NULL , "
                + DataBase.ResidentsTable.COLUMN_COMMENT + " TEXT) ";

        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_RESIDENTS_TABLE);
    }

    /**
     * Вызывается при обновлении схемы базы данных
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Запишем в журнал
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Создаём новую таблицу
        onCreate(db);
    }
}

