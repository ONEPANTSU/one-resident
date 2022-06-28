package space.onepantsu.oneresident.dialogframe;

import android.view.View;

import space.onepantsu.oneresident.residents.ResidentActivity;

public class DeleteResidentButton extends AcceptButton{

    ResidentActivity activity;
    ResidentActivity.ResidentInfo resident;
    View view;

    public DeleteResidentButton(ResidentActivity activity, ResidentActivity.ResidentInfo newResident, View view) {
        super();
        this.activity = activity;
        this.resident = newResident;
        this.view = view;
    }

    @Override
    public void funcOnClick() {
        super.funcOnClick();
        activity.deleteToHistoryDB(resident.currentID, resident.currentName, resident.currentSurname);
        activity.deleteResident(view, resident);
    }
}
