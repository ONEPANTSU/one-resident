package space.onepantsu.oneresident.payment.database;

import android.provider.BaseColumns;

public class PaymentDB {

    private PaymentDB() {}

    public static final class PaymentTable implements BaseColumns {
        public final static String TABLE_NAME = "payment";

        public final static String _ID = BaseColumns._ID;
        public final static String STATUS = "status";

    }
}
