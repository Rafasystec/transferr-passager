package br.com.transferr.dialogs;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

/**
 * Created by idoctor on 31/10/2018.
 */

public class ProgressLoadDialog extends ProgressDialog {
    public ProgressLoadDialog(Context context) {
        super(context);
    }

    public ProgressLoadDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void dismiss() {
        new Handler().postDelayed(
                ()->{
                    super.dismiss();
                },1000);

    }
}
