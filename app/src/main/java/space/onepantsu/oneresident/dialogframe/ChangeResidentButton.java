package space.onepantsu.oneresident.dialogframe;

import space.onepantsu.oneresident.ChangeResidentActivity;

public class ChangeResidentButton extends AcceptButton{

    public ChangeResidentButton(ChangeResidentActivity activity){
        super(activity);
    }

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
