package com.sky.heartbeat;

import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sky.bean.User;
import com.sky.utils.CommandOrder;
import com.sky.utils.RequestConstant;
import com.sky.utils.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名称：com.sky.heartbeat
 * 类描述：
 * 创建人：Sky
 * 创建时间：2016/11/8 21:18
 */
public class LoginActivity extends BaseActivity {


    private TextView tv_register;
    private Button bt_login;
    private AutoCompleteTextView at_username;
    private EditText et_password;
    private User user;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void findId() {
        tv_register = (TextView) findViewById(R.id.tv_register);
        bt_login = (Button) findViewById(R.id.bt_login);
        at_username = (AutoCompleteTextView) findViewById(R.id.at_username);
        et_password = (EditText) findViewById(R.id.login_et_password);
    }

    @Override
    protected void setListener() {
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = at_username.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(LoginActivity.this,"用户名不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                doLogin(username,password);

            }
        });
    }

    /**
     * 登录
     * @param username
     * @param password
     */
    private void doLogin(final String username, String password) {
        JSONObject json = new JSONObject();
        try {
            json.put("command", CommandOrder.COMMAND_LOGIN);
            json.put("username",username);
            json.put("password",password);
            requestServer(RequestConstant.REQUEST_LOGIN, json, new DataCallback() {

                @Override
                public void onSuccess(String t) {
                    RequestConstant.USER_ID = Integer.parseInt(t);
                    Log.i("TAG","USER_ID:"+t);
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("username",username);
                    startActivity(intent);

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void initData() {
        initTvRegister("还没有账号？去注册");
    }

    @Override
    protected void processLogic() {
        if (SPUtil.isRegisterd(this)) {
            user = SPUtil.getUser(this);
            at_username.setText(user.getUsername());
            et_password.setText(user.getPassword());
        }
    }

    /**
     * 初始化去注册事件
     *
     * @param str
     */
    private void initTvRegister(String str) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(str);
        spannable.setSpan(new ForegroundColorSpan(Color.RED), 6, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_register.setMovementMethod(LinkMovementMethod.getInstance());
        spannable.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //处理点击事件
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                //此方法会在父类方法中设置了划线
                super.updateDrawState(ds);
            }
        }, 6, 9, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_register.setText(spannable);
    }

}
