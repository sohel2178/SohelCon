package net.optimusbs.sohelcon.Utility;

import android.content.Context;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Sohel on 5/3/2016.
 */
public class MyDialog extends SweetAlertDialog {

    private Context context;
    private String title;
    private String message;

    public MyDialog(Context context,String title,String message) {
        super(context, SweetAlertDialog.ERROR_TYPE);
        this.context = context;
        this.title=title;
        this.message= message;
        this.setTitleText(title)
                .setContentText(message)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();

    }


}
