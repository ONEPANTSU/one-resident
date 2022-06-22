package space.onepantsu.oneresident.dialogframe;
import android.app.AlertDialog;
import android.app.Dialog;
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
                .setPositiveButton("Ок", (dialog, id) -> {
                    // Закрываем окно
                    button.setDialog(dialog);
                    button.funcOnClick();
                });
        if(button.isAccept){
            builder.setNegativeButton("Отмена", (dialog, i) -> dialog.cancel());
        }
        return builder.create();
    }
}