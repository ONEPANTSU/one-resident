package space.onepantsu.oneresident.dialogframe;

import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;

import space.onepantsu.oneresident.ChangeResidentActivity;


public class AcceptButton extends DialogButton {

    public AcceptButton() {
        super.isAccept = true;
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
