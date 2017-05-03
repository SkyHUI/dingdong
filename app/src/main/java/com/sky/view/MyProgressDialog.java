package com.sky.view;

import android.app.ProgressDialog;
import android.content.Context;
/**
 * 项目名称：com.sky.view
 * 类描述：
 * 创建人：Sky
 * 创建时间：2016/12/12 17:52
 */
public class MyProgressDialog {

    private final ProgressDialog progressDialog;
    private final String message;
    private final Context context;

    public MyProgressDialog(Context context,String message) {
        this.message = message;
        this.context = context;

        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(message);
        progressDialog.setIndeterminate(true);
//        progressDialog.setIcon(android.support.v7.appcompat.R.drawable.notification_template_icon_bg);
        progressDialog.setCancelable(false);
    }

    public void show() {
        if (progressDialog == null) {
            //对话框不存在时候，应该创建对话框
            new MyProgressDialog(context,message);
        }
        progressDialog.show();
    }

    public void dismiss() {
        if (null != progressDialog) {
            progressDialog.dismiss();
        }
    }

    public boolean isShowing() {
        return progressDialog.isShowing();
    }
}
