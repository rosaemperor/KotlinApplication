package net.lanlingdai.kotlinapplication.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.lanlingdai.kotlinapplication.R;

public class DialogUtils {

    public static void showExitDialog(Context context){
        final Dialog settingDialog = new Dialog(context );
        final LinearLayout layout= (LinearLayout) LayoutInflater.from(context).inflate(R.layout.dia_exit_layout,null);
        TextView cancelview=layout.findViewById(R.id.cancel_update_layout);
        ImageView imageView = layout.findViewById(R.id.loading);
        Animation animation = AnimationUtils.loadAnimation(context,R.anim.anima);
        LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
        animation.setInterpolator(interpolator);
        imageView.startAnimation(animation);
        imageView.setOnClickListener(v -> Toast.makeText(context,"再点我就停止运行",Toast.LENGTH_LONG).show());
        cancelview.setOnClickListener(v -> settingDialog.dismiss());
        TextView updateView=layout.findViewById(R.id.update_view);
        updateView.setOnClickListener(v -> ((Activity)context).finish());
        settingDialog.setContentView(layout);
        settingDialog.setCancelable(false);
        settingDialog.show();
    }
}
