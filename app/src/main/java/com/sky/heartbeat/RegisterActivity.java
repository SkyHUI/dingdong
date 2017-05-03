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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sky.bean.User;
import com.sky.hkg.DeviceListActivity;
import com.sky.utils.CommandOrder;
import com.sky.utils.RequestConstant;
import com.sky.utils.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名称：com.sky.heartbeat
 * 类描述：
 * 创建人：Sky
 * 创建时间：2016/11/8 21:12
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{

    private String TAG = "RegisterActivity";

    private TextView tv_login;
    private EditText et_username;
    private EditText et_password;
    private EditText et_password_again;
    private EditText et_age;
    private EditText et_sbp,et_dbp;
    private RadioGroup rg_sex;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private Button bt_register;

    private String username = null;
    private String password = null;
    private String passwordAgain = null;
    private int age = 0;
    private int dbp = 0;    //收缩压
    private int sbp = 0;    //舒张压
    /**
     * 默认值 = 0
     * 0 男性
     * 1 女性
     */
    private int sex = 0;

    private User user = null;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void findId() {
        tv_login = (TextView) findViewById(R.id.tv_login);
        et_username = (EditText) findViewById(R.id.et_register_username);
        et_password = (EditText) findViewById(R.id.et_register_password);
        et_password_again = (EditText) findViewById(R.id.et_register_again);
        et_age = (EditText) findViewById(R.id.et_age);
        et_sbp = (EditText) findViewById(R.id.et_sbp);
        et_dbp = (EditText) findViewById(R.id.et_dbp);
        rg_sex = (RadioGroup) findViewById(R.id.radio_sex);
        rb_male = (RadioButton) findViewById(R.id.sex_male);
        rb_female = (RadioButton) findViewById(R.id.sex_female);
        bt_register = (Button) findViewById(R.id.bt_register);
    }

    @Override
    protected void setListener() {
        bt_register.setOnClickListener(this);
        rg_sex.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData() {
        initLogin("已有账号，去登录");
    }

    @Override
    protected void processLogic() {
        if(SPUtil.isRegisterd(this)){
            user = SPUtil.getUser(this);
            username = user.getUsername();
            password = user.getPassword();
            passwordAgain = user.getPassword();
            age = user.getAge();
            sex = user.getSex();
            sbp = user.getSbp();
            dbp = user.getDbp();
            et_username.setText(username);
            et_password.setText(password);
            et_password_again.setText(password);
            et_age.setText(age+"");
            et_sbp.setText(sbp+"");
            et_dbp.setText(dbp+"");
            if(sex == 0){
                rb_male.setChecked(true);
            }else{
                rb_female.setChecked(true);
            }
        }
    }

    /**
     *  初始化去登录事件
     * @param str
     */
    private void initLogin(String str){
        SpannableStringBuilder spannable = new SpannableStringBuilder(str);
        spannable.setSpan(new ForegroundColorSpan(Color.RED),5,8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_login.setMovementMethod(LinkMovementMethod.getInstance());
        spannable.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //处理点击事件
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                //此方法会在父类方法中设置了划线
                super.updateDrawState(ds);
            }
        },5,8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_login.setText(spannable);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.bt_register){
            username = et_username.getText().toString().trim();
            password = et_password.getText().toString().trim();
            passwordAgain = et_password_again.getText().toString().trim();
            String AGE = et_age.getText().toString();
            String SBP = et_sbp.getText().toString();
            String DBP = et_dbp.getText().toString();
            if(TextUtils.isEmpty(username)){
                Toast.makeText(this,"用户名不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(password)){
                Toast.makeText(this,"密码不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(passwordAgain)){
                Toast.makeText(this,"确认密码不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!password.equals(passwordAgain)){
                Toast.makeText(this,"两次输入密码不一致，请重新输入",Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(AGE)){
                Toast.makeText(this,"年龄不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(SBP)){
                Toast.makeText(this,"收缩压不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            if(TextUtils.isEmpty(DBP)){
                Toast.makeText(this,"舒张压不能为空",Toast.LENGTH_SHORT).show();
                return;
            }
            age = Integer.parseInt(AGE);
            sbp = Integer.parseInt(SBP);
            dbp = Integer.parseInt(DBP);
            User user = new User(username,password,age,sex,sbp,dbp);
            doRegister(user);
        }
    }

    /**
     * 用户注册
     * @param user
     */
    private void doRegister(final User user) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("command", CommandOrder.COMMAND_REGISTER);
            jsonObject.put("username",user.getUsername());
            jsonObject.put("password",user.getPassword());
            jsonObject.put("age",user.getAge());
            jsonObject.put("sex",user.getSex());
            jsonObject.put("sbp",user.getSbp());
            jsonObject.put("dbp",user.getDbp());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestServer(RequestConstant.REQUEST_LOGIN,jsonObject, new DataCallback() {

            @Override
            public void onSuccess(String t) {
                SPUtil.setRegisterd(RegisterActivity.this,user);
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.sex_male:
                sex = 0;
                break;
            case R.id.sex_female:
                sex = 1;
                break;
            default:
                break;
        }
    }
}
