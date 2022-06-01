package space.onepantsu.oneresident.residentManagement.database;

import android.provider.BaseColumns;

public class DataBase {

    private DataBase(){}

    public static final class ResidentsTable implements BaseColumns {
        public final static String TABLE_NAME = "residents";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_CITY = "city";
        public final static String COLUMN_STREET = "street";
        public final static String COLUMN_HOUSE = "house";
        public final static String COLUMN_LEVEL = "level";
        public final static String COLUMN_FLAT = "flat";
        public final static String COLUMN_SURNAME = "surname";
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_SECONDNAME = "secondname";
        public final static String COLUMN_PHONE = "phone";
        public final static String COLUMN_DATE = "date";
        public final static String COLUMN_PERIOD = "period";
        public final static String COLUMN_PRICE = "price";
        public final static String COLUMN_COMMENT = "comment";


    }


}
