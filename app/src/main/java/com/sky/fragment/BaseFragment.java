package com.sky.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.http.VolleyError;
import com.sky.bean.Result;
import com.sky.heartbeat.BaseActivity;
import com.sky.heartbeat.LoginActivity;
import com.sky.utils.ActivityCollector;
import com.sky.utils.GsonUtils;
import com.sky.view.MustDoThingDailog;
import com.sky.view.MyProgressDialog;

import org.json.JSONObject;

/**
 * 项目名称：com.sky.fragment
 * 类描述：Fragment基类
 * 创建人：Sky
 * 创建时间：2017/3/22 14:51
 *
 * 生命周期:
 * 创建：onAttach --> onCreate --> onCreateView --> onActivityCreated --> onStart --> onResume
 * 变可见：onStart --> onResume
 * 进入后台：onPause --> onStop
 * 销毁：onPause --> onStop --> onDestroyView --> onDestroy
 *
 */
public abstract class BaseFragment extends Fragment {

    private BaseActivity mActivity;
    private MyProgressDialog progressDialog;

    /**
     * 此方法可以得到上下文对象
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /*
     * 返回一个需要展示的View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        View view = initView(inflater);
        initFindViewById(view);

        return view;
    }



    /**
     * 子类可以复写此方法初始化事件
     */
    protected  void initEvent(){

    }

    /*
     * 当Activity初始化之后可以在这里进行一些数据的初始化操作
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initEvent();
    }

    /**
     * 子类实现此抽象方法返回View进行展示
     *
     * @return
     */
    public abstract View initView(LayoutInflater inflater);

    /**
     * 初始化控件
     */
    protected abstract void initFindViewById(View view);

    /**
     * 子类在此方法中实现数据的初始化
     */
    protected  abstract void initData() ;

    /**
     * 从服务器获取数据
     * @param url 请求地址
     * @param jsonObject	请求参数
     * @param callback	请求回调
     */
    protected void requestServer(String url, JSONObject jsonObject, final BaseActivity.DataCallback callback) {

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
                            startActivity(new Intent(mActivity,LoginActivity.class));
                        }

                        @Override
                        public void needDoThings() {
                            ActivityCollector.finishAll();
                            startActivity(new Intent(mActivity,LoginActivity.class));
                        }
                    });
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                super.onFailure(error);
                alert("请求失败！" + error.getMessage(), null);
//				callback.onFailure(error);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                closeProgressDialog();
            }
        });
    }

        /**
         * 显示提示框
         */
    protected void showProgressDialog(String msg) {
        if(progressDialog == null){
            progressDialog = new MyProgressDialog(mActivity,msg);
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
        new MustDoThingDailog("叮咚",message,mActivity,mustdo);
    }

    protected Activity getBaseActivity(){
        return mActivity;
    }

}
