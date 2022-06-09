package space.onepantsu.oneresident.dialogframe;

import space.onepantsu.oneresident.residents.ChangeResidentActivity;

public class ChangeResidentButton extends AcceptButton{

    ChangeResidentActivity activity;

    public ChangeResidentButton(ChangeResidentActivity activity){super(); this.activity = activity;}

    @Override
    public void funcOnClick() {
        super.funcOnClick();
        try {
            activity.saveChanges();
        }
        catch (Exception e){
            e.getCause();
        }
    }
}
