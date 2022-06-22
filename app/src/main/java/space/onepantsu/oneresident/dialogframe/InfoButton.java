package space.onepantsu.oneresident.dialogframe;

import android.content.DialogInterface;

public class InfoButton extends DialogButton {
    public DialogInterface dialog;

    @Override
    public void setDialog(DialogInterface dialog) {
        this.dialog = dialog;
    }

    @Override
    public void funcOnClick() {
        dialog.cancel();
    }
}
