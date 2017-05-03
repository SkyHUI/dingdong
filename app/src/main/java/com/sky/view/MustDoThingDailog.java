package com.sky.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * 常用对话框
 */
public class MustDoThingDailog {
    /**
     * 弹出对话框
     */
    public MustDoThingDailog(String title, String message, Context c, final NeedDoThing needDoThing) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);

        builder.setTitle(title);
        builder.setMessage(message);


        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(needDoThing != null){
                    needDoThing.needDoThings();
                }
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(needDoThing != null){
                    needDoThing.mustDoThings();
                }
            }
        });

        builder.setCancelable(false);

        builder.show();
    }


    public static abstract class NeedDoThing {
        public abstract void mustDoThings();

        public void needDoThings() {
        }
    }

}
