package space.onepantsu.oneresident.dialogframe;
import space.onepantsu.oneresident.residents.ChangeResidentActivity;

public class BackButtonFromChange extends AcceptButton{

    ChangeResidentActivity activity;

    public BackButtonFromChange(ChangeResidentActivity activity){super(); this.activity = activity;}

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
