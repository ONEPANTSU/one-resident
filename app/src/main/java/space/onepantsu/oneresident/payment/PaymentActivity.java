package space.onepantsu.oneresident.payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

import space.onepantsu.oneresident.MainActivity;
import space.onepantsu.oneresident.R;
import space.onepantsu.oneresident.dialogframe.AcceptButton;
import space.onepantsu.oneresident.dialogframe.DeleteResidentButton;
import space.onepantsu.oneresident.dialogframe.DialogFrame;
import space.onepantsu.oneresident.payment.database.PaymentDB;
import space.onepantsu.oneresident.payment.database.PaymentDBMS;
import space.onepantsu.oneresident.residentManagement.AddActivity;
import space.onepantsu.oneresident.residentManagement.ResidentActivity;
import space.onepantsu.oneresident.residentManagement.ResidentInfoActivity;
import space.onepantsu.oneresident.residentManagement.database.DBMS;
import space.onepantsu.oneresident.residentManagement.database.DataBase;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        linear = (LinearLayout) findViewById(R.id.residentLinear);
        checkPayment();
    }

    LinearLayout linear;

    PaymentDBMS dbms = new PaymentDBMS(this);

    public static class PaymentInfo implements Serializable {
        public int currentID;
        public String currentStatus;
    }

    public void checkPayment(){
        SQLiteDatabase db = dbms.getReadableDatabase();

        String[] projection = {
                PaymentDB.PaymentTable._ID,  PaymentDB.PaymentTable.STATUS};

        Cursor cursor = db.query(
                PaymentDB.PaymentTable.TABLE_NAME,   // таблица
                projection,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        int idColumnIndex = cursor.getColumnIndex(PaymentDB.PaymentTable._ID);
        int statusColumnIndex = cursor.getColumnIndex(PaymentDB.PaymentTable.STATUS);

        while (cursor.moveToNext()) {
            try {
                PaymentInfo paymentInfo = new PaymentInfo();

                paymentInfo.currentID = cursor.getInt(idColumnIndex);
                paymentInfo.currentStatus = cursor.getString(statusColumnIndex);

                addPaymentView(paymentInfo);

            }
            catch (Exception e){
                System.out.println("Ошибка при чтении строки");
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private void addPaymentView(PaymentInfo paymentInfo){
        @SuppressLint("InflateParams") final View view = getLayoutInflater().inflate(R.layout.custom_payment_layout, null);

        TextView paymentText = (TextView) view.findViewById(R.id.paymentInfo);

        StringBuilder paymentTextBuilder = new StringBuilder();

        paymentTextBuilder.append(paymentInfo.currentID + "\t" + paymentInfo.currentStatus);

        paymentText.setText(paymentTextBuilder.toString());

        linear.addView(view);
    }


    public void goBack(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        closeActivity();
    }

    private void closeActivity() {
        this.finish();
    }
}