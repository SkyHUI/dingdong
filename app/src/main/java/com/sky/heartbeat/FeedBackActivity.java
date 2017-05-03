package com.sky.heartbeat;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sky.utils.CommandOrder;
import com.sky.utils.RequestConstant;
import com.sky.view.MustDoThingDailog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 项目名称：com.sky.heartbeat
 * 类描述：问题反馈
 * 创建人：Sky
 * 创建时间：2017/5/2 17:33
 */
public class FeedBackActivity extends BaseActivity {

    private EditText et;

    private Button bt;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_feed);
    }

    @Override
    protected void findId() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.feed_toolbar);
        et = (EditText) findViewById(R.id.feed_et);
        bt = (Button) findViewById(R.id.feed_btn);
        toolbar.setTitle(getResources().getString(R.string.feed_back));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void setListener() {
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String des = et.getText().toString().trim();
                if(TextUtils.isEmpty(des)){
                    alert("请如输入您宝贵的建议!",null);
                }else{
                    JSONObject json = new JSONObject();
                    try {
                        json.put("command", CommandOrder.COMMAND_FEED_BACK);
                        json.put("user_id",RequestConstant.USER_ID);
                        json.put("des",des);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    requestServer(RequestConstant.REQUEST_LOGIN, json, new DataCallback() {
                        @Override
                        public void onSuccess(String t) {
                            alert(t, new MustDoThingDailog.NeedDoThing() {
                                @Override
                                public void mustDoThings() {
                                    finish();
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void processLogic() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
