package space.onepantsu.oneresident.history.database;

import android.provider.BaseColumns;

public class HistoryDB {

    private HistoryDB() {}

    public static final class HistoryTable implements BaseColumns {
        public final static String TABLE_NAME = "history";

        public final static String _ID = BaseColumns._ID;

        public final static String DATE = "date";
        public final static String RESIDENT_ID = "residentID";
        public final static String RESIDENT_OBJECT = "object";
        public final static String RESIDENT_CITY = "city";
        public final static String RESIDENT_STREET = "street";
        public final static String RESIDENT_HOUSE = "house";
        public final static String RESIDENT_FLAT = "flat";
        public final static String RESIDENT_NAME = "residentName";
        public final static String RESIDENT_SURNAME = "residentSurname";
        public final static String TYPE = "type";

    }

}
