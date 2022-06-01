package space.onepantsu.oneresident.dialogframe;

import space.onepantsu.oneresident.AddActivity;

public class BackButtonFromAdd extends AcceptButton{

    AddActivity activity;

    public BackButtonFromAdd(AddActivity activity){super(); this.activity = activity;}

    @Override
    public void funcOnClick() {
        super.funcOnClick();
        try {
            activity.back();
        }
        catch (Exception e){
            e.getCause();
        }
    }
}
