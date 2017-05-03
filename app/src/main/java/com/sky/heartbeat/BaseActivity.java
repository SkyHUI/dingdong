package com.sky.heartbeat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;
import com.sky.bean.Result;
import com.sky.utils.ActivityCollector;
import com.sky.utils.GsonUtils;
import com.sky.view.MustDoThingDailog;
import com.sky.view.MyProgressDialog;

import org.json.JSONObject;


public abstract class BaseActivity extends AppCompatActivity{

	private MyProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCollector.addActivity(this);
		//android4.4之后才支持沉浸式导航栏
		//设置导航栏透明
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		initView();
	}
	private void initView() {
		setContentView();
		findId();
		initData();
		setListener();
		processLogic();
	}

	/**
	 * 设置布局
	 */
	protected abstract void setContentView();

	/**
	 * 初始化id
	 */
	protected abstract void findId();

	/**
	 * 设置监听
	 */
	protected abstract void setListener();

	/**
	 * 初始化数据
	 */
	protected abstract void initData();

	/**
	 * 逻辑处理
	 */
	protected abstract void processLogic();

	/**
	 * 从服务器获取数据
	 * @param url 请求地址
	 * @param jsonObject	请求参数
	 * @param callback	请求回调
     */
	protected void requestServer(String url, JSONObject jsonObject, final DataCallback callback) {

		HttpParams params = new HttpParams();
		if(jsonObject != null ){
			params.put("params", jsonObject.toString());
		}
		RxVolley.post(url, params, new HttpCallback() {
			@Override
			public void onPreStart() {
				super.onPreStart();
				showProgressDialog("加载中...");
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				Result result = new GsonUtils().toBean(t,Result.class);
				if(result.getSuccess() == Result.RESULT_OK){
					callback.onSuccess(result.getMessage());
				}else if(result.getSuccess() == Result.RESULT_FAIL){
					alert(result.getMessage(),null);
				}else if(result.getSuccess() == Result.RESULT_TOKEN_FAIL){
					alert(result.getMessage(), new MustDoThingDailog.NeedDoThing() {
						@Override
						public void mustDoThings() {
							ActivityCollector.finishAll();
							startActivity(new Intent(BaseActivity.this,LoginActivity.class));
						}

						@Override
						public void needDoThings() {
							ActivityCollector.finishAll();
							startActivity(new Intent(BaseActivity.this,LoginActivity.class));
						}
					});
				}
			}

			@Override
			public void onFailure(VolleyError error) {
				super.onFailure(error);
				alert("请求失败！" + error.getMessage(),null);
//				callback.onFailure(error);
			}

			@Override
			public void onFinish() {
				super.onFinish();
				closeProgressDialog();
			}
		});


	}

	public static interface DataCallback{
//		void onFailure(VolleyError error);
		void onSuccess(String t);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	/**
	 * 显示提示框
	 */
	protected void showProgressDialog(String msg) {
		if(progressDialog == null){
			progressDialog = new MyProgressDialog(this,msg);
		}
		progressDialog.show();
	}

	/**
	 * 关闭提示框
	 */
	protected void closeProgressDialog() {
		if (this.progressDialog != null)
			this.progressDialog.dismiss();
	}

	protected void alert(String message , final MustDoThingDailog.NeedDoThing mustdo){
		new MustDoThingDailog("叮咚",message,this,mustdo);
	}
}
