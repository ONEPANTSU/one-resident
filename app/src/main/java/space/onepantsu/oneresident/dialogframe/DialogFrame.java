package space.onepantsu.oneresident.dialogframe;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DialogFrame extends DialogFragment {
    String title;
    String message;
    DialogButton button;

    public DialogFrame(String title, String message, DialogButton button){
        super();
        this.title = title;
        this.message = message;
        this.button = button;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(message)
                .setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Закрываем окно
                        button.setDialog(dialog);
                        button.funcOnClick();
                    }
                });
        if(button.isAccept){
            builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.cancel();
                }
            });
        }
        return builder.create();
    }
}