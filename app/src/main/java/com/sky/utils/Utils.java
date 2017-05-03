package com.sky.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Utils {

    /**
     * 提示框
     * @param msg
     */
    public static void alert(Context context,String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("大华提示");
        builder.setMessage(msg);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
