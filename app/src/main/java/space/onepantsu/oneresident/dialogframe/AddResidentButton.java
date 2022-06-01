package space.onepantsu.oneresident.dialogframe;

import space.onepantsu.oneresident.residentManagement.AddActivity;

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
            e.getCause();
        }
    }
}
