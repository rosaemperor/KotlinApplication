package net.lanlingdai.kotlinapplication.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.lanlingdai.kotlinapplication.R;

public class DialogUtils {

    public static void showExitDialog(Context context){
        final Dialog settingDialog = new Dialog(context );
        final LinearLayout layout= (LinearLayout) LayoutInflater.from(context).inflate(R.layout.dia_exit_layout,null);
        TextView cancelview=layout.findViewById(R.id.cancel_update_layout);
        cancelview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingDialog.dismiss();
            }
        });
        TextView updateView=layout.findViewById(R.id.update_view);
        updateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).finish();
            }
        });
        settingDialog.setContentView(layout);
        settingDialog.setCancelable(false);
        settingDialog.show();
    }
}
