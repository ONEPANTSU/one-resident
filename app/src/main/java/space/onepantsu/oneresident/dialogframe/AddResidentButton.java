package space.onepantsu.oneresident.dialogframe;

import android.util.Log;

import space.onepantsu.oneresident.residents.AddActivity;

public class AddResidentButton extends AcceptButton{

    AddActivity activity;

    public AddResidentButton(AddActivity activity){super(); this.activity = activity;}

    @Override
    public void funcOnClick() {
        super.funcOnClick();
        try {
            activity.addResident();
        }
        catch (Exception e){
            Log.e("ADD_RESIDENT_BUTTON", "ERROR");
            e.getCause();
        }
    }
}
