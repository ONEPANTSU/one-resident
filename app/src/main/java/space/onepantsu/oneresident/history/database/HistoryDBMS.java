package space.onepantsu.oneresident.history.database;

import static space.onepantsu.oneresident.history.database.HistoryDB.HistoryTable.TABLE_NAME;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import space.onepantsu.oneresident.payment.database.PaymentDB;

public class HistoryDBMS extends SQLiteOpenHelper {

    /**
     * Имя файла базы данных
     */
    private static final String DATABASE_NAME = "history.db";

    /**
     * Версия базы данных. При изменении схемы увеличить на единицу
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Конструктор {@link space.onepantsu.oneresident.residents.database.DBMS}.
     *
     * @param context Контекст приложения
     */
    public HistoryDBMS(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Вызывается при создании базы данных
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Строка для создания таблицы
        String SQL_CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + HistoryDB.HistoryTable._ID + " INTEGER PRIMARY KEY, "
                + HistoryDB.HistoryTable.DATA+ " DATE NOT NULL, "
                + HistoryDB.HistoryTable.RESIDENT_ID + " INTEGER NOT NULL, "
                + HistoryDB.HistoryTable.TYPE + " INTEGER NOT NULL) ";

        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_HISTORY_TABLE);
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
