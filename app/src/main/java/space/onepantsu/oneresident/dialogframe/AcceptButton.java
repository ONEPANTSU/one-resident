package space.onepantsu.oneresident.dialogframe;

import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;

import space.onepantsu.oneresident.ChangeResidentActivity;


public class AcceptButton extends DialogButton {

    ChangeResidentActivity activity;
    public AcceptButton() {}

    public AcceptButton(ChangeResidentActivity activity) {
        super.isAccept = true;
        this.activity = activity;
    }

    public DialogInterface dialog;

    @Override
    public void setDialog(DialogInterface dialog) {
        this.dialog = dialog;
    }

    @Override
    public void funcOnClick() {

    }
}
