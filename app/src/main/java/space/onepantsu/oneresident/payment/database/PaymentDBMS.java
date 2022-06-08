package space.onepantsu.oneresident.payment.database;

import static space.onepantsu.oneresident.payment.database.PaymentDB.PaymentTable.TABLE_NAME;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PaymentDBMS extends SQLiteOpenHelper {

    public static final String LOG_TAG = space.onepantsu.oneresident.residentManagement.database.DBMS.class.getSimpleName();

    /**
     * Имя файла базы данных
     */
    private static final String DATABASE_NAME = "payment.db";

    /**
     * Версия базы данных. При изменении схемы увеличить на единицу
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Конструктор {@link space.onepantsu.oneresident.residentManagement.database.DBMS}.
     *
     * @param context Контекст приложения
     */
    public PaymentDBMS(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Вызывается при создании базы данных
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Строка для создания таблицы
        String SQL_CREATE_RESIDENTS_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + PaymentDB.PaymentTable._ID + " INTEGER PRIMARY KEY, "
                + PaymentDB.PaymentTable.STATUS + " INTEGER NOT NULL ) ";

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

